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
    public void updateAccount(Account account) throws WebApplicationException {
        List<Account> allAccounts = accountFacadeLocal.findAll();
        if (account.getPhoneNumber() != null) {
            if (account.getPhoneNumber().isEmpty()) {
                account.setPhoneNumber(null);
            } else if (allAccounts.stream()
                    .filter(x -> !account.getLogin().equals(x.getLogin()))
                    .anyMatch(x -> account.getPhoneNumber().equals(x.getPhoneNumber()))) {
                throw new WebApplicationException("Such phone number exists", 409);
            }
        }

        Account accountFromDB = accountFacadeLocal.findByLogin(account.getLogin());

        Account acc = new Account();
        acc.setVersion(account.getVersion());
        acc.setId(accountFromDB.getId());
        acc.setLogin(accountFromDB.getLogin());
        acc.setPassword(accountFromDB.getPassword());
        acc.setActive(accountFromDB.getActive());
        acc.setConfirmed(accountFromDB.getConfirmed());
        acc.setFirstName(accountFromDB.getFirstName());
        acc.setLastName(accountFromDB.getLastName());
        acc.setEmail(accountFromDB.getEmail());
        acc.setPhoneNumber(accountFromDB.getPhoneNumber());
        acc.setLanguage(accountFromDB.getLanguage());
        acc.setTimeZone(accountFromDB.getTimeZone());
        acc.setModificationDate(accountFromDB.getModificationDate());
        acc.setModifiedBy(accountFromDB.getModifiedBy());
        acc.setCreationDate(accountFromDB.getCreationDate());
        acc.setLastKnownGoodLogin(accountFromDB.getLastKnownGoodLogin());
        acc.setLastKnownGoodLoginIp(accountFromDB.getLastKnownGoodLoginIp());
        acc.setLastKnownBadLogin(accountFromDB.getLastKnownBadLogin());
        acc.setLastKnownBadLoginIp(accountFromDB.getLastKnownBadLoginIp());
        acc.setNumberOfBadLogins(accountFromDB.getNumberOfBadLogins());

        if (account.getPhoneNumber() != null) {
            acc.setPhoneNumber(account.getPhoneNumber());
        }
        if (account.getFirstName() != null && !account.getFirstName().isEmpty()) {
            acc.setFirstName(account.getFirstName());
        }
        if (account.getLastName() != null && !account.getLastName().isEmpty()) {
            acc.setLastName(account.getLastName());
        }
        if (account.getTimeZone() != null && !account.getTimeZone().isEmpty()) {
            acc.setTimeZone(account.getTimeZone());
        }
        if (account.getLanguage() != null && !account.getLanguage().isEmpty()) {
            acc.setLanguage(account.getLanguage());
        }

        accountFacadeLocal.edit(acc);

        EmailSender.sendModificationEmail(account.getFirstName(), accountFromDB.getEmail());
    }
}
