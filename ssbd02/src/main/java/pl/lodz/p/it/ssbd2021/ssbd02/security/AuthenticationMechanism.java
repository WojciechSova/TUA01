package pl.lodz.p.it.ssbd2021.ssbd02.security;

import com.nimbusds.jwt.SignedJWT;

import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;

/**
 * Klasa implementująca mechanizm uwierzytelniania.
 *
 * @author Patryk Kolanek
 */
@ApplicationScoped
public class AuthenticationMechanism implements HttpAuthenticationMechanism {

    /**
     * Metoda wykonywana podczas wysłania zapytania przez klienta. Sprawdza czy zapytanie zawiera token JWT
     * oraz czy jest on aktualny.
     *
     * @param request            Zawiera zapytanie jakie wykonał klient.
     * @param response           Zawiera odpowiedź jaka zostanie wysłana do klienta.
     * @param httpMessageContext Kontekst służący do interakcji z kontenerem.
     * @return W przypadku braku przesłania tokenu zwraca NOT_DONE. W przypadku przesłania tokenu, którego walidacja się nie powiedzie
     * oraz przasłania tokenu który wygasł, metoda zwraca SEND_FAILURE. W przypadku poprawnego uwierzytelnienia
     * zwraca SUCCESS.
     * @throws AuthenticationException Kiedy przetwarzanie się nie powiodło.
     */
    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) throws AuthenticationException {
        String authorizationHeader = request.getHeader(SecurityConstants.AUTHORIZATION);

        if (Objects.isNull(authorizationHeader) || !authorizationHeader.startsWith(SecurityConstants.BEARER)) {
            return httpMessageContext.doNothing();
        }

        String jwt = authorizationHeader.substring(SecurityConstants.BEARER.length()).trim();

        if (!JWTVerifier.validateJwt(jwt)) {
            return httpMessageContext.responseUnauthorized();
        }

        try {
            SignedJWT signedJWT = SignedJWT.parse(jwt);

            String login = signedJWT.getJWTClaimsSet().getSubject();
            String groups = signedJWT.getJWTClaimsSet().getStringClaim(SecurityConstants.AUTH);

            Date actualDate = new Date();
            Date expirationDate = (Date) signedJWT.getJWTClaimsSet().getClaim(SecurityConstants.EXP);

            if (actualDate.after(expirationDate)) {
                return httpMessageContext.responseUnauthorized();
            }

            return httpMessageContext.notifyContainerAboutLogin(login, new HashSet<>(Arrays.asList(groups.split(SecurityConstants.GROUP_SPLIT_CONSTANT))));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        throw new AuthenticationException();
    }
}
