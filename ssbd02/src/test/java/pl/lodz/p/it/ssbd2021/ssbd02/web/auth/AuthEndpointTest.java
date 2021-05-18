package pl.lodz.p.it.ssbd2021.ssbd02.web.auth;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.auth.CredentialsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces.AccountManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.security.JWTVerifier;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.security.SecurityConstants;

import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class AuthEndpointTest {

    @Mock
    private IdentityStoreHandler identityStoreHandler;
    @Mock
    private CredentialValidationResult credentialValidationResult;
    @Mock
    private CallerPrincipal callerPrincipal;
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private AccountManagerLocal accountManagerLocal;
    @Mock
    private Account account;
    @Mock
    private final List<AccessLevel> accessLevels = new ArrayList<>();
    @Mock
    private final Pair<Account, List<AccessLevel>> accountPair = new ImmutablePair<>(account, accessLevels);

    @InjectMocks
    private AuthEndpoint authEndpoint;

    private final CredentialsDTO credentialsDTO = new CredentialsDTO("login", "password");
    private final CredentialsDTO wrongCredentialsDTO = new CredentialsDTO("login", "wrongPassword");

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);

        Mockito.when(identityStoreHandler.validate(Mockito.any()))
                .thenReturn(credentialValidationResult);

        Mockito.when(httpServletRequest.getHeader("X-Forwarded-For"))
                .thenReturn("192.168.1.1");

        Mockito.when(httpServletRequest.getHeader("Accept-Language"))
                .thenReturn("pl");

        Mockito.when(accountManagerLocal.getAccountWithActiveAccessLevels("admin")).thenReturn(accountPair);
        Mockito.when(accountPair.getKey()).thenReturn(account);
        Mockito.when(accountPair.getValue()).thenReturn(accessLevels);
    }

    @Test
    void authValid() {
        Mockito.when(credentialValidationResult.getStatus())
                .thenReturn(CredentialValidationResult.Status.VALID);

        Mockito.when(credentialValidationResult.getCallerPrincipal())
                .thenReturn(callerPrincipal);

        Mockito.when(callerPrincipal.getName())
                .thenReturn("login");

        Mockito.when(credentialValidationResult.getCallerGroups())
                .thenReturn(Set.of("ADMIN", "EMPLOYEE"));

        Response response = authEndpoint.auth(httpServletRequest, credentialsDTO);

        Assertions.assertEquals(202, response.getStatus());
        Assertions.assertTrue(JWTVerifier.validateJwt((String) response.getEntity()));

        Mockito.verify(accountManagerLocal, Mockito.times(1))
                .registerGoodLogin("login", "192.168.1.1");
    }

    @Test
    void authNotValid() {
        Mockito.when(credentialValidationResult.getStatus())
                .thenReturn(CredentialValidationResult.Status.INVALID);

        Response response = authEndpoint.auth(httpServletRequest, wrongCredentialsDTO);

        Assertions.assertEquals(401, response.getStatus());
        Assertions.assertNull(response.getEntity());

        Mockito.verify(accountManagerLocal, Mockito.times(1))
                .registerBadLogin("login", "192.168.1.1");
    }

    @Test
    void refreshTokenValid() {
        Mockito.when(httpServletRequest.getHeader(SecurityConstants.AUTHORIZATION))
                .thenReturn("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJBRE1JTiIsImlzcyI6InNzYmQwMiIsImV4cCI6MTYyMDk5NDI3Nn0.YPSaXui7WuSscIJLZ9QYADmAGz4b2Y2y7VT2OnfrPbs");
        Mockito.when(account.getActive()).thenReturn(true);
        Response response = authEndpoint.refreshToken(httpServletRequest);
        Assertions.assertEquals(202, response.getStatus());
    }

    @Test
    void refreshTokenInactiveUser() {
        Mockito.when(httpServletRequest.getHeader(SecurityConstants.AUTHORIZATION))
                .thenReturn("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJBRE1JTiIsImlzcyI6InNzYmQwMiIsImV4cCI6MTYyMDk5NDI3Nn0.YPSaXui7WuSscIJLZ9QYADmAGz4b2Y2y7VT2OnfrPbs");
        Mockito.when(account.getActive()).thenReturn(false);
        Response response = authEndpoint.refreshToken(httpServletRequest);
        Assertions.assertEquals(403, response.getStatus());
    }

    @Test
    void refreshTokenParseException() {
        Mockito.when(httpServletRequest.getHeader(SecurityConstants.AUTHORIZATION))
                .thenReturn("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJBRE1JTiIsImlzcyI6InNzYmQwMiIsImV4cCI6MTYyMDk5NDI3Nn0");
        Response response = authEndpoint.refreshToken(httpServletRequest);
        Assertions.assertEquals(400, response.getStatus());
    }
}
