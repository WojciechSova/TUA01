package pl.lodz.p.it.ssbd2021.ssbd02.web.auth;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.CredentialsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.security.JWTGenerator;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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

    /**
     * Metoda obsługująca operację uwierzytelnienia.
     *
     * @param credentialsDTO Dane logowania użytkownika.
     * @return W przypadku poprawnego uwierzytelnienia zwraca w odpowiedzi wygenerowany token JWT.
     * W przypadku niepoprawnego wyniku uwierzytelnienia zwracana jest odpowiedź o kodzie błędu HTTP 401, Unauthorized.
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.TEXT_PLAIN})
    public Response auth(CredentialsDTO credentialsDTO) {

        Credential credential = new UsernamePasswordCredential(credentialsDTO.getLogin(), credentialsDTO.getPassword());
        CredentialValidationResult result = identityStoreHandler.validate(credential);

        if (result.getStatus() != CredentialValidationResult.Status.VALID) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        return Response.accepted()
                .type("application/jwt")
                .entity(JWTGenerator.generateJWT(result))
                .build();
    }
}
