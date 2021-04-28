package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers;

import org.apache.commons.lang3.tuple.Pair;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccessLevelFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccountFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces.AccountManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manager kont
 *
 * @author Daniel Łondka
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AccountManager implements AccountManagerLocal {

    @Inject
    private AccountFacadeLocal accountFacadeLocal;

    @Inject
    private AccessLevelFacadeLocal accessLevelFacadeLocal;

    @Override
    public Map<Account, List<AccessLevel>> getAllAccountsWithAccessLevels() {
        Map<Account, List<AccessLevel>> accountLevelsMap = new HashMap<>();
        List<Account> accountList = accountFacadeLocal.findAll();
        for (Account account: accountList) {
            accountLevelsMap.put(account, accessLevelFacadeLocal.findAllByAccount(account));
        }
        return accountLevelsMap;
    }

    @Override
    public Pair<Account, List<AccessLevel>> getAccountWithLogin(String login) {
        Account account = accountFacadeLocal.findByLogin(login);
        List<AccessLevel> accessLevels = accessLevelFacadeLocal.findAllByAccount(account);
        return Pair.of(account, accessLevels);
    }
}
