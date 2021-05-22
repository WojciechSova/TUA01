package pl.lodz.p.it.ssbd2021.ssbd02.web.mok;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.PasswordDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces.AccountManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.AccountMapper;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.signing.DTOIdentitySignerVerifier;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.nio.file.attribute.UserPrincipal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AccountEndpointTest {

    private final List<Account> accounts = new ArrayList<>();
    @Mock
    private AccountManagerLocal accountManager;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private UserPrincipal userPrincipal;
    @Captor
    private ArgumentCaptor<String> loginCaptor;
    @Captor
    private ArgumentCaptor<String> requestedByCaptor;
    @Captor
    private ArgumentCaptor<String> emailCaptor;
    @InjectMocks
    private AccountEndpoint accountEndpoint;
    private Account account;
    private PasswordDTO passwordDTO;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
        account = createAccount();
        passwordDTO = new PasswordDTO();
        passwordDTO.setOldPassword("P@ssword");
        passwordDTO.setNewPassword("newPassword");
    }

    private Account createAccount() {
        Account acc = new Account();
        acc.setLogin("ExampleLogin");
        acc.setPassword("P@ssword");
        acc.setActive(true);
        acc.setConfirmed(true);
        acc.setFirstName("Annabelle");
        acc.setLastName("Washington");
        acc.setEmail("example@example.com");
        acc.setPhoneNumber("48123456788");
        acc.setLanguage("PL");
        acc.setTimeZone("PL");
        acc.setModificationDate(Timestamp.from(Instant.now()));
        acc.setModifiedBy(null);
        acc.setCreationDate(Timestamp.valueOf("2020-03-21 11:21:15"));
        acc.setLastKnownGoodLogin(Timestamp.valueOf("2020-03-25 11:21:15"));
        acc.setLastKnownGoodLoginIp("111.111.111.111");
        acc.setLastKnownBadLogin(Timestamp.valueOf("2020-03-26 11:21:15"));
        acc.setLastKnownBadLoginIp("222.222.222.222");
        acc.setNumberOfBadLogins(2);
        return acc;
    }

    @Test
    void getAllAccountGenerals() {
        List<Pair<Account, List<AccessLevel>>> accountAccessPairList =
                Collections.singletonList(Pair.of(account, Collections.emptyList()));
        List<AccountGeneralDTO> expectedDTOList = accountAccessPairList.stream()
                .map(AccountMapper::createAccountGeneralDTOFromEntities)
                .collect(Collectors.toList());
        when(accountManager.getAllAccountsWithActiveAccessLevels()).thenReturn(accountAccessPairList);

        Response response = accountEndpoint.getAllAccountGenerals();

        assertEquals(expectedDTOList, response.getEntity());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void getAccountWithLogin() {
        String testLogin = "TestLogin";
        when(accountManager.getAccountWithLogin(testLogin))
                .thenReturn(Pair.of(account, Collections.emptyList()));

        Response response = accountEndpoint.getAccountWithLogin(testLogin);

        assertEquals(AccountMapper.createAccountDetailsDTOFromEntities(Pair.of(account, Collections.emptyList())),
                response.getEntity());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void getProfile() {
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn("ExampleLogin");
        when(accountManager.getAccountWithLogin("ExampleLogin"))
                .thenReturn(Pair.of(account, Collections.emptyList()));

        Response response = accountEndpoint.getProfile(securityContext);

        assertEquals(AccountMapper.createAccountDetailsDTOFromEntities(Pair.of(account, Collections.emptyList())),
                response.getEntity());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void createAccountTest() {
        doAnswer(invocationOnMock -> {
            accounts.add(account);
            return null;
        }).when(accountManager).createAccount(any());

        assertEquals(0, accounts.size());

        Response response = accountEndpoint.createAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair.of(account, Collections.emptyList())));
        assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
        assertEquals(1, accounts.size());
    }

    @Test
    void createAccountExceptionTest() {
        Account accountWithoutLogin = createAccount();
        accountWithoutLogin.setLogin(null);
        Account accountWithoutPassword = createAccount();
        accountWithoutPassword.setPassword(null);
        Account accountWithoutEmail = createAccount();
        accountWithoutEmail.setEmail(null);

        GeneralException loginException = assertThrows(GeneralException.class,
                () -> accountEndpoint.createAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair.of(accountWithoutLogin, Collections.emptyList()))));
        GeneralException passwordException = assertThrows(GeneralException.class,
                () -> accountEndpoint.createAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair.of(accountWithoutPassword, Collections.emptyList()))));
        GeneralException emailException = assertThrows(GeneralException.class,
                () -> accountEndpoint.createAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair.of(accountWithoutEmail, Collections.emptyList()))));

        assertEquals(400, loginException.getResponse().getStatus());
        assertEquals("HTTP 400 Bad Request", loginException.getMessage());
        assertEquals("ERROR.CONSTRAINT_VIOLATION", loginException.getResponse().getEntity());

        assertEquals(400, passwordException.getResponse().getStatus());
        assertEquals("HTTP 400 Bad Request", passwordException.getMessage());
        assertEquals("ERROR.CONSTRAINT_VIOLATION", loginException.getResponse().getEntity());

        assertEquals(400, emailException.getResponse().getStatus());
        assertEquals("HTTP 400 Bad Request", emailException.getMessage());
        assertEquals("ERROR.CONSTRAINT_VIOLATION", loginException.getResponse().getEntity());
    }

    @Test
    void updateAccountTest() {
        doAnswer(invocationOnMock -> {
            account.setVersion(1L);
            accounts.set(0, account);
            return null;
        }).when(accountManager).updateAccount(any(), any());
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);

        accounts.clear();
        account.setVersion(0L);
        accounts.add(account);

        String tag = DTOIdentitySignerVerifier.calculateDTOSignature(AccountMapper
                .createAccountDetailsDTOFromEntities(Pair.of(account, Collections.emptyList())));

        assertEquals(0L, accounts.get(0).getVersion());

        Response response = accountEndpoint.updateAccount(AccountMapper
                .createAccountDetailsDTOFromEntities(Pair.of(account, Collections.emptyList())), securityContext, tag);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(1L, accounts.get(0).getVersion());
    }

    @Test
    void updateAccountExceptionTest() {
        doAnswer(invocationOnMock -> {
            account.setVersion(1L);
            return null;
        }).when(accountManager).updateAccount(any(), any());
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);

        Account accountWithoutLogin = createAccount();
        accountWithoutLogin.setLogin(null);
        Account accountWithoutVersion = createAccount();
        accountWithoutVersion.setVersion(null);
        account.setVersion(0L);
        String tag = DTOIdentitySignerVerifier.calculateDTOSignature(AccountMapper
                .createAccountDetailsDTOFromEntities(Pair.of(account, Collections.emptyList())));

        GeneralException loginException = assertThrows(GeneralException.class,
                () -> accountEndpoint.updateAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair
                        .of(accountWithoutLogin, Collections.emptyList())), securityContext, "notETag"));
        GeneralException versionException = assertThrows(GeneralException.class,
                () -> accountEndpoint.updateAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair
                        .of(accountWithoutVersion, Collections.emptyList())), securityContext, "notETag"));

        Response responseTag = accountEndpoint.updateAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair
                .of(account, Collections.emptyList())), securityContext, tag);

        account.setVersion(2L);

        GeneralException tagException = assertThrows(GeneralException.class,
                () -> accountEndpoint.updateAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair
                        .of(account, Collections.emptyList())), securityContext, tag));

        assertEquals(412, loginException.getResponse().getStatus());
        assertEquals("HTTP 412 Precondition Failed", loginException.getMessage());
        assertEquals("ERROR.PRECONDITION_FAILED", loginException.getResponse().getEntity());

        assertEquals(412, versionException.getResponse().getStatus());
        assertEquals("HTTP 412 Precondition Failed", versionException.getMessage());
        assertEquals("ERROR.PRECONDITION_FAILED", loginException.getResponse().getEntity());

        assertEquals(200, responseTag.getStatus());

        assertEquals(412, tagException.getResponse().getStatus());
        assertEquals("HTTP 412 Precondition Failed", tagException.getMessage());
        assertEquals("ERROR.PRECONDITION_FAILED", loginException.getResponse().getEntity());
    }

    //region Update own account tests
    @Test
    void updateOwnAccountTest() {
        doAnswer(invocationOnMock -> {
            account.setVersion(1L);
            accounts.set(0, account);
            return null;
        }).when(accountManager).updateAccount(any(), anyString());
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn("ExampleLogin");

        accounts.clear();
        account.setVersion(0L);
        accounts.add(account);

        String tag = DTOIdentitySignerVerifier.calculateDTOSignature(AccountMapper
                .createAccountDetailsDTOFromEntities(Pair.of(account, Collections.emptyList())));

        assertEquals(0L, accounts.get(0).getVersion());

        Response response = accountEndpoint.updateOwnAccount(AccountMapper
                .createAccountDetailsDTOFromEntities(Pair.of(account, Collections.emptyList())), securityContext, tag);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(1L, accounts.get(0).getVersion());
        assertEquals(account.getLogin(), accounts.get(0).getLogin());
    }

    @Test
    void updateOwnAccountExceptionNoLoginTest() {
        Account accountNoLogin = createAccount();
        accountNoLogin.setLogin(null);
        accountNoLogin.setVersion(0L);

        doAnswer(invocationOnMock -> {
            accountNoLogin.setVersion(1L);
            return null;
        }).when(accountManager).updateAccount(any(), anyString());
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn("ExampleLogin");

        String tag = DTOIdentitySignerVerifier.calculateDTOSignature(AccountMapper
                .createAccountDetailsDTOFromEntities(Pair.of(accountNoLogin, Collections.emptyList())));

        GeneralException noLoginException = assertThrows(GeneralException.class,
                () -> accountEndpoint.updateOwnAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair
                        .of(accountNoLogin, Collections.emptyList())), securityContext, tag));

        assertEquals(400, noLoginException.getResponse().getStatus());
        assertEquals("HTTP 400 Bad Request", noLoginException.getMessage());
        assertEquals("ERROR.CONSTRAINT_VIOLATION", noLoginException.getResponse().getEntity());
    }

    @Test
    void updateOwnAccountExceptionNoVersionTest() {
        Account accountNoVersion = createAccount();
        accountNoVersion.setVersion(null);

        doAnswer(invocationOnMock -> {
            accountNoVersion.setVersion(1L);
            return null;
        }).when(accountManager).updateAccount(any(), anyString());
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn("ExampleLogin");

        String tag = DTOIdentitySignerVerifier.calculateDTOSignature(AccountMapper
                .createAccountDetailsDTOFromEntities(Pair.of(accountNoVersion, Collections.emptyList())));

        GeneralException noVersionException = assertThrows(GeneralException.class,
                () -> accountEndpoint.updateOwnAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair
                        .of(accountNoVersion, Collections.emptyList())), securityContext, tag));

        assertEquals(400, noVersionException.getResponse().getStatus());
        assertEquals("HTTP 400 Bad Request", noVersionException.getMessage());
        assertEquals("ERROR.CONSTRAINT_VIOLATION", noVersionException.getResponse().getEntity());
    }

    @Test
    void updateOwnAccountExceptionInvalidTagTest() {
        doAnswer(invocationOnMock -> {
            account.setVersion(1L);
            return null;
        }).when(accountManager).updateAccount(any(), anyString());
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn("ExampleLogin");

        account.setVersion(0L);

        String tag = DTOIdentitySignerVerifier.calculateDTOSignature(AccountMapper
                .createAccountDetailsDTOFromEntities(Pair.of(account, Collections.emptyList())));

        GeneralException invalidTagException = assertThrows(GeneralException.class,
                () -> accountEndpoint.updateOwnAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair
                        .of(account, Collections.emptyList())), securityContext, "notAValidTag"));

        assertEquals(412, invalidTagException.getResponse().getStatus());
        assertEquals("HTTP 412 Precondition Failed", invalidTagException.getMessage());
        assertEquals("ERROR.PRECONDITION_FAILED", invalidTagException.getResponse().getEntity());
    }

    @Test
    void updateOwnAccountExceptionOutdatedTagTest() {
        doAnswer(invocationOnMock -> {
            account.setVersion(1L);
            return null;
        }).when(accountManager).updateAccount(any(), anyString());
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn("ExampleLogin");

        account.setVersion(0L);

        String tag = DTOIdentitySignerVerifier.calculateDTOSignature(AccountMapper
                .createAccountDetailsDTOFromEntities(Pair.of(account, Collections.emptyList())));

        account.setVersion(5L);

        GeneralException invalidTagException = assertThrows(GeneralException.class,
                () -> accountEndpoint.updateOwnAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair
                        .of(account, Collections.emptyList())), securityContext, tag));

        assertEquals(412, invalidTagException.getResponse().getStatus());
        assertEquals("HTTP 412 Precondition Failed", invalidTagException.getMessage());
        assertEquals("ERROR.PRECONDITION_FAILED", invalidTagException.getResponse().getEntity());
    }

    @Test
    void updateOwnAccountExceptionIdentityMismatchTest() {
        doAnswer(invocationOnMock -> {
            account.setVersion(1L);
            return null;
        }).when(accountManager).updateAccount(any(), anyString());
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn("DifferentLogin");

        account.setVersion(0L);

        String tag = DTOIdentitySignerVerifier.calculateDTOSignature(AccountMapper
                .createAccountDetailsDTOFromEntities(Pair.of(account, Collections.emptyList())));

        GeneralException invalidTagException = assertThrows(GeneralException.class,
                () -> accountEndpoint.updateOwnAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair
                        .of(account, Collections.emptyList())), securityContext, tag));

        assertEquals(412, invalidTagException.getResponse().getStatus());
        assertEquals("HTTP 412 Precondition Failed", invalidTagException.getMessage());
        assertEquals("ERROR.PRECONDITION_FAILED", invalidTagException.getResponse().getEntity());
    }
    //endregion

    @Test
    void addAccessLevel() {
        AccessLevel accessLevel = new AccessLevel();
        accessLevel.setAccount(account);
        accessLevel.setLevel("Admin");
        accessLevel.setActive(false);

        account.setLogin("login");

        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn("Admin");

        doAnswer(invocationOnMock -> {
            accessLevel.setActive(true);
            return null;
        }).when(accountManager).addAccessLevel("Admin", "login", "ADMIN");

        Response response = accountEndpoint.addAccessLevel(securityContext, "login", "ADMIN");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(accessLevel.getActive());
    }

    @Test
    void removeAccessLevel() {
        AccessLevel accessLevel = new AccessLevel();
        accessLevel.setAccount(account);
        accessLevel.setLevel("Admin");
        accessLevel.setActive(true);

        account.setLogin("login");

        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn("Admin");

        doAnswer(invocationOnMock -> {
            accessLevel.setActive(false);
            return null;
        }).when(accountManager).removeAccessLevel("Admin", "login", "ADMIN");

        Response response = accountEndpoint.removeAccessLevel(securityContext, "login", "ADMIN");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertFalse(accessLevel.getActive());
    }

    @Test
    void blockAccountTest() {
        account.setLogin("Login");

        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn("ExampleLogin");

        doAnswer(invocationOnMock -> {
            account.setActive(false);
            return null;
        }).when(accountManager).changeActivity(account.getLogin(), false, "ExampleLogin");

        Response response = accountEndpoint.blockAccount(account.getLogin(), securityContext);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertFalse(account.getActive());
    }

    @Test
    void changePasswordTest() {
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn(account.getLogin());

        doAnswer(invocationOnMock -> {
            account.setPassword(invocationOnMock.getArgument(2));
            return null;
        }).when(accountManager).changePassword(account.getLogin(), passwordDTO.getOldPassword(), passwordDTO.getNewPassword());

        Response response = assertDoesNotThrow(() -> accountEndpoint.changePassword(securityContext, passwordDTO));
        assertEquals("newPassword", account.getPassword());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void unblockAccountTest() {
        account.setLogin("Login");
        account.setActive(false);

        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn("ExampleLogin");

        doAnswer(invocationOnMock -> {
            account.setActive(true);
            return null;
        }).when(accountManager).changeActivity(account.getLogin(), true, "ExampleLogin");

        Response response = accountEndpoint.unblockAccount(account.getLogin(), securityContext);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(account.getActive());
    }

    @Test
    void confirmAccount() {
        Response response;
        String url = "IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII";

        doNothing().when(accountManager).confirmAccount(url);

        response = accountEndpoint.confirmAccount(url);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void sendChangeEmailAddressUrl() {
        Response response;

        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn("login");

        response = accountEndpoint.sendChangeEmailAddressUrl("email@mail.pl", securityContext);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        verify(accountManager).sendChangeEmailAddressUrl(loginCaptor.capture(), emailCaptor.capture(), requestedByCaptor.capture());
    }

    @Test
    void sendChangeEmailAddressUrlException() {
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn("login");

        GeneralException exception = assertThrows(GeneralException.class,
                () -> accountEndpoint.sendChangeEmailAddressUrl("nowy", securityContext));

        assertEquals(400, exception.getResponse().getStatus());
        assertEquals("ERROR.CONSTRAINT_VIOLATION", exception.getResponse().getEntity());
        assertEquals("HTTP 400 Bad Request", exception.getMessage());

        exception = assertThrows(GeneralException.class,
                        () -> accountEndpoint.sendChangeEmailAddressUrl("nowy@", securityContext));

                assertEquals(400, exception.getResponse().getStatus());
                assertEquals("ERROR.CONSTRAINT_VIOLATION", exception.getResponse().getEntity());
                assertEquals("HTTP 400 Bad Request", exception.getMessage());

        exception = assertThrows(GeneralException.class,
                        () -> accountEndpoint.sendChangeEmailAddressUrl("nowy@j.", securityContext));

                assertEquals(400, exception.getResponse().getStatus());
                assertEquals("ERROR.CONSTRAINT_VIOLATION", exception.getResponse().getEntity());
                assertEquals("HTTP 400 Bad Request", exception.getMessage());
    }

    @Test
    void changeEmailAddress() {
        account.setLogin("login");
        account.setEmail("stary@mail.com");
        Response response;
        String url = "IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII";

        doNothing().when(accountManager).changeEmailAddress(url);
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn("login");

        doAnswer(invocationOnMock -> {
            account.setEmail("nowy@mail.com");
            account.setModifiedBy(account);
            return true;
        }).when(accountManager).changeEmailAddress(url);

        response = accountEndpoint.changeEmailAddress(url);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("nowy@mail.com", account.getEmail());

    }

    @Test
    void sendPasswordResetAddressUrl() {
        String email1 = "email@a.com";
        Response response;
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn("login");

        response = accountEndpoint.sendPasswordResetAddressUrl(email1, securityContext);

        verify(accountManager).sendPasswordResetAddressUrl(email1, "login");
        assertEquals(200, response.getStatus());

        try {
            accountEndpoint.sendPasswordResetAddressUrl("wrongEmail", securityContext);
        } catch (WebApplicationException e) {
            assertEquals(400, e.getResponse().getStatus());
            assertEquals("ERROR.CONSTRAINT_VIOLATION", e.getResponse().getEntity());
            assertEquals("HTTP 400 Bad Request", e.getLocalizedMessage());
        }

        try {
            accountEndpoint.sendPasswordResetAddressUrl("  ", securityContext);
        } catch (WebApplicationException e) {
            assertEquals(400, e.getResponse().getStatus());
            assertEquals("ERROR.CONSTRAINT_VIOLATION", e.getResponse().getEntity());
            assertEquals("HTTP 400 Bad Request", e.getLocalizedMessage());
        }


        try {
            accountEndpoint.sendPasswordResetAddressUrl(null, securityContext);
        } catch (WebApplicationException e) {
            assertEquals(400, e.getResponse().getStatus());
            assertEquals("ERROR.CONSTRAINT_VIOLATION", e.getResponse().getEntity());
            assertEquals("HTTP 400 Bad Request", e.getLocalizedMessage());
        }

    }

    @Test
    void resetPassword() {
        String url = "RkbuoN5REPt6TzMvBcUHWQmtKcBaDO4c";
        String newPassword = "newPassword";

        Response response = accountEndpoint.resetPassword(url, newPassword);

        verify(accountManager).resetPassword(url, newPassword);

        assertEquals(200, response.getStatus());

        try {
            accountEndpoint.resetPassword("shortUrl", newPassword);
        } catch (WebApplicationException e) {
            assertEquals(406, e.getResponse().getStatus());
            assertEquals("ERROR.URL_INVALID", e.getResponse().getEntity());
            assertEquals("HTTP 406 Not Acceptable", e.getLocalizedMessage());
        }

        try {
            accountEndpoint.resetPassword(url, "new");
        } catch (WebApplicationException e) {
            assertEquals(400, e.getResponse().getStatus());
            assertEquals("ERROR.CONSTRAINT_VIOLATION", e.getResponse().getEntity());
            assertEquals("HTTP 400 Bad Request", e.getLocalizedMessage());
        }
    }
}
