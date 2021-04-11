package pl.lodz.p.it.ssbd2021.ssbd02.web.auth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.auth.CredentialsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.security.JWTVerifier;

import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.ws.rs.core.Response;
import java.util.Set;

class AuthEndpointTest {

    @Mock
    private IdentityStoreHandler identityStoreHandler;
    @Mock
    private CredentialValidationResult credentialValidationResult;
    @Mock
    private CallerPrincipal callerPrincipal;

    @InjectMocks
    private AuthEndpoint authEndpoint;

    private final CredentialsDTO credentialsDTO = new CredentialsDTO("login", "password");
    private final CredentialsDTO wrongCredentialsDTO = new CredentialsDTO("login", "wrongPassword");

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authValid() {
        Mockito.when(identityStoreHandler.validate(Mockito.any()))
                .thenReturn(credentialValidationResult);

        Mockito.when(credentialValidationResult.getStatus())
                .thenReturn(CredentialValidationResult.Status.VALID);

        Mockito.when(credentialValidationResult.getCallerPrincipal())
                .thenReturn(callerPrincipal);

        Mockito.when(callerPrincipal.getName())
                .thenReturn("login");

        Mockito.when(credentialValidationResult.getCallerGroups())
                .thenReturn(Set.of("ADMIN", "EMPLOYEE"));

        Response response = authEndpoint.auth(credentialsDTO);

        Assertions.assertEquals(202, response.getStatus());
        Assertions.assertTrue(JWTVerifier.validateJwt((String) response.getEntity()));
    }

    @Test
    void authNotValid() {
        Mockito.when(identityStoreHandler.validate(Mockito.any()))
                .thenReturn(credentialValidationResult);

        Mockito.when(credentialValidationResult.getStatus())
                .thenReturn(CredentialValidationResult.Status.INVALID);

        Response response = authEndpoint.auth(wrongCredentialsDTO);

        Assertions.assertEquals(401, response.getStatus());
        Assertions.assertNull(response.getEntity());
    }
}
