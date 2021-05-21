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
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.OneTimeUrl;
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
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

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

        WebApplicationException loginException = assertThrows(WebApplicationException.class,
                () -> accountEndpoint.createAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair.of(accountWithoutLogin, Collections.emptyList()))));
        WebApplicationException passwordException = assertThrows(WebApplicationException.class,
                () -> accountEndpoint.createAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair.of(accountWithoutPassword, Collections.emptyList()))));
        WebApplicationException emailException = assertThrows(WebApplicationException.class,
                () -> accountEndpoint.createAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair.of(accountWithoutEmail, Collections.emptyList()))));

        assertEquals(400, loginException.getResponse().getStatus());
        assertEquals("Not all required fields were provided", loginException.getMessage());

        assertEquals(400, passwordException.getResponse().getStatus());
        assertEquals("Not all required fields were provided", passwordException.getMessage());

        assertEquals(400, emailException.getResponse().getStatus());
        assertEquals("Not all required fields were provided", emailException.getMessage());
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

        WebApplicationException loginException = assertThrows(WebApplicationException.class,
                () -> accountEndpoint.updateAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair
                        .of(accountWithoutLogin, Collections.emptyList())), securityContext, "notETag"));
        WebApplicationException versionException = assertThrows(WebApplicationException.class,
                () -> accountEndpoint.updateAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair
                        .of(accountWithoutVersion, Collections.emptyList())), securityContext, "notETag"));

        Response responseTag = accountEndpoint.updateAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair
                .of(account, Collections.emptyList())), securityContext, tag);

        account.setVersion(2L);

        WebApplicationException tagException = assertThrows(WebApplicationException.class,
                () -> accountEndpoint.updateAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair
                        .of(account, Collections.emptyList())), securityContext, tag));

        assertEquals(400, loginException.getResponse().getStatus());
        assertEquals("Not all required fields were provided", loginException.getMessage());

        assertEquals(400, versionException.getResponse().getStatus());
        assertEquals("Not all required fields were provided", versionException.getMessage());

        assertEquals(200, responseTag.getStatus());

        assertEquals(412, tagException.getResponse().getStatus());
        assertEquals("Not valid tag", tagException.getMessage());
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

        WebApplicationException noLoginException = assertThrows(WebApplicationException.class,
                () -> accountEndpoint.updateOwnAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair
                        .of(accountNoLogin, Collections.emptyList())), securityContext, tag));

        assertEquals(400, noLoginException.getResponse().getStatus());
        assertEquals("Not all required fields were provided", noLoginException.getMessage());
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

        WebApplicationException noVersionException = assertThrows(WebApplicationException.class,
                () -> accountEndpoint.updateOwnAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair
                        .of(accountNoVersion, Collections.emptyList())), securityContext, tag));

        assertEquals(400, noVersionException.getResponse().getStatus());
        assertEquals("Not all required fields were provided", noVersionException.getMessage());
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

        WebApplicationException invalidTagException = assertThrows(WebApplicationException.class,
                () -> accountEndpoint.updateOwnAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair
                        .of(account, Collections.emptyList())), securityContext, "notAValidTag"));

        assertEquals(412, invalidTagException.getResponse().getStatus());
        assertEquals("ETag not valid", invalidTagException.getMessage());
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

        WebApplicationException invalidTagException = assertThrows(WebApplicationException.class,
                () -> accountEndpoint.updateOwnAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair
                        .of(account, Collections.emptyList())), securityContext, tag));

        assertEquals(412, invalidTagException.getResponse().getStatus());
        assertEquals("ETag not valid", invalidTagException.getMessage());
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

        WebApplicationException invalidTagException = assertThrows(WebApplicationException.class,
                () -> accountEndpoint.updateOwnAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair
                        .of(account, Collections.emptyList())), securityContext, tag));

        assertEquals(412, invalidTagException.getResponse().getStatus());
        assertEquals("You can not change not your own account", invalidTagException.getMessage());
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
    void changePasswordExceptionTest() {
        passwordDTO.setOldPassword("");
        WebApplicationException oldPasswordEmpty = assertThrows(WebApplicationException.class,
                () -> accountEndpoint.changePassword(securityContext, passwordDTO));

        assertEquals(400, oldPasswordEmpty.getResponse().getStatus());
        assertEquals("Required fields are missing", oldPasswordEmpty.getMessage());

        passwordDTO.setNewPassword("");
        WebApplicationException newPasswordEmpty = assertThrows(WebApplicationException.class,
                () -> accountEndpoint.changePassword(securityContext, passwordDTO));

        assertEquals(400, newPasswordEmpty.getResponse().getStatus());
        assertEquals("Required fields are missing", newPasswordEmpty.getMessage());

        passwordDTO.setOldPassword(" ");
        WebApplicationException oldPasswordBlank = assertThrows(WebApplicationException.class,
                () -> accountEndpoint.changePassword(securityContext, passwordDTO));

        assertEquals(400, oldPasswordBlank.getResponse().getStatus());
        assertEquals("Required fields are missing", oldPasswordBlank.getMessage());

        passwordDTO.setNewPassword(" ");
        WebApplicationException newPasswordBlank = assertThrows(WebApplicationException.class,
                () -> accountEndpoint.changePassword(securityContext, passwordDTO));

        assertEquals(400, newPasswordBlank.getResponse().getStatus());
        assertEquals("Required fields are missing", newPasswordBlank.getMessage());

        passwordDTO.setOldPassword(null);
        WebApplicationException oldPasswordNull = assertThrows(WebApplicationException.class,
                () -> accountEndpoint.changePassword(securityContext, passwordDTO));

        assertEquals(400, oldPasswordNull.getResponse().getStatus());
        assertEquals("Required fields are missing", oldPasswordNull.getMessage());

        passwordDTO.setNewPassword(null);
        WebApplicationException newPasswordNull = assertThrows(WebApplicationException.class,
                () -> accountEndpoint.changePassword(securityContext, passwordDTO));

        assertEquals(400, newPasswordNull.getResponse().getStatus());
        assertEquals("Required fields are missing", newPasswordNull.getMessage());
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
        String url = "url";

        when(accountManager.confirmAccount(url)).thenReturn(true);

        response = accountEndpoint.confirmAccount(url);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        response = accountEndpoint.confirmAccount("invalid");
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    void sendChangeEmailAddressUrl() {
        Response response;

        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn("login");

        response = accountEndpoint.sendChangeEmailAddressUrl("email@mail.pl", securityContext);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        verify(accountManager).sendChangeEmailAddressUrl(loginCaptor.capture(), emailCaptor.capture(), requestedByCaptor.capture());
        assertEquals("login", loginCaptor.getValue());
        assertEquals("email@mail.pl", emailCaptor.getValue());
        assertEquals("login", requestedByCaptor.getValue());

        response = accountEndpoint.sendChangeEmailAddressUrl("nowy", securityContext);
        assertEquals(Response.Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());

        response = accountEndpoint.sendChangeEmailAddressUrl("nowy@", securityContext);
        assertEquals(Response.Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());

        response = accountEndpoint.sendChangeEmailAddressUrl("nowy@j.", securityContext);
        assertEquals(Response.Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());

    }

    @Test
    void changeEmailAddress() {
        account.setLogin("login");
        account.setEmail("stary@mail.com");
        Response response;
        String url = "url";

        when(accountManager.changeEmailAddress(url)).thenReturn(true);
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

        response = accountEndpoint.changeEmailAddress("invalid");
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
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
            assertEquals("Invalid email format", e.getLocalizedMessage());
        }

        try {
            accountEndpoint.sendPasswordResetAddressUrl("  ", securityContext);
        } catch (WebApplicationException e) {
            assertEquals(400, e.getResponse().getStatus());
            assertEquals("No email provided", e.getLocalizedMessage());
        }


        try {
            accountEndpoint.sendPasswordResetAddressUrl(null, securityContext);
        } catch (WebApplicationException e) {
            assertEquals(400, e.getResponse().getStatus());
            assertEquals("No email provided", e.getLocalizedMessage());
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
            assertEquals(400, e.getResponse().getStatus());
            assertEquals("Invalid URL", e.getLocalizedMessage());
        }

        try {
            accountEndpoint.resetPassword(url, "new");
        } catch (WebApplicationException e) {
            assertEquals(406, e.getResponse().getStatus());
            assertEquals("New password too short", e.getLocalizedMessage());
        }
    }
}
