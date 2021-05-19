package pl.lodz.p.it.ssbd2021.ssbd02.utils.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import java.util.Base64;
import java.util.Collections;

class JWTGeneratorTest {

    @Mock
    private CredentialValidationResult credentialValidationResult;

    private final CallerPrincipal callerPrincipal;

    private final String algorithm = "\"alg\":\"HS256\"";
    private final String issuer = "\"iss\":\"ssbd02\"";
    private final String subject = "\"sub\":\"Test\"";
    private final String auth = "\"auth\":\"test-group\"";

    private JWTGeneratorTest() {
        this.callerPrincipal = new CallerPrincipal("Test");
    }

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);

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

    @Test
    void updateJWT() {
        String jwt = JWTGenerator.generateJWT(credentialValidationResult);
        String updatedJwt = JWTGenerator.updateJWT(jwt, "current");

        String[] separatedUpdatedJwt = updatedJwt.split("\\.");
        String updatedJwtHeader = new String(Base64.getDecoder().decode(separatedUpdatedJwt[0]));
        String updatedJwtPayload = new String(Base64.getDecoder().decode(separatedUpdatedJwt[1]));

        Assertions.assertTrue(updatedJwtHeader.contains(algorithm));
        Assertions.assertTrue(updatedJwtPayload.contains(issuer));
        Assertions.assertTrue(updatedJwtPayload.contains(subject));
        Assertions.assertTrue(updatedJwtPayload.contains("current"));
    }
}
