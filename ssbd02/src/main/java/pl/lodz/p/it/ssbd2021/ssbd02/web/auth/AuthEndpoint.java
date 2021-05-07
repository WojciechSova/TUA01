package pl.lodz.p.it.ssbd2021.ssbd02.web.auth;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.auth.CredentialsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces.AccountManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.security.JWTGenerator;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    public Response auth(@Context HttpServletRequest req, CredentialsDTO credentialsDTO) {

        Credential credential = new UsernamePasswordCredential(credentialsDTO.getLogin(), credentialsDTO.getPassword());
        CredentialValidationResult result = identityStoreHandler.validate(credential);

        String clientAddress = getClientIp(req);

        if (result.getStatus() != CredentialValidationResult.Status.VALID) {
            accountManagerLocal.registerBadLogin(credentialsDTO.getLogin(), clientAddress);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        accountManagerLocal.registerGoodLogin(credentialsDTO.getLogin(), clientAddress);
        return Response.accepted()
                .type("application/jwt")
                .entity(JWTGenerator.generateJWT(result))
                .build();
    }

    private String getClientIp(HttpServletRequest req) {
        String xForwardedFor = req.getHeader("X-Forwarded-For");
        if (xForwardedFor == null || "".equals(xForwardedFor.trim())) {
            return req.getRemoteAddr();
        }
        return xForwardedFor.contains(",") ? xForwardedFor.split(",")[0] : xForwardedFor;
    }
}
