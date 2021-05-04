package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.tuple.Pair;
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
import java.util.List;
import java.util.stream.Collectors;

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
    public List<Pair<Account, List<AccessLevel>>> getAllAccountsWithAccessLevels() {
        return accountFacadeLocal.findAll().stream()
                .map(account -> Pair.of(account, accessLevelFacadeLocal.findAllByAccount(account)))
                .collect(Collectors.toList());
    }

    @Override
    public Pair<Account, List<AccessLevel>> getAccountWithLogin(String login) {
        Account account = accountFacadeLocal.findByLogin(login);
        List<AccessLevel> accessLevels = accessLevelFacadeLocal.findAllByAccount(account);
        return Pair.of(account, accessLevels);
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
        } else if (account.getPhoneNumber() != null) {
            if (account.getPhoneNumber().isEmpty()) {
                account.setPhoneNumber(null);
            } else if (allAccounts.stream()
                    .filter(x -> x.getPhoneNumber() != null)
                    .anyMatch(x -> account.getPhoneNumber().equals(x.getPhoneNumber()))) {
                throw new WebApplicationException("Such phone number exists", 409);
            }
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

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addAccessLevel(String login, String accessLevel) {
        Account account = accountFacadeLocal.findByLogin(login);
        List<AccessLevel> accessLevels = accessLevelFacadeLocal.findAllByAccount(account);

        if (!List.of("ADMIN", "EMPLOYEE", "CLIENT").contains(accessLevel)) {
            return;
        }

        if (accessLevels.stream().anyMatch(x -> x.getLevel().equals(accessLevel))) {
            return;
        }

        AccessLevel newAccessLevel = new AccessLevel();
        newAccessLevel.setLevel(accessLevel);
        newAccessLevel.setAccount(account);
        accessLevelFacadeLocal.create(newAccessLevel);

        EmailSender.sendModificationEmail(account.getFirstName(), account.getEmail());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removeAccessLevel(String login, String accessLevel) {
        Account account = accountFacadeLocal.findByLogin(login);
        List<AccessLevel> accessLevels = accessLevelFacadeLocal.findAllByAccount(account);

        List<AccessLevel> removeAccessLevels = accessLevels.stream()
                .filter(x -> x.getLevel().equals(accessLevel)).collect(Collectors.toList());

        if (removeAccessLevels.isEmpty()) {
            return;
        }

        removeAccessLevels.forEach(x -> accessLevelFacadeLocal.remove(x));

        EmailSender.sendModificationEmail(account.getFirstName(), account.getEmail());
    }
}
