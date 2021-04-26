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


    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
        accessLevels1.add(al1);
        accessLevels1.add(al2);
        accessLevels2.add(al3);

        accountListMap.put(a1, accessLevels1);
        accountListMap.put(a2, accessLevels2);
    }

    @Test
    void getAllAccountsWithAccessLevelsTest() {
        Mockito.when(accountFacadeLocal.findAll()).thenReturn(Arrays.asList(a1, a2));
        Mockito.when(a1.getLogin()).thenReturn(login1);
        Mockito.when(a2.getLogin()).thenReturn(login2);
        Mockito.when(accessLevelFacadeLocal.findByLogin(login1)).thenReturn(accessLevels1);
        Mockito.when(accessLevelFacadeLocal.findByLogin(login2)).thenReturn(accessLevels2);

        Map<Account, List<AccessLevel>> testedMap = accountManager.getAllAccountsWithAccessLevels();

        Assertions.assertEquals(accountListMap, testedMap);
        Assertions.assertEquals(2, testedMap.size());
        Assertions.assertEquals(2, testedMap.get(a1).size());
        Assertions.assertEquals(1, testedMap.get(a2).size());
        Assertions.assertEquals(accessLevels1, testedMap.get(a1));
        Assertions.assertEquals(accessLevels2, testedMap.get(a2));
    }
}
