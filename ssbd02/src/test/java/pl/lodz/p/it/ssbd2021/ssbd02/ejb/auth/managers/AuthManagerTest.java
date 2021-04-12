package pl.lodz.p.it.ssbd2021.ssbd02.ejb.auth.managers;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.auth.facades.AuthViewFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.auth.managers.AuthManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class AuthManagerTest {

    @Mock
    private AuthViewFacade authViewFacade;
    @InjectMocks
    private AuthManager authManager;

    private final String login = "test";
    private final String password = "password";
    private final String hashedPassword = DigestUtils.sha512Hex(password);
    private final List<String> accessLevels = Arrays.asList("admin", "pracownik");

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAccessLevelsValidCredentials() {
        Mockito.when(authViewFacade.findLevelsByCredentials(login, hashedPassword))
                .thenReturn(accessLevels);
        Assertions.assertEquals(accessLevels, authManager.getAccessLevels(login, password));
    }

    @Test
    void getAccessLevelsInvalidLogin() {
        Mockito.when(authViewFacade.findLevelsByCredentials("InvalidLogin", hashedPassword))
                .thenReturn(Collections.emptyList());
        Assertions.assertTrue(authManager.getAccessLevels("InvalidLogin", password).isEmpty());
    }

    @Test
    void getAccessLevelsInvalidPassword() {
        String hashedInvalidPassword = DigestUtils.sha512Hex("InvalidPassword");
        Mockito.when(authViewFacade.findLevelsByCredentials(login, hashedInvalidPassword))
                .thenReturn(Collections.emptyList());
        Assertions.assertTrue(authManager.getAccessLevels(login, "InvalidPassword").isEmpty());
    }
}
