package pl.lodz.p.it.ssbd2021.ssbd02.utils.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.security.AuthenticationMechanism;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.security.JWTGenerator;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.security.SecurityConstants;

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

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpMessageContext httpMessageContext;

    @Mock
    private CredentialValidationResult credentialValidationResult;

    @InjectMocks
    private AuthenticationMechanism authenticationMechanism;

    private final CallerPrincipal callerPrincipal;

    private AuthenticationMechanismTest() {
        this.callerPrincipal = new CallerPrincipal("Test");
    }

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);

        Mockito.when(credentialValidationResult.getCallerPrincipal()).thenReturn(callerPrincipal);
        Mockito.when(credentialValidationResult.getCallerGroups()).thenReturn(Collections.singleton("test-group"));

        Mockito.when(httpMessageContext.responseUnauthorized()).thenReturn(AuthenticationStatus.SEND_FAILURE);
        Mockito.when(httpMessageContext.doNothing()).thenReturn(AuthenticationStatus.NOT_DONE);
        Mockito.when(httpMessageContext
                .notifyContainerAboutLogin("Test", new HashSet<>(Collections.singletonList("test-group"))))
                .thenReturn(AuthenticationStatus.SUCCESS);
    }

    @Test
    void validateRequestWithoutAuthorizationHeader() {
        Mockito.when(request.getHeader(SecurityConstants.AUTHORIZATION)).thenReturn(null);

        try {
            Assertions.assertEquals(AuthenticationStatus.NOT_DONE, authenticationMechanism.validateRequest(request, response, httpMessageContext));
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
