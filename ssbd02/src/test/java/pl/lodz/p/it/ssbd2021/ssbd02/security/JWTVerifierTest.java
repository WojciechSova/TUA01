package pl.lodz.p.it.ssbd2021.ssbd02.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.security.JWTGenerator;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.security.JWTVerifier;

import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import java.util.Collections;

class JWTVerifierTest {

    @Mock
    private CredentialValidationResult credentialValidationResult;

    private final CallerPrincipal callerPrincipal;

    public JWTVerifierTest() {
        this.callerPrincipal = new CallerPrincipal("Test");
    }

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);

        Mockito.when(credentialValidationResult.getCallerPrincipal()).thenReturn(callerPrincipal);
        Mockito.when(credentialValidationResult.getCallerGroups()).thenReturn(Collections.singleton("test-group"));
    }

    @Test
    void validateJwt() {
        String validJwt = JWTGenerator.generateJWT(credentialValidationResult);
        String invalidJwt = validJwt + "invalid";

        Assertions.assertTrue(JWTVerifier.validateJwt(validJwt));

        Assertions.assertFalse(JWTVerifier.validateJwt(invalidJwt));
    }
}
