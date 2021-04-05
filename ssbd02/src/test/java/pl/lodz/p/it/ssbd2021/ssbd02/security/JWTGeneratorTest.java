package pl.lodz.p.it.ssbd2021.ssbd02.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.identitystore.CredentialValidationResult;

import java.util.Base64;
import java.util.Collections;

class JWTGeneratorTest {

    private final CredentialValidationResult credentialValidationResult = Mockito.mock(CredentialValidationResult.class);
    private final CallerPrincipal callerPrincipal;

    private final String algorithm;
    private final String issuer;
    private final String subject;
    private final String auth;

    private JWTGeneratorTest() {
        this.callerPrincipal = new CallerPrincipal("Test");
        this.algorithm = "alg\":\"HS256";
        this.issuer = "iss\":\"" + SecretConstants.ISSUER;
        this.subject = "sub\":\"Test";
        this.auth = "auth\":\"test-group";

        Mockito.when(credentialValidationResult.getCallerPrincipal()).thenReturn(callerPrincipal);
        Mockito.when(credentialValidationResult.getCallerGroups()).thenReturn(Collections.singleton("test-group"));
    }

    @Test
    void generateJWT() {
        String jwt = JWTGenerator.generateJWT(credentialValidationResult);
        String[] separatedJwt = jwt.split("\\.");

        String header = new String(Base64.getDecoder().decode(separatedJwt[0]));
        String payload = new String(Base64.getDecoder().decode(separatedJwt[1]));

        Assertions.assertTrue(header.contains(algorithm));

        Assertions.assertTrue(payload.contains(issuer));
        Assertions.assertTrue(payload.contains(subject));
        Assertions.assertTrue(payload.contains(auth));
    }
}
