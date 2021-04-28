package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
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
    private final List<Pair<Account, List<AccessLevel>>> pairList = new ArrayList<>();


    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
        accessLevels1.add(al1);
        accessLevels1.add(al2);
        accessLevels2.add(al3);

        pairList.add(new ImmutablePair<>(a1, accessLevels1));
        pairList.add(new ImmutablePair<>(a2, accessLevels2));
    }

    @Test
    void getAllAccountsWithAccessLevelsTest() {
        Mockito.when(accountFacadeLocal.findAll()).thenReturn(Arrays.asList(a1, a2));
        Mockito.when(accessLevelFacadeLocal.findAllByAccount(a1)).thenReturn(accessLevels1);
        Mockito.when(accessLevelFacadeLocal.findAllByAccount(a2)).thenReturn(accessLevels2);

        List<Pair<Account, List<AccessLevel>>> testedPairList = accountManager.getAllAccountsWithAccessLevels();

        Assertions.assertEquals(pairList, testedPairList);
        Assertions.assertEquals(2, testedPairList.size());
        Assertions.assertEquals(2, testedPairList.get(0).getValue().size());
        Assertions.assertEquals(1, testedPairList.get(1).getValue().size());
        Assertions.assertEquals(accessLevels1, testedPairList.get(0).getValue());
        Assertions.assertEquals(accessLevels2, testedPairList.get(1).getValue());
        Assertions.assertEquals(a1, testedPairList.get(0).getKey());
        Assertions.assertEquals(a2, testedPairList.get(1).getKey());
    }

    @Test
    void getAccountWithLogin() {
        Mockito.when(accountFacadeLocal.findByLogin(login1)).thenReturn(a1);
        Mockito.when(accessLevelFacadeLocal.findAllByAccount(a1)).thenReturn(accessLevels1);
        Assertions.assertEquals(Pair.of(a1, accessLevels1), accountManager.getAccountWithLogin(login1));
        Assertions.assertEquals(a1, accountManager.getAccountWithLogin(login1).getLeft());
        Assertions.assertEquals(a1, accountManager.getAccountWithLogin(login1).getKey());
        Assertions.assertEquals(accessLevels1, accountManager.getAccountWithLogin(login1).getRight());
        Assertions.assertEquals(accessLevels1, accountManager.getAccountWithLogin(login1).getValue());
    }
}
