package pl.lodz.p.it.ssbd2021.ssbd02.utils.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.auth.managers.AuthManager;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.security.DatabaseIdentityStore;

import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class DatabaseIdentityStoreTest {

    @Mock
    private UsernamePasswordCredential usernamePasswordCredential;

    @Mock
    private AuthManager authManager;

    @InjectMocks
    private DatabaseIdentityStore databaseIdentityStore;

    private final String login = "test";
    private final String password = "password";

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);

        Mockito.when(usernamePasswordCredential.getCaller()).thenReturn(login);
        Mockito.when(usernamePasswordCredential.getPasswordAsString()).thenReturn(password);
    }

    @Test
    void validateIncorrectCredentialType() {
        Credential credential = Mockito.mock(Credential.class);

        Assertions.assertEquals(CredentialValidationResult.NOT_VALIDATED_RESULT, databaseIdentityStore.validate(credential));
    }

    @Test
    void validateIncorrectCredential() {
        Mockito.when(authManager.getAccessLevels(login, password))
                .thenReturn(Collections.emptyList());

        Assertions.assertEquals(CredentialValidationResult.INVALID_RESULT, databaseIdentityStore.validate(usernamePasswordCredential));
    }

    @Test
    void validateCorrectCredential() {
        List<String> accessGroups = Collections.singletonList("ADMIN");

        Mockito.when(authManager.getAccessLevels(login, password))
                .thenReturn(accessGroups);

        CredentialValidationResult credentialValidationResult = databaseIdentityStore.validate(usernamePasswordCredential);

        Assertions.assertEquals(login, credentialValidationResult.getCallerPrincipal().getName());

        Assertions.assertEquals(1, databaseIdentityStore.getCallerGroups(credentialValidationResult).size());
        Assertions.assertTrue(databaseIdentityStore.getCallerGroups(credentialValidationResult)
                .stream().anyMatch(element -> element.equals("ADMIN")));
    }

    @Test
    void validateCorrectCredentialWithDuplicatedGroup() {
        List<String> accessGroups = new ArrayList<>();
        accessGroups.add("ADMIN");
        accessGroups.add("ADMIN");

        Mockito.when(authManager.getAccessLevels(login, password))
                .thenReturn(accessGroups);

        CredentialValidationResult credentialValidationResult = databaseIdentityStore.validate(usernamePasswordCredential);

        Assertions.assertEquals(login, credentialValidationResult.getCallerPrincipal().getName());

        Assertions.assertEquals(1, databaseIdentityStore.getCallerGroups(credentialValidationResult).size());
        Assertions.assertTrue(databaseIdentityStore.getCallerGroups(credentialValidationResult)
                .stream().anyMatch(element -> element.equals("ADMIN")));
    }

    @Test
    void validateCorrectCredentialWithTwoGroups() {
        List<String> accessGroups = new ArrayList<>();
        accessGroups.add("ADMIN");
        accessGroups.add("EMPLOYEE");

        Mockito.when(authManager.getAccessLevels(login, password))
                .thenReturn(accessGroups);

        CredentialValidationResult credentialValidationResult = databaseIdentityStore.validate(usernamePasswordCredential);

        Assertions.assertEquals(login, credentialValidationResult.getCallerPrincipal().getName());

        Assertions.assertEquals(2, databaseIdentityStore.getCallerGroups(credentialValidationResult).size());
        Assertions.assertTrue(databaseIdentityStore.getCallerGroups(credentialValidationResult)
                .stream().anyMatch(element -> element.equals("ADMIN")));
        Assertions.assertTrue(databaseIdentityStore.getCallerGroups(credentialValidationResult)
                .stream().anyMatch(element -> element.equals("EMPLOYEE")));
    }
}
