package pl.lodz.p.it.ssbd2021.ssbd02.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import java.util.Collections;

class JWTVerifierTest {

    private final CredentialValidationResult credentialValidationResult = Mockito.mock(CredentialValidationResult.class);
    private final CallerPrincipal callerPrincipal;

    public JWTVerifierTest() {
        this.callerPrincipal = new CallerPrincipal("Test");

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
