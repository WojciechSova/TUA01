package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccessLevelFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccountFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;

import java.util.*;

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
    private final String login1 = "a1Login";
    private final String login2 = "a2Login";
    private final AccessLevel al1 = new AccessLevel();
    private final AccessLevel al2 = new AccessLevel();
    private final AccessLevel al3 = new AccessLevel();
    private final List<AccessLevel> accessLevels1 = new ArrayList<>();
    private final List<AccessLevel> accessLevels2 = new ArrayList<>();
    private final Map<Account, List<AccessLevel>> accountListMap = new HashMap<>();
    private final List<Account> accounts = new ArrayList<>();


    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
        accessLevels1.add(al1);
        accessLevels1.add(al2);
        accessLevels2.add(al3);
        accounts.add(a1);

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
            accounts.add(a2);
            return null;
        }).when(accountFacadeLocal).create(a2);

        assertEquals(1, accounts.size());

        accountManager.createAccount(a2);

        assertEquals(2, accounts.size());
        assertEquals(a2.hashCode(), accounts.get(1).hashCode());
    }
}
