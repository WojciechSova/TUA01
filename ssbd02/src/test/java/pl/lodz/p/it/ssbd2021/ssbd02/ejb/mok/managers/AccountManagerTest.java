package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccessLevelFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccountFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.ClientData;

import javax.ws.rs.WebApplicationException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    private final ClientData cd3 = new ClientData();
    private final String login1 = "a1Login";
    private final String email1 = "a1Email@domain.com";
    private final String login2 = "a2Login";
    private final String email2 = "a2Email@domain.com";
    private final String login3 = "a3Login";
    private final String email3 = "a3Email@domain.com";
    private final String level = "CLIENT";
    private final AccessLevel al1 = new AccessLevel();
    private final AccessLevel al2 = new AccessLevel();
    private final AccessLevel al3 = new AccessLevel();
    private final List<AccessLevel> accessLevels1 = new ArrayList<>();
    private final List<AccessLevel> accessLevels2 = new ArrayList<>();
    private Map<Account, List<AccessLevel>> accountListMap;
    private List<Account> accounts;


    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
        accessLevels1.add(al1);
        accessLevels1.add(al2);
        accessLevels2.add(al3);

        accounts = new ArrayList<>();
        accounts.add(a1);
        accounts.add(a2);

        accountListMap = new HashMap<>();
        accountListMap.put(a1, accessLevels1);
        accountListMap.put(a2, accessLevels2);
    }

    @Test
    void getAllAccountsWithAccessLevelsTest() {
        when(accountFacadeLocal.findAll()).thenReturn(Arrays.asList(a1, a2));
        when(a1.getLogin()).thenReturn(login1);
        when(a2.getLogin()).thenReturn(login2);
        when(accessLevelFacadeLocal.findAllByAccount(a1)).thenReturn(accessLevels1);
        when(accessLevelFacadeLocal.findAllByAccount(a2)).thenReturn(accessLevels2);

        Map<Account, List<AccessLevel>> testedMap = accountManager.getAllAccountsWithAccessLevels();

        assertEquals(accountListMap, testedMap);
        assertEquals(2, testedMap.size());
        assertEquals(2, testedMap.get(a1).size());
        assertEquals(1, testedMap.get(a2).size());
        assertEquals(accessLevels1, testedMap.get(a1));
        assertEquals(accessLevels2, testedMap.get(a2));
    }

    @Test
    void createAccountTest() {
        doAnswer(invocationOnMock -> {
            accounts.add(a3);
            return null;
        }).when(accountFacadeLocal).create(a3);

        doAnswer(invocationOnMock -> {
            accountListMap.put(a3, List.of(cd3));
            return null;
        }).when(accessLevelFacadeLocal).create(any());

        when(cd3.getAccount()).thenReturn(a3);
        when(cd3.getLevel()).thenReturn(level);
        when(a3.getEmail()).thenReturn(email3);

        assertEquals(2, accounts.size());
        assertEquals(2, accountListMap.size());

        accountManager.createAccount(a3);

        assertEquals(3, accounts.size());
        assertEquals(3, accountListMap.size());
        assertEquals(a3.hashCode(), accounts.get(2).hashCode());
        assertEquals(cd3.getAccount(), accountListMap.get(a3).get(0).getAccount());
        assertEquals(cd3.getLevel(), accountListMap.get(a3).get(0).getLevel());
    }

    @Test
    void createAccountExceptionTest() {
        when(a1.getEmail()).thenReturn(email1);
        when(a1.getLogin()).thenReturn(login1);

        when(a2.getEmail()).thenReturn(email1);
        when(a2.getLogin()).thenReturn(login2);

        when(a3.getEmail()).thenReturn(email3);
        when(a3.getLogin()).thenReturn(login1);

        when(accountFacadeLocal.findAll()).thenReturn(List.of(a1));
        WebApplicationException exceptionA2 = assertThrows(WebApplicationException.class, () -> accountManager.createAccount(a2));
        WebApplicationException exceptionA3 = assertThrows(WebApplicationException.class, () -> accountManager.createAccount(a3));

        assertEquals(409, exceptionA2.getResponse().getStatus());
        assertEquals("Such email exists", exceptionA2.getMessage());

        assertEquals(409, exceptionA3.getResponse().getStatus());
        assertEquals("Such login exists", exceptionA3.getMessage());
    }
}
