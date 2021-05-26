package pl.lodz.p.it.ssbd2021.ssbd02.ejb.auth.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.auth.facades.interfaces.AuthViewFacadeLocal;

import javax.security.enterprise.credential.Password;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthManagerTest {

    @Mock
    private AuthViewFacadeLocal authViewFacade;
    @InjectMocks
    private AuthManager authManager;
    private final String login = "test";
    private final String password = "password";
    private final List<String> accessLevels = Arrays.asList("admin", "pracownik");

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAccessLevelsValidCredentials() {
        Mockito.when(authViewFacade.findLevelsByCredentials(any(), any(Password.class)))
                .thenReturn(accessLevels);
        assertEquals(accessLevels, authManager.getAccessLevels(login, new Password(password)));
    }

    @Test
    void getAccessLevelsInvalidLogin() {
        when(authViewFacade.findLevelsByCredentials(any(), any(Password.class)))
                .thenReturn(Collections.emptyList());
        assertTrue(authManager.getAccessLevels("InvalidLogin", new Password(password)).isEmpty());
    }

    @Test
    void getAccessLevelsInvalidPassword() {
        when(authViewFacade.findLevelsByCredentials(any(), any(Password.class)))
                .thenReturn(Collections.emptyList());
        assertTrue(authManager.getAccessLevels(login, new Password("InvalidPassword")).isEmpty());
    }
}
