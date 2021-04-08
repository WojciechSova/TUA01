package pl.lodz.p.it.ssbd2021.ssbd02.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashSet;

class AuthenticationMechanismTest {

    private final AuthenticationMechanism authenticationMechanism;

    private final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    private final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    private final HttpMessageContext httpMessageContext = Mockito.mock(HttpMessageContext.class);

    private final CredentialValidationResult credentialValidationResult = Mockito.mock(CredentialValidationResult.class);
    private final CallerPrincipal callerPrincipal;

    private AuthenticationMechanismTest() {
        this.authenticationMechanism = new AuthenticationMechanism();
        this.callerPrincipal = new CallerPrincipal("Test");

        Mockito.when(credentialValidationResult.getCallerPrincipal()).thenReturn(callerPrincipal);
        Mockito.when(credentialValidationResult.getCallerGroups()).thenReturn(Collections.singleton("test-group"));

        Mockito.when(httpMessageContext.responseUnauthorized()).thenReturn(AuthenticationStatus.SEND_FAILURE);
        Mockito.when(httpMessageContext.notifyContainerAboutLogin("Test", new HashSet<>(Collections.singletonList("test-group"))))
                .thenReturn(AuthenticationStatus.SUCCESS);
    }

    @Test
    void validateRequestWithoutAuthorizationHeader() {
        Mockito.when(request.getHeader(SecurityConstants.AUTHORIZATION)).thenReturn(null);

        try {
            Assertions.assertEquals(AuthenticationStatus.SEND_FAILURE, authenticationMechanism.validateRequest(request, response, httpMessageContext));
        } catch (AuthenticationException ignored) {}
    }

    @Test
    void validateRequestWithInvalidJwt() {
        String invalidJwt = JWTGenerator.generateJWT(credentialValidationResult) + "invalid";

        Mockito.when(request.getHeader(SecurityConstants.AUTHORIZATION)).thenReturn(SecurityConstants.BEARER + invalidJwt);

        try {
            Assertions.assertEquals(AuthenticationStatus.SEND_FAILURE, authenticationMechanism.validateRequest(request, response, httpMessageContext));
        } catch (AuthenticationException ignored) {}
    }

    @Test
    void validateRequest() {
        String validJwt = JWTGenerator.generateJWT(credentialValidationResult);

        Mockito.when(request.getHeader(SecurityConstants.AUTHORIZATION)).thenReturn(SecurityConstants.BEARER + validJwt);

        try {
            Assertions.assertEquals(AuthenticationStatus.SUCCESS, authenticationMechanism.validateRequest(request, response, httpMessageContext));
        } catch (AuthenticationException ignored) {}
    }
}
