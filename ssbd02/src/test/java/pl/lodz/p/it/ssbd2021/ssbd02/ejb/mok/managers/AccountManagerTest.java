package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccessLevelFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccountFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;

import javax.ws.rs.WebApplicationException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountManagerTest {

    @Mock
    private AccountFacadeLocal accountFacadeLocal;
    @Mock
    private AccessLevelFacadeLocal accessLevelFacadeLocal;
    @InjectMocks
    private AccountManager accountManager;

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
    private final AccessLevel al1 = new AccessLevel();
    private final AccessLevel al2 = new AccessLevel();
    private final AccessLevel al3 = new AccessLevel();
    private final AccessLevel al5 = new AccessLevel();
    @Spy
    private final List<AccessLevel> accessLevels1 = new ArrayList<>();
    private final List<AccessLevel> accessLevels2 = new ArrayList<>();
    private final List<Pair<Account, List<AccessLevel>>> pairList = new ArrayList<>();
    private List<Account> accounts;


    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);

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
    void getAllAccountsWithAccessLevelsTest() {
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
    void createAccountTest() {
        doAnswer(invocationOnMock -> {
            accounts.add(a3);
            return null;
        }).when(accountFacadeLocal).create(a3);

        doAnswer(invocationOnMock -> {
            accessLevels1.add(al4);
            return null;
        }).when(accessLevelFacadeLocal).create(any());

        when(al4.getAccount()).thenReturn(a3);
        when(al4.getLevel()).thenReturn(levelClient);
        when(a3.getEmail()).thenReturn(email3);
        when(a3.getPassword()).thenReturn(password3);
        when(a3.getPhoneNumber()).thenReturn(phoneNumber3);

        assertEquals(2, accounts.size());
        assertEquals(2, accessLevels1.size());

        accountManager.createAccount(a3);

        assertEquals(3, accounts.size());
        assertEquals(3, accessLevels1.size());
        assertEquals(a3.hashCode(), accounts.get(2).hashCode());
        assertEquals(al4.getAccount(), accessLevels1.get(2).getAccount());
        assertEquals(al4.getLevel(), accessLevels1.get(2).getLevel());
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
        WebApplicationException exceptionA2 = assertThrows(WebApplicationException.class, () -> accountManager.createAccount(a2));
        WebApplicationException exceptionA3 = assertThrows(WebApplicationException.class, () -> accountManager.createAccount(a3));
        WebApplicationException exceptionA4 = assertThrows(WebApplicationException.class, () -> accountManager.createAccount(a4));

        assertEquals(409, exceptionA2.getResponse().getStatus());
        assertEquals("Such email exists", exceptionA2.getMessage());

        assertEquals(409, exceptionA3.getResponse().getStatus());
        assertEquals("Such login exists", exceptionA3.getMessage());

        assertEquals(409, exceptionA4.getResponse().getStatus());
        assertEquals("Such phone number exists", exceptionA4.getMessage());
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
    void addAccessLevel() {
        AccessLevel employeeAccessLevel = new AccessLevel();
        employeeAccessLevel.setAccount(a1);
        employeeAccessLevel.setLevel(levelEmployee);

        a1.setLogin(login1);
        a1.setEmail(email1);

        al1.setAccount(a1);
        al1.setLevel(levelClient);
        al1.setActive(true);

        al2.setAccount(a1);
        al2.setLevel(levelAdmin);
        al2.setActive(false);

        when(accountFacadeLocal.findByLogin(login1)).thenReturn(a1);
        when(accessLevelFacadeLocal.findAllByAccount(a1)).thenReturn(accessLevels1);

        doAnswer(invocationOnMock -> {
            accessLevels1.get(1).setActive(true);
            return null;
        }).when(accessLevelFacadeLocal).edit(any());

        doAnswer(invocationOnMock -> {
            accessLevels1.add(employeeAccessLevel);
            return null;
        }).when(accessLevelFacadeLocal).create(any());

        accountManager.addAccessLevel(a2.getLogin(), a1.getLogin(), "random");
        assertTrue(accessLevels1.get(0).getActive());
        assertFalse(accessLevels1.get(1).getActive());

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

        accountManager.removeAccessLevel(a2.getLogin(), a1.getLogin(), "random");
        assertFalse(accessLevels1.get(0).getActive());
        assertTrue(accessLevels1.get(1).getActive());

        accountManager.removeAccessLevel(a2.getLogin(), a1.getLogin(), levelClient);
        assertFalse(accessLevels1.get(0).getActive());
        assertTrue(accessLevels1.get(1).getActive());

        accountManager.removeAccessLevel(a2.getLogin(), a1.getLogin(), levelAdmin);
        assertFalse(accessLevels1.get(0).getActive());
        assertFalse(accessLevels1.get(1).getActive());
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
    void updateAccountExceptionTest() {
        Account account = createAccount();

        when(a1.getLogin()).thenReturn("Test");
        when(a1.getPhoneNumber()).thenReturn("48123456788");

        when(accountFacadeLocal.findByLogin(anyString())).thenReturn(account);
        when(accountFacadeLocal.findAll()).thenReturn(Arrays.asList(account, a1));
        WebApplicationException exceptionA1 = assertThrows(WebApplicationException.class,
                () -> accountManager.updateAccount(a1, "test"));

        assertEquals(409, exceptionA1.getResponse().getStatus());
        assertEquals("Such phone number exists", exceptionA1.getMessage());
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

        assertDoesNotThrow(() -> accountManager.changePassword(login1, oldPassword, newPassword));
        verify(accountFacadeLocal).edit(a1);
        assertEquals(DigestUtils.sha512Hex(newPassword), a1.getPassword());
        assertEquals(DigestUtils.sha512Hex(newPassword), accounts.get(0).getPassword());
    }

    @Test
    void changePasswordExceptionTest() {
        a1.setPassword(DigestUtils.sha512Hex(oldPassword));
        when(accountFacadeLocal.findByLogin(login1)).thenReturn(a1);

        try {
            accountManager.changePassword(login1, invalidPassword, newPassword);
        } catch (WebApplicationException ex) {
            assertEquals("The provided password is invalid", ex.getMessage());
            assertEquals(406, ex.getResponse().getStatus());
        }

        try {
            accountManager.changePassword(login1, oldPassword, oldPassword);
        } catch (WebApplicationException ex) {
            assertEquals("The new password is the same as the old password", ex.getMessage());
            assertEquals(409, ex.getResponse().getStatus());
        }
    }

    @Test
    void changeActivityTest(){
        a1.setLogin(login1);
        a1.setEmail(email1);
        a2.setLogin(login2);
        a2.setEmail(email3);
        a2.setModifiedBy(a1);
        a3.setLogin(login4);
        when(accountFacadeLocal.findByLogin(login1)).thenReturn(a1);
        when(accountFacadeLocal.findByLogin(login2)).thenReturn(a2);
        when(accountFacadeLocal.findByLogin(login4)).thenReturn(a3);

        accountManager.changeActivity(login1, false, login2);
        verify(accountFacadeLocal).edit(a1);
        assertFalse(a1.getActive());
        assertEquals(a2, a1.getModifiedBy());
        assertFalse(accounts.get(0).getActive());

        accountManager.changeActivity(login1, true, login4);
        verify(accountFacadeLocal, times(2)).edit(a1);
        assertTrue(a1.getActive());
        assertEquals(a3, a1.getModifiedBy());
        assertTrue(accounts.get(0).getActive());


        a2.setNumberOfBadLogins(2);
        accountManager.changeActivity(login2, true, null);
        assertEquals(0, a2.getNumberOfBadLogins());
        assertNull(a2.getModifiedBy());
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
    void registerGoodLogin() {
        when(accountFacadeLocal.findByLogin(login1)).thenReturn(a1);
        String address = "192.168.1.1";
        a1.setNumberOfBadLogins(2);

        assertNull(a1.getLastKnownGoodLogin());
        assertNull(a1.getLastKnownGoodLoginIp());

        Timestamp before = Timestamp.from(Instant.now());
        accountManager.registerGoodLogin(login1, address);
        Timestamp after = Timestamp.from(Instant.now());

        assertTrue(a1.getLastKnownGoodLogin().getTime() >= before.getTime());
        assertTrue(a1.getLastKnownGoodLogin().getTime() <= after.getTime());
        assertEquals(address, a1.getLastKnownGoodLoginIp());
        assertEquals(0, a1.getNumberOfBadLogins());
    }
}
