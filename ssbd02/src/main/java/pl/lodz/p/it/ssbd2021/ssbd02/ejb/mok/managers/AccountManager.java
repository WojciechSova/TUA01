package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers;

import org.apache.commons.codec.digest.DigestUtils;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccessLevelFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccountFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces.AccountManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mail.EmailSender;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
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
        for (Account account : accountList) {
            accountLevelsMap.put(account, accessLevelFacadeLocal.findAllByAccount(account));
        }
        return accountLevelsMap;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createAccount(Account account) throws WebApplicationException {
        List<Account> allAccounts = accountFacadeLocal.findAll();
        if (allAccounts.stream()
                .anyMatch(x -> account.getLogin().equals(x.getLogin()))) {
            throw new WebApplicationException("Such login exists", 409);
        } else if (allAccounts.stream()
                .anyMatch(x -> account.getEmail().equals(x.getEmail()))) {
            throw new WebApplicationException("Such email exists", 409);
        } else if (account.getPhoneNumber() != null &&
                allAccounts.stream()
                .filter(x -> x.getPhoneNumber() != null)
                .anyMatch(x -> account.getPhoneNumber().equals(x.getPhoneNumber()))) {
            throw new WebApplicationException("Such phone number exists", 409);
        }

        account.setPassword(DigestUtils.sha512Hex(account.getPassword()));
        AccessLevel accessLevel = new AccessLevel();
        accessLevel.setLevel("CLIENT");
        accessLevel.setAccount(account);
        accountFacadeLocal.create(account);
        accessLevelFacadeLocal.create(accessLevel);

        EmailSender.sendRegistrationEmail(account.getFirstName(), account.getEmail(), "link");
    }

    //TODO: method that will handle account confirmation
}
