package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccessLevelFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccountFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.OneTimeUrlFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.utils.interfaces.EmailSenderLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.OneTimeUrl;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mok.AccessLevelExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mok.AccountExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;

import javax.security.enterprise.credential.Password;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static java.time.temporal.ChronoUnit.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountManagerTest {

    @Spy
    private final Account a1 = new Account();
    @Spy
    private final Account a2 = new Account();
    @Spy
    private final Account a3 = new Account();
    @Spy
    private final Account a4 = new Account();
    @Spy
    private final AccessLevel al4 = new AccessLevel();
    @Spy
    private final OneTimeUrl oneTimeUrl = new OneTimeUrl();
    private final String login1 = "a1Login";
    private final String email1 = "a1Email@domain.com";
    private final String phoneNumber1 = "111111111";
    private final String password1 = "a1Password";
    private final String login2 = "a2Login";
    private final String phoneNumber2 = "222222222";
    private final String email3 = "a3Email@domain.com";
    private final String password3 = "a3Password";
    private final String phoneNumber3 = "333333333";
    private final String login4 = "a4login";
    private final String email4 = "a4Email@domain.com";
    private final String level = "CLIENT";
    private final String oldPassword = "oldPassword";
    private final String newPassword = "newPassword";
    private final String invalidPassword = "invalidPassword";
    private final String levelClient = "CLIENT";
    private final String levelAdmin = "ADMIN";
    private final String levelEmployee = "EMPLOYEE";
    private final String randomUrl = RandomStringUtils.randomAlphanumeric(32);
    private final AccessLevel al1 = new AccessLevel();
    private final AccessLevel al2 = new AccessLevel();
    private final AccessLevel al3 = new AccessLevel();
    private final AccessLevel al5 = new AccessLevel();
    @Spy
    private final List<AccessLevel> accessLevels1 = new ArrayList<>();
    private final List<AccessLevel> accessLevels2 = new ArrayList<>();
    private final List<Pair<Account, List<AccessLevel>>> pairList = new ArrayList<>();
    private final List<OneTimeUrl> oneTimeUrls = new ArrayList<>();
    @Mock
    private AccountFacadeLocal accountFacadeLocal;
    @Mock
    private AccessLevelFacadeLocal accessLevelFacadeLocal;
    @Mock
    private EmailSenderLocal emailSender;
    @Mock
    private OneTimeUrlFacadeLocal oneTimeUrlFacadeLocal;
    @InjectMocks
    private AccountManager accountManager;
    @Captor
    private ArgumentCaptor<OneTimeUrl> urlCaptor;
    private List<Account> accounts;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);

        doNothing().when(emailSender).sendRegistrationEmail(anyString(), anyString(), anyString(), anyString());
        doNothing().when(emailSender).sendChangedActivityEmail(anyString(), anyString(), anyString(), anyBoolean());
        doNothing().when(emailSender).sendModificationEmail(anyString(), anyString(), anyString());
        doNothing().when(emailSender).sendAddAccessLevelEmail(anyString(), anyString(), anyString(), anyString());
        doNothing().when(emailSender).sendRemoveAccessLevelEmail(anyString(), anyString(), anyString(), anyString());
        doNothing().when(emailSender).sendRemovalEmail(anyString(), anyString(), anyString());
        doNothing().when(emailSender).sendAdminAuthenticationEmail(anyString(), anyString(), anyString(), anyString());

        al5.setActive(false);

        accessLevels1.add(al1);
        accessLevels1.add(al2);
        accessLevels2.add(al3);
        accessLevels2.add(al5);

        accounts = new ArrayList<>();
        accounts.add(a1);
        accounts.add(a2);
        pairList.add(new ImmutablePair<>(a1, accessLevels1));
        pairList.add(new ImmutablePair<>(a2, accessLevels2));
    }

    @Test
    void getAllAccountsWithActiveAccessLevelsTest() {
        when(accountFacadeLocal.findAll()).thenReturn(Arrays.asList(a1, a2));
        when(accessLevelFacadeLocal.findAllActiveByAccount(a1)).thenReturn(accessLevels1);
        when(accessLevelFacadeLocal.findAllActiveByAccount(a2)).thenReturn(accessLevels2);

        pairList.get(1).getValue().remove(al5); // Remove al5 from compared list cause it's inactive

        List<Pair<Account, List<AccessLevel>>> testedPairList = accountManager.getAllAccountsWithActiveAccessLevels();

        assertEquals(pairList, testedPairList);
        assertEquals(2, testedPairList.size());
        assertEquals(2, testedPairList.get(0).getValue().size());
        assertEquals(1, testedPairList.get(1).getValue().size());
        assertEquals(accessLevels1, testedPairList.get(0).getValue());
        assertEquals(accessLevels2, testedPairList.get(1).getValue());
        assertEquals(a1, testedPairList.get(0).getKey());
        assertEquals(a2, testedPairList.get(1).getKey());
        assertTrue(testedPairList.get(1).getValue().stream()
                .noneMatch(accessLevel -> accessLevel.equals(al5)));
    }

    @Test
    void getAccountWithLoginTest() {
        when(accountFacadeLocal.findByLogin(login1)).thenReturn(a1);
        when(accessLevelFacadeLocal.findAllByAccount(a1)).thenReturn(accessLevels1);
        assertEquals(Pair.of(a1, accessLevels1), accountManager.getAccountWithLogin(login1));
        assertEquals(a1, accountManager.getAccountWithLogin(login1).getLeft());
        assertEquals(a1, accountManager.getAccountWithLogin(login1).getKey());
        assertEquals(accessLevels1, accountManager.getAccountWithLogin(login1).getRight());
        assertEquals(accessLevels1, accountManager.getAccountWithLogin(login1).getValue());
    }

    @Test
    void getAccountWithActiveAccessLevels() {
        when(accountFacadeLocal.findByLogin(login1)).thenReturn(a1);
        when(accessLevelFacadeLocal.findAllActiveByAccount(a1)).thenReturn(accessLevels1);
        assertEquals(Pair.of(a1, accessLevels1), accountManager.getAccountWithActiveAccessLevels(login1));
        assertEquals(a1, accountManager.getAccountWithActiveAccessLevels(login1).getLeft());
        assertEquals(a1, accountManager.getAccountWithActiveAccessLevels(login1).getKey());
        assertEquals(accessLevels1, accountManager.getAccountWithActiveAccessLevels(login1).getRight());
        assertEquals(accessLevels1, accountManager.getAccountWithActiveAccessLevels(login1).getValue());
    }

    @Test
    void createAccountTest() {
        doAnswer(invocationOnMock -> {
            accounts.add(a3);
            return null;
        }).when(accountFacadeLocal).create(a3);

        doAnswer(invocationOnMock -> {
            accessLevels1.add(al4);
            return null;
        }).when(accessLevelFacadeLocal).create(any());

        doAnswer(invocationOnMock -> {
            oneTimeUrl.setUrl(randomUrl);
            oneTimeUrls.add(oneTimeUrl);
            return null;
        }).when(oneTimeUrlFacadeLocal).create(any());

        when(al4.getAccount()).thenReturn(a3);
        when(al4.getLevel()).thenReturn(levelClient);
        when(a3.getEmail()).thenReturn(email3);
        when(a3.getPassword()).thenReturn(password3);
        when(a3.getPhoneNumber()).thenReturn(phoneNumber3);
        when(oneTimeUrl.getAccount()).thenReturn(a3);

        assertEquals(2, accounts.size());
        assertEquals(2, accessLevels1.size());

        accountManager.createAccount(a3);

        assertEquals(3, accounts.size());
        assertEquals(3, accessLevels1.size());
        assertEquals(a3.hashCode(), accounts.get(2).hashCode());
        assertEquals(al4.getAccount(), accessLevels1.get(2).getAccount());
        assertEquals(al4.getLevel(), accessLevels1.get(2).getLevel());
        assertEquals(randomUrl, oneTimeUrls.get(0).getUrl());
        assertEquals(oneTimeUrl.getAccount(), oneTimeUrls.get(0).getAccount());
    }

    @Test
    void createAccountExceptionTest() {
        when(a1.getEmail()).thenReturn(email1);
        when(a1.getLogin()).thenReturn(login1);
        when(a1.getPhoneNumber()).thenReturn(phoneNumber1);

        when(a2.getEmail()).thenReturn(email1);
        when(a2.getLogin()).thenReturn(login2);
        when(a2.getPhoneNumber()).thenReturn(phoneNumber2);

        when(a3.getEmail()).thenReturn(email3);
        when(a3.getLogin()).thenReturn(login1);
        when(a3.getPhoneNumber()).thenReturn(phoneNumber3);

        when(a4.getEmail()).thenReturn(email4);
        when(a4.getLogin()).thenReturn(login4);
        when(a4.getPhoneNumber()).thenReturn(phoneNumber1);

        when(accountFacadeLocal.findAll()).thenReturn(List.of(a1));
    }

    @Test
    void addAccessLevel() {
        AccessLevel employeeAccessLevel = new AccessLevel();
        employeeAccessLevel.setAccount(a1);
        employeeAccessLevel.setLevel(levelEmployee);

        a1.setLogin(login1);
        a1.setEmail(email1);
        a2.setLogin(login2);

        al1.setAccount(a1);
        al1.setLevel(levelClient);
        al1.setActive(true);

        al2.setAccount(a1);
        al2.setLevel(levelAdmin);
        al2.setActive(false);

        when(accountFacadeLocal.findByLogin(login1)).thenReturn(a1);
        when(accountFacadeLocal.findByLogin(login2)).thenReturn(a2);
        when(accessLevelFacadeLocal.findAllByAccount(a1)).thenReturn(accessLevels1);

        doAnswer(invocationOnMock -> {
            accessLevels1.get(1).setActive(true);
            return null;
        }).when(accessLevelFacadeLocal).edit(any());

        doAnswer(invocationOnMock -> {
            accessLevels1.add(employeeAccessLevel);
            return null;
        }).when(accessLevelFacadeLocal).create(any());

        AccessLevelExceptions accessLevelExceptions = assertThrows(AccessLevelExceptions.class, () -> accountManager.addAccessLevel(a2.getLogin(), a1.getLogin(), "random"));
        assertTrue(accessLevels1.get(0).getActive());
        assertFalse(accessLevels1.get(1).getActive());

        assertAll(
                () -> assertEquals(400, accessLevelExceptions.getResponse().getStatus()),
                () -> assertEquals(AccessLevelExceptions.ERROR_NO_ACCESS_LEVEL, accessLevelExceptions.getResponse().getEntity())
        );

        accountManager.addAccessLevel(a2.getLogin(), a1.getLogin(), levelClient);
        assertTrue(accessLevels1.get(0).getActive());
        assertFalse(accessLevels1.get(1).getActive());


        accountManager.addAccessLevel(a2.getLogin(), a1.getLogin(), levelAdmin);
        assertTrue(accessLevels1.get(0).getActive());
        assertTrue(accessLevels1.get(1).getActive());


        accountManager.addAccessLevel(a2.getLogin(), a1.getLogin(), levelEmployee);
        assertEquals(3, accessLevels1.size());
        assertTrue(accessLevels1.get(0).getActive());
        assertTrue(accessLevels1.get(1).getActive());
        assertTrue(accessLevels1.get(2).getActive());
        assertEquals(a1, accessLevels1.get(2).getAccount());
    }

    @Test
    void removeAccessLevel() {
        a1.setLogin(login1);
        a1.setEmail(email1);

        al1.setAccount(a1);
        al1.setLevel(levelClient);
        al1.setActive(false);

        al2.setAccount(a1);
        al2.setLevel(levelAdmin);
        al2.setActive(true);

        when(accountFacadeLocal.findByLogin(login1)).thenReturn(a1);
        when(accountFacadeLocal.findByLogin(login2)).thenReturn(a2);
        when(accessLevelFacadeLocal.findAllByAccount(a1)).thenReturn(accessLevels1);

        doAnswer(invocationOnMock -> {
            accessLevels1.get(1).setActive(false);
            return null;
        }).when(accessLevelFacadeLocal).edit(any());


        AccessLevelExceptions accessLevelExceptions = assertThrows(AccessLevelExceptions.class, () -> accountManager.removeAccessLevel(a2.getLogin(), a1.getLogin(), "random"));
        assertFalse(accessLevels1.get(0).getActive());
        assertTrue(accessLevels1.get(1).getActive());

        accountManager.removeAccessLevel(a2.getLogin(), a1.getLogin(), levelClient);
        assertFalse(accessLevels1.get(0).getActive());
        assertTrue(accessLevels1.get(1).getActive());


        CommonExceptions commonExceptions = assertThrows(CommonExceptions.class, () -> accountManager.removeAccessLevel(a2.getLogin(), a1.getLogin(), levelAdmin));
        assertFalse(accessLevels1.get(0).getActive());
        assertFalse(accessLevels1.get(1).getActive());

        assertAll(
                () -> assertEquals(400, accessLevelExceptions.getResponse().getStatus()),
                () -> assertEquals(AccessLevelExceptions.ERROR_NO_ACCESS_LEVEL, accessLevelExceptions.getResponse().getEntity()),
                () -> assertEquals(410, commonExceptions.getResponse().getStatus())
        );
    }

    @Test
    void updateAccountTest() {
        Account account = createAccount();
        a1.setLogin("testLogin");
        when(accountFacadeLocal.findByLogin("ExampleLogin")).thenReturn(account);
        when(accountFacadeLocal.findAll()).thenReturn(Collections.singletonList(account));
        when(accountFacadeLocal.findByLogin("testLogin")).thenReturn(a1);

        assertEquals("48123456788", account.getPhoneNumber());
        assertEquals("Annabelle", account.getFirstName());
        assertEquals("Washington", account.getLastName());
        assertEquals("PL", account.getTimeZone());
        assertEquals("PL", account.getLanguage());

        Account updateAcc = new Account();
        updateAcc.setLogin("ExampleLogin");
        updateAcc.setPhoneNumber("123");
        updateAcc.setFirstName("Edward");
        updateAcc.setLastName("Piotrowski");
        updateAcc.setTimeZone("en-US");
        updateAcc.setLanguage("EN");

        doAnswer(invocation -> {
            account.setPhoneNumber("123");
            account.setFirstName("Edward");
            account.setLastName("Piotrowski");
            account.setTimeZone("en-US");
            account.setLanguage("EN");
            account.setModificationDate(Timestamp.from(Instant.now()));
            account.setModifiedBy(a1);
            return null;
        }).when(accountFacadeLocal).edit(any());

        accountManager.updateAccount(updateAcc, "testLogin");

        assertEquals("123", account.getPhoneNumber());
        assertEquals("Edward", account.getFirstName());
        assertEquals("Piotrowski", account.getLastName());
        assertEquals("en-US", account.getTimeZone());
        assertEquals("EN", account.getLanguage());
        assertTrue(account.getModificationDate().compareTo(Timestamp.from(Instant.now())) < 10000);
        assertEquals(a1, account.getModifiedBy());
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
        acc.setCreationDate(Timestamp.valueOf("2020-03-21 11:21:15"));
        acc.setLastKnownGoodLogin(Timestamp.valueOf("2020-03-25 11:21:15"));
        acc.setLastKnownGoodLoginIp("111.111.111.111");
        acc.setLastKnownBadLogin(Timestamp.valueOf("2020-03-26 11:21:15"));
        acc.setLastKnownBadLoginIp("222.222.222.222");
        acc.setNumberOfBadLogins(2);
        return acc;
    }

    @Test
    void changePasswordTest() {
        a1.setPassword(DigestUtils.sha512Hex(oldPassword));
        when(accountFacadeLocal.findByLogin(login1)).thenReturn(a1);

        doAnswer(invocationOnMock -> {
            Account account = invocationOnMock.getArgument(0);
            account.setPassword(DigestUtils.sha512Hex(newPassword));
            return null;
        }).when(accountFacadeLocal).edit(any());

        assertDoesNotThrow(() -> accountManager.changePassword(login1,  new Password(oldPassword),  new Password(newPassword)));
        verify(accountFacadeLocal).edit(a1);
        assertEquals(DigestUtils.sha512Hex(newPassword), a1.getPassword());
        assertEquals(DigestUtils.sha512Hex(newPassword), accounts.get(0).getPassword());
    }

    @Test
    void changePasswordExceptionTest() {
        a1.setPassword(DigestUtils.sha512Hex(oldPassword));
        when(accountFacadeLocal.findByLogin(login1)).thenReturn(a1);

        AccountExceptions accountExceptions1 = assertThrows(AccountExceptions.class, () -> accountManager.changePassword(login1, new Password(invalidPassword),  new Password(newPassword)));
        AccountExceptions accountExceptions2 = assertThrows(AccountExceptions.class, () -> accountManager.changePassword(login1, new Password(oldPassword),  new Password(oldPassword)));
        assertAll(
                () -> assertEquals(400, accountExceptions1.getResponse().getStatus()),
                () -> assertEquals(AccountExceptions.ERROR_PASSWORD_NOT_CORRECT, accountExceptions1.getResponse().getEntity()),
                () -> assertEquals(409, accountExceptions2.getResponse().getStatus()),
                () -> assertEquals(AccountExceptions.ERROR_SAME_PASSWORD, accountExceptions2.getResponse().getEntity())
        );
    }

    @Test
    void changeActivityTest() {
        a1.setLogin(login1);
        a1.setEmail(email1);
        a2.setLogin(login2);
        a2.setEmail(email3);
        a2.setActivityModifiedBy(a1);
        a3.setLogin(login4);
        when(accountFacadeLocal.findByLogin(login1)).thenReturn(a1);
        when(accountFacadeLocal.findByLogin(login2)).thenReturn(a2);
        when(accountFacadeLocal.findByLogin(login4)).thenReturn(a3);

        accountManager.changeActivity(login1, false, login2);
        verify(accountFacadeLocal).edit(a1);
        assertFalse(a1.getActive());
        assertEquals(a2, a1.getActivityModifiedBy());
        assertFalse(accounts.get(0).getActive());

        accountManager.changeActivity(login1, true, login4);
        verify(accountFacadeLocal, times(2)).edit(a1);
        assertTrue(a1.getActive());
        assertEquals(a3, a1.getActivityModifiedBy());
        assertTrue(accounts.get(0).getActive());


        a2.setNumberOfBadLogins(2);
        accountManager.changeActivity(login2, true, null);
        assertEquals(0, a2.getNumberOfBadLogins());
        assertNull(a2.getActivityModifiedBy());
    }

    @Test
    void registerBadLogin() {
        when(accountFacadeLocal.findByLogin(login1)).thenReturn(a1);
        a1.setLogin(login1);
        a1.setEmail(email1);
        String address = "192.168.1.1";

        assertNull(a1.getLastKnownBadLogin());
        assertNull(a1.getLastKnownBadLoginIp());
        assertEquals(0, a1.getNumberOfBadLogins());
        assertTrue(a1.getActive());

        Timestamp before = Timestamp.from(Instant.now());
        accountManager.registerBadLogin(login1, address);
        Timestamp after = Timestamp.from(Instant.now());

        assertTrue(a1.getLastKnownBadLogin().getTime() >= before.getTime());
        assertTrue(a1.getLastKnownBadLogin().getTime() <= after.getTime());
        assertEquals(address, a1.getLastKnownBadLoginIp());

        assertEquals(1, a1.getNumberOfBadLogins());
        assertTrue(a1.getActive());

        accountManager.registerBadLogin(login1, address);
        assertEquals(2, a1.getNumberOfBadLogins());
        assertTrue(a1.getActive());

        accountManager.registerBadLogin(login1, address);
        assertEquals(3, a1.getNumberOfBadLogins());
        assertFalse(a1.getActive());
    }

    @Test
    void confirmAccount() {
        a1.setActive(false);
        a1.setLogin(login1);

        OneTimeUrl oneTimeUrl = new OneTimeUrl();
        oneTimeUrl.setUrl(randomUrl);
        oneTimeUrl.setAccount(a1);
        oneTimeUrl.setActionType("verify");
        oneTimeUrl.setExpireDate(Timestamp.from(Instant.now().plus(24, HOURS)));

        when(oneTimeUrlFacadeLocal.findByUrl(randomUrl)).thenReturn(oneTimeUrl);
        when(accountFacadeLocal.findByLogin(login1)).thenReturn(a1);

        doAnswer(invocationOnMock -> {
            a1.setActive(true);
            return null;
        }).when(accountFacadeLocal).edit(any());

        assertDoesNotThrow(() -> accountManager.confirmAccount(randomUrl));
        assertTrue(a1.getActive());

        assertThrows(AccountExceptions.class, () -> accountManager.confirmAccount(null));

        assertThrows(CommonExceptions.class, () -> accountManager.confirmAccount("invalidUrl"));

        oneTimeUrl.setActionType("invalid");

        assertThrows(AccountExceptions.class, () -> accountManager.confirmAccount(randomUrl));

        oneTimeUrl.setActionType("verify");
        oneTimeUrl.setExpireDate(Timestamp.from(Instant.now().minus(1, HOURS)));

        assertThrows(AccountExceptions.class, () -> accountManager.confirmAccount(randomUrl));
    }

    @Test
    void changeEmailAddress() {
        a1.setEmail("stary@mail.com");
        a1.setLogin(login1);

        OneTimeUrl oneTimeUrl = new OneTimeUrl();
        oneTimeUrl.setUrl(randomUrl);
        oneTimeUrl.setAccount(a1);
        oneTimeUrl.setNewEmail("nowy@mail.com");
        oneTimeUrl.setExpireDate(Timestamp.from(Instant.now().plus(24, HOURS)));
        oneTimeUrl.setActionType("e-mail");

        when(oneTimeUrlFacadeLocal.findByUrl(randomUrl)).thenReturn(oneTimeUrl);
        when(accountFacadeLocal.findByLogin(login1)).thenReturn(a1);

        doAnswer(invocationOnMock -> {
            a1.setEmail("nowy@mail.com");
            a1.setModifiedBy(a1);
            return null;
        }).when(accountFacadeLocal).edit(any());

        assertThrows(CommonExceptions.class, () -> accountManager.changeEmailAddress("invalidUrl"));

        assertDoesNotThrow(() -> accountManager.changeEmailAddress(randomUrl));
        assertEquals("nowy@mail.com", a1.getEmail());

        oneTimeUrl.setExpireDate(Timestamp.from(Instant.now().minus(1, HOURS)));
        assertThrows(AccountExceptions.class, () -> accountManager.changeEmailAddress(randomUrl));

        oneTimeUrl.setExpireDate(Timestamp.from(Instant.now().plus(24, HOURS)));
        oneTimeUrl.setActionType("invalid");
        assertThrows(AccountExceptions.class, () -> accountManager.changeEmailAddress(randomUrl));
    }

    @Test
    void sendChangeEmailAddressUrl() {
        a1.setLogin(login1);
        a1.setEmail("stary@mail.com");
        accounts.add(a1);
        OneTimeUrl oneTimeUrl = new OneTimeUrl();
        oneTimeUrl.setUrl(randomUrl);
        oneTimeUrl.setAccount(a1);
        oneTimeUrl.setNewEmail("niepowtarzalny@mail.com");

        when(accountFacadeLocal.findByLogin(login1)).thenReturn(a1);
        when(accountFacadeLocal.findAll()).thenReturn(accounts);
        when(a1.getLogin()).thenReturn(login1);

        accountManager.sendChangeEmailAddressUrl(a1.getLogin(), "niepowtarzalny@mail.com", a1.getLogin());
        verify(oneTimeUrlFacadeLocal).create(urlCaptor.capture());
        OneTimeUrl url = urlCaptor.getValue();
        assertEquals(oneTimeUrl.getAccount(), url.getAccount());
        assertEquals("e-mail", url.getActionType());
        assertEquals(oneTimeUrl.getNewEmail(), url.getNewEmail());
        Timestamp oldExpireDate = url.getExpireDate();

        a1.setEmail("powtarzalny@mail.com");

        oneTimeUrl.setActionType("e-mail");
        when(oneTimeUrlFacadeLocal.findByAccount(a1)).thenReturn(Collections.singletonList(oneTimeUrl));
        accountManager.sendChangeEmailAddressUrl(a1.getLogin(), "niepowtarzalny@mail.com", a1.getLogin());
        verify(oneTimeUrlFacadeLocal).edit(urlCaptor.capture());
        url = urlCaptor.getValue();
        assertEquals(oneTimeUrl.getAccount(), url.getAccount());
        assertEquals("e-mail", url.getActionType());
        assertEquals(oneTimeUrl.getNewEmail(), url.getNewEmail());
        assertEquals(oneTimeUrl.getModifiedBy(), url.getModifiedBy());
        assertTrue(oldExpireDate.before(url.getExpireDate()));
        assertTrue(url.getCreationDate().before(url.getModificationDate()));
    }

    @Test
    void sendPasswordResetAddressUrl() {
        when(accountFacadeLocal.findByEmail(email3)).thenReturn(a3);
        when(accountFacadeLocal.findByLogin(login1)).thenReturn(a3);
        when(oneTimeUrlFacadeLocal.findByAccount(a3)).thenReturn(Collections.EMPTY_LIST);
        a3.setLanguage("pl");
        a3.setFirstName("Marek");

        Timestamp before = Timestamp.from(Instant.now().plus(20, MINUTES));
        accountManager.sendPasswordResetAddressUrl(email3, login1);
        Timestamp after = Timestamp.from(Instant.now().plus(20, MINUTES));

        verify(oneTimeUrlFacadeLocal).create(urlCaptor.capture());

        OneTimeUrl url = urlCaptor.getValue();

        assertEquals(32, url.getUrl().length());
        assertEquals("passwd", url.getActionType());
        assertEquals(a3, url.getAccount());
        assertNull(url.getNewEmail());
        assertTrue(url.getExpireDate().getTime() >= before.getTime());
        assertTrue(url.getExpireDate().getTime() <= after.getTime());

        verify(emailSender).sendPasswordResetEmail("pl", "Marek", email3, url.getUrl());

        OneTimeUrl oneTimeUrl = new OneTimeUrl();
        oneTimeUrl.setActionType("passwd");
        a3.setFirstName("Bartek");
        when(oneTimeUrlFacadeLocal.findByAccount(a3)).thenReturn(List.of(oneTimeUrl));

        before = Timestamp.from(Instant.now().plus(20, MINUTES));
        accountManager.sendPasswordResetAddressUrl(email3, login1);
        after = Timestamp.from(Instant.now().plus(20, MINUTES));

        assertTrue(oneTimeUrl.getExpireDate().getTime() >= before.getTime());
        assertTrue(oneTimeUrl.getExpireDate().getTime() <= after.getTime());

        verify(emailSender).sendPasswordResetEmail("pl", "Bartek", email3, oneTimeUrl.getUrl());

        verify(oneTimeUrlFacadeLocal).edit(urlCaptor.capture());
        OneTimeUrl url2 = urlCaptor.getValue();
        assertEquals(a3, url2.getModifiedBy());
        assertTrue(Timestamp.from(Instant.now().minus(10, SECONDS)).before(url2.getModificationDate()));
        assertTrue(url.getExpireDate().before(url2.getExpireDate()));
    }

    @Test
    void resetPassword() {
        OneTimeUrl oneTimeUrl = new OneTimeUrl();
        oneTimeUrl.setActionType("passwd");
        oneTimeUrl.setExpireDate(Timestamp.from(Instant.now().plus(10, MINUTES)));
        a3.setPassword("pass");
        a3.setModifiedBy(a1);
        oneTimeUrl.setAccount(a3);
        when(oneTimeUrlFacadeLocal.findByUrl("testUrl")).thenReturn(oneTimeUrl);

        Timestamp before = Timestamp.from(Instant.now());
        accountManager.resetPassword("testUrl", new Password("newPass"));
        Timestamp after = Timestamp.from(Instant.now());


        assertEquals(DigestUtils.sha512Hex("newPass"), a3.getPassword());
        assertTrue(a3.getPasswordModificationDate().getTime() >= before.getTime());
        assertTrue(a3.getPasswordModificationDate().getTime() <= after.getTime());
    }

    @Test
    void registerGoodLoginAndGetTimezone() {
        when(accountFacadeLocal.findByLogin(login1)).thenReturn(a1);
        a1.setTimeZone("zone");
        String address = "192.168.1.1";
        a1.setNumberOfBadLogins(2);

        assertNull(a1.getLastKnownGoodLogin());
        assertNull(a1.getLastKnownGoodLoginIp());

        Timestamp before = Timestamp.from(Instant.now());
        String zone = accountManager.registerGoodLoginAndGetTimezone(login1, Set.of("CLIENT"), address, "pl");
        Timestamp after = Timestamp.from(Instant.now());

        assertTrue(a1.getLastKnownGoodLogin().getTime() >= before.getTime());
        assertTrue(a1.getLastKnownGoodLogin().getTime() <= after.getTime());
        assertEquals(address, a1.getLastKnownGoodLoginIp());
        assertEquals("zone", zone);
        assertEquals("pl", a1.getLanguage());
        assertEquals(0, a1.getNumberOfBadLogins());
    }
}
