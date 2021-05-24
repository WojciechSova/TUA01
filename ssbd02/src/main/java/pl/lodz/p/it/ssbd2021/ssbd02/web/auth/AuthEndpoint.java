package pl.lodz.p.it.ssbd2021.ssbd02.web.auth;

import com.nimbusds.jwt.SignedJWT;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.auth.CredentialsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces.AccountManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.AccountExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.security.JWTGenerator;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.security.SecurityConstants;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.AccessLocalException;
import javax.ejb.EJBAccessException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Klasa ziarna CDI o zasięgu żądania.
 * Znajdują się w niej metody obsługujące zapytania użytkownika związne z uwierzytelnieniem.
 *
 * @author Kacper Świercz
 */
@RequestScoped
@Path("auth")
public class AuthEndpoint {

    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @Inject
    private AccountManagerLocal accountManagerLocal;

    private static final Logger logger = LogManager.getLogger();

    /**
     * Metoda obsługująca operację uwierzytelnienia.
     * W bazie danych zapisywana jest data logowania oraz adres IP, z którego się zalogowano.
     * W przypadku niepoprawngo uwierzytelnienia również zapisywana jest data niepoprawnego uwierzytelnienia oraz adres IP.
     *
     * @param credentialsDTO Dane logowania użytkownika.
     * @return W przypadku poprawnego uwierzytelnienia zwraca w odpowiedzi wygenerowany token JWT.
     * W przypadku niepoprawnego wyniku uwierzytelnienia zwracana jest odpowiedź o kodzie błędu HTTP 401 - Unauthorized.
     */
    @POST
    @PermitAll
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.TEXT_PLAIN})
    public Response auth(@Context HttpServletRequest req, @Valid CredentialsDTO credentialsDTO) {
        try {
            Credential credential = new UsernamePasswordCredential(credentialsDTO.getLogin(), credentialsDTO.getPassword());
            CredentialValidationResult result = identityStoreHandler.validate(credential);

            String clientAddress = getClientIp(req);
            String language = getLanguage(req);

            if (result.getStatus() != CredentialValidationResult.Status.VALID) {
                accountManagerLocal.registerBadLogin(credentialsDTO.getLogin(), clientAddress);
                logger.info("Failed logon attempt, user: {} (ip: {})",
                        credentialsDTO.getLogin(), clientAddress);
                throw CommonExceptions.createUnauthorizedException();
            }

            if (result.getCallerGroups().contains("ADMIN")) {
                accountManagerLocal.notifyAdminAboutLogin(result.getCallerPrincipal().getName(), clientAddress);
            }

            accountManagerLocal.registerGoodLogin(credentialsDTO.getLogin(), clientAddress);
            accountManagerLocal.updateLanguage(credentialsDTO.getLogin(), language);
            String timezone = accountManagerLocal.getTimezone(result.getCallerPrincipal().getName());

            logger.info("New successful logon, authenticated user: {} (ip: {})",
                    credentialsDTO.getLogin(), clientAddress);

            return Response.accepted()
                    .type("application/jwt")
                    .entity(JWTGenerator.generateJWT(result, timezone))
                    .build();

        } catch (GeneralException generalException) {
            if (generalException.getResponse().getStatus() == 410) {
                throw CommonExceptions.createUnauthorizedException();
            } else {
                throw generalException;
            }
        } catch (EJBAccessException | AccessLocalException accessExcept) {
            throw CommonExceptions.createForbiddenException();
        } catch (Exception e) {
            throw CommonExceptions.createUnknownException();
        }
    }

    /**
     * Metoda obsługująca odświeżanie tokenu na podstawie aktualnego tokenu.
     *
     * @param httpServletRequest Obiekt reprezentujący żądanie
     * @return Gdy konto jest aktywne, zwraca w odpowiedzi zaktualizowany token JWT,
     * gdy konto jest nieaktywne, zwraca odpowiedź o kodzie błędu HTTP 403 - Forbidden,
     * w przypadku wystąpienia wyjątku ParseException, zwraca odpowiedź o kodzie błędu HTTP 400 - Bad request
     */
    @GET
    @RolesAllowed({"ADMIN", "EMPLOYEE", "CLIENT"})
    @Produces({MediaType.APPLICATION_JSON})
    public Response refreshToken(@Context HttpServletRequest httpServletRequest) {
        try {
            String authHeader = httpServletRequest.getHeader(SecurityConstants.AUTHORIZATION);
            String serializedJWT = authHeader.substring(SecurityConstants.BEARER.length()).trim();
            try {
                String login = SignedJWT.parse(serializedJWT).getJWTClaimsSet().getSubject();
                Pair<Account, List<AccessLevel>> account = accountManagerLocal.getAccountWithActiveAccessLevels(login);
                String accessLevels = account.getValue().stream().map(AccessLevel::getLevel)
                        .collect(Collectors.joining(SecurityConstants.GROUP_SPLIT_CONSTANT));
                String timezone = account.getKey().getTimeZone();
                if (account.getKey().getActive()) {
                    if (account.getValue().isEmpty()){
                        throw CommonExceptions.createForbiddenException();
                    }
                    return Response.accepted()
                            .entity(JWTGenerator.updateJWT(serializedJWT, accessLevels, timezone))
                            .build();
                } else {
                    throw AccountExceptions.createForbiddenException(AccountExceptions.ERROR_ACCOUNT_INACTIVE);
                }
            } catch (ParseException e) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (GeneralException generalException) {
            throw generalException;
        } catch (EJBAccessException | AccessLocalException accessExcept) {
            throw CommonExceptions.createForbiddenException();
        } catch (Exception e) {
            throw CommonExceptions.createUnknownException();
        }
    }

    /**
     * Metoda służąca do logowania zmiany aktualnego poziomu dostępu przez użytkownika
     *
     * @param securityContext    Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika
     * @param httpServletRequest Obiekt reprezentujący żądanie
     * @param accessLevel        Poziom dostępu, na który przełączył się użytkownik
     * @return Kod odpowiedzi 200
     */
    @POST
    @RolesAllowed({"ADMIN", "CLIENT", "EMPLOYEE"})
    @Path("change/accesslevel")
    public Response informAboutAccessLevelChange(@Context SecurityContext securityContext, @Context HttpServletRequest httpServletRequest, String accessLevel) {
        String clientAddress = getClientIp(httpServletRequest);
        logger.info("The user with login {} changed the access level to {} (ip: {})",
                securityContext.getUserPrincipal().getName(), accessLevel, clientAddress);
        return Response.ok().build();
    }

    /**
     * Metoda wyznaczająca adres ip, z którego zostało wysłane żądanie.
     * W pierwszej kolejności brana pod uwagę jest pierwsza wartość nagłówka X-Forwarded-For.
     *
     * @param req Zapytanie HTTP
     * @return Adres ip, z którego zostało wysłane żądanie.
     */
    private String getClientIp(HttpServletRequest req) {
        String xForwardedFor = req.getHeader("X-Forwarded-For");
        if (xForwardedFor == null || "".equals(xForwardedFor.trim())) {
            return req.getRemoteAddr();
        }
        return xForwardedFor.contains(",") ? xForwardedFor.split(",")[0] : xForwardedFor;
    }

    /**
     * Metoda wyznaczająca język przeglądarki, z której zostało wysłane żądanie.
     * W pierwszej kolejności pod uwagę brana jest pierwsza wartość nagłówka Accept-Language.
     * Akceptowalne języki to takie zawierające en lub pl.
     * W przypadku braku znalezienia odpowiedniego języka domyślną wartością jest en.
     *
     * @param req Zapytanie HTTP
     * @return Adres ip, z którego zostało wysłane żądanie.
     */
    private String getLanguage(HttpServletRequest req) {
        String acceptLanguage = req.getHeader("Accept-Language");
        if (acceptLanguage == null || "".equals(acceptLanguage.trim())) {
            return "en";
        }
        String[] languages = acceptLanguage.split(",");
        for (String language : languages) {
            if (language.contains("en")) {
                return "en";
            }
            if (language.contains("pl")) {
                return "pl";
            }
        }
        return "en";
    }
}
