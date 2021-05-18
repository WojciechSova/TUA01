package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.tuple.Pair;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccessLevelFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccountFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.OneTimeUrlFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces.AccountManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.utils.interfaces.EmailSenderLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.OneTimeUrl;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.SECONDS;

/**
 * Manager kont
 *
 * @author Daniel Łondka
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AccountManager implements AccountManagerLocal {

    private static final Properties prop = new Properties();
    @Inject
    private AccountFacadeLocal accountFacadeLocal;
    @Inject
    private AccessLevelFacadeLocal accessLevelFacadeLocal;
    @Inject
    private EmailSenderLocal emailSender;
    @Inject
    private OneTimeUrlFacadeLocal oneTimeUrlFacadeLocal;
    private long expirationTime;

    @Override
    public List<Pair<Account, List<AccessLevel>>> getAllAccountsWithActiveAccessLevels() {
        return accountFacadeLocal.findAll().stream()
                .map(account -> Pair.of(account, accessLevelFacadeLocal.findAllActiveByAccount(account)))
                .collect(Collectors.toList());
    }

    @Override
    public Pair<Account, List<AccessLevel>> getAccountWithLogin(String login) {
        Account account = accountFacadeLocal.findByLogin(login);
        List<AccessLevel> accessLevels = accessLevelFacadeLocal.findAllByAccount(account);
        return Pair.of(account, accessLevels);
    }

    @Override
    public Pair<Account, List<AccessLevel>> getAccountWithActiveAccessLevels(String login) {
        Account account = accountFacadeLocal.findByLogin(login);
        List<AccessLevel> accessLevels = accessLevelFacadeLocal.findAllActiveByAccount(account);
        return Pair.of(account, accessLevels);
    }

    @Override
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

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("system.properties")) {
            prop.load(input);
            expirationTime = Long.parseLong(prop.getProperty("system.time.account.confirmation"));
        } catch (IOException e) {
            expirationTime = 86400;
            e.printStackTrace();
        }

        OneTimeUrl oneTimeUrl = new OneTimeUrl();
        oneTimeUrl.setUrl(RandomStringUtils.randomAlphanumeric(32));
        oneTimeUrl.setAccount(account);
        oneTimeUrl.setActionType("verify");
        oneTimeUrl.setExpireDate(Timestamp.from(Instant.now().plus(expirationTime, SECONDS)));

        accountFacadeLocal.create(account);
        accessLevelFacadeLocal.create(accessLevel);
        oneTimeUrlFacadeLocal.create(oneTimeUrl);

        emailSender.sendRegistrationEmail(account.getLanguage(), account.getFirstName(), account.getEmail(), oneTimeUrl.getUrl());
    }

    @Override
    public void registerBadLogin(String login, String clientAddress) {
        Account account = accountFacadeLocal.findByLogin(login);
        account.setLastKnownBadLogin(Timestamp.from(Instant.now()));
        account.setLastKnownBadLoginIp(clientAddress);
        int badLogins = account.getNumberOfBadLogins() + 1;
        int badLoginsThreshold = 3;

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("system.properties")) {
            prop.load(input);
            badLoginsThreshold = Integer.parseInt(prop.getProperty("system.login.incorrect.threshold"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        account.setNumberOfBadLogins(badLogins);

        if (badLogins >= badLoginsThreshold) {
            changeActivity(account.getLogin(), false, null);
        }

    }

    @Override
    public void registerGoodLogin(String login, String clientAddress) {
        Account account = accountFacadeLocal.findByLogin(login);
        account.setLastKnownGoodLogin(Timestamp.from(Instant.now()));
        account.setLastKnownGoodLoginIp(clientAddress);
        account.setNumberOfBadLogins(0);
    }

    @Override
    public void updateLanguage(String login, String language) {
        Account account = accountFacadeLocal.findByLogin(login);
        account.setLanguage(language);
    }

    @Override
    public void updateAccount(Account account, String modifiedBy) throws WebApplicationException {
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
        Account acc = SerializationUtils.clone(accountFromDB);

        acc.setVersion(account.getVersion());

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
        acc.setModificationDate(Timestamp.from(Instant.now()));

        Account accModifiedBy = accountFacadeLocal.findByLogin(modifiedBy);
        acc.setModifiedBy(accModifiedBy);

        accountFacadeLocal.edit(acc);

        emailSender.sendModificationEmail(acc.getLanguage(), account.getFirstName(), accountFromDB.getEmail());
    }

    @Override
    public void addAccessLevel(String login, String targetLogin, String accessLevel) {
        if (!List.of("ADMIN", "EMPLOYEE", "CLIENT").contains(accessLevel)) {
            return;
        }

        Account account = accountFacadeLocal.findByLogin(targetLogin);
        List<AccessLevel> accessLevels = accessLevelFacadeLocal.findAllByAccount(account);

        if (accessLevels.stream().noneMatch(x -> x.getLevel().equals(accessLevel))) {
            AccessLevel newAccessLevel = new AccessLevel();
            newAccessLevel.setAccount(account);
            newAccessLevel.setLevel(accessLevel);
            newAccessLevel.setActive(true);
            newAccessLevel.setCreatedBy(accountFacadeLocal.findByLogin(login));
            accessLevelFacadeLocal.create(newAccessLevel);
            emailSender.sendAddAccessLevelEmail(account.getLanguage(), account.getFirstName(), account.getEmail(), accessLevel);
            return;
        }

        accessLevels.forEach(x -> {
            if (x.getLevel().equals(accessLevel) && !x.getActive()) {
                x.setActive(true);
                x.setModifiedBy(accountFacadeLocal.findByLogin(login));
                x.setModificationDate(Timestamp.from(Instant.now()));
                accessLevelFacadeLocal.edit(x);
                emailSender.sendAddAccessLevelEmail(account.getLanguage(), account.getFirstName(), account.getEmail(), accessLevel);
            }
        });
    }

    @Override
    public void removeAccessLevel(String login, String targetLogin, String accessLevel) {
        if (!List.of("ADMIN", "EMPLOYEE", "CLIENT").contains(accessLevel)) {
            return;
        }

        Account account = accountFacadeLocal.findByLogin(targetLogin);
        List<AccessLevel> accessLevels = accessLevelFacadeLocal.findAllByAccount(account);

        if (accessLevels.stream().noneMatch(x -> x.getLevel().equals(accessLevel))) {
            return;
        }

        accessLevels.forEach(x -> {
            if (x.getLevel().equals(accessLevel) && x.getActive()) {
                x.setActive(false);
                x.setModifiedBy(accountFacadeLocal.findByLogin(login));
                x.setModificationDate(Timestamp.from(Instant.now()));
                accessLevelFacadeLocal.edit(x);
                emailSender.sendRemoveAccessLevelEmail(account.getLanguage(), account.getFirstName(), account.getEmail(), accessLevel);
            }
        });
    }

    public void changePassword(String login, String oldPassword, String newPassword) throws WebApplicationException {
        Account account = accountFacadeLocal.findByLogin(login);
        String hashedOldPassword = DigestUtils.sha512Hex(oldPassword);
        if (!hashedOldPassword.equals(account.getPassword())) {
            throw new WebApplicationException("The provided password is invalid", 406);
        } else if (newPassword.equals(oldPassword)) {
            throw new WebApplicationException("The new password is the same as the old password", 409);
        }

        account.setPassword(DigestUtils.sha512Hex(newPassword));
        accountFacadeLocal.edit(account);
    }

    @Override
    public void changeActivity(String login, boolean newActivity, String modifiedBy) {
        Account account = accountFacadeLocal.findByLogin(login);
        account.setActive(newActivity);
        if (modifiedBy == null) {
            account.setModifiedBy(null);
        } else {
            account.setModifiedBy(accountFacadeLocal.findByLogin(modifiedBy));
        }
        if (newActivity) {
            account.setNumberOfBadLogins(0);
        }
        account.setModificationDate(new Timestamp(new Date().getTime()));
        accountFacadeLocal.edit(account);

        emailSender.sendChangedActivityEmail(account.getLanguage(), account.getFirstName(), account.getEmail(), account.getActive());
    }

    @Override
    public void notifyAdminAboutLogin(String login, String clientAddress) {
        Account account = accountFacadeLocal.findByLogin(login);

        emailSender.sendAdminAuthenticationEmail(account.getLanguage(), account.getFirstName(), account.getEmail(), clientAddress);
    }

    @Override
    public boolean confirmAccount(String url) {
        if (url == null) {
            return false;
        }

        OneTimeUrl oneTimeUrl = oneTimeUrlFacadeLocal.findByUrl(url);

        if (oneTimeUrl == null
                || !oneTimeUrl.getActionType().equals("verify")
                || Instant.now().isAfter(oneTimeUrl.getExpireDate().toInstant())) {
            return false;
        }

        if (url.equals(oneTimeUrl.getUrl())) {
            Account account = accountFacadeLocal.findByLogin(oneTimeUrl.getAccount().getLogin());
            account.setConfirmed(true);
            accountFacadeLocal.edit(account);
            oneTimeUrlFacadeLocal.remove(oneTimeUrl);
            return true;
        }

        return false;
    }

    @Override
    public boolean changeEmailAddress(String url) {
        if (url == null) {
            return false;
        }

        OneTimeUrl oneTimeUrl = oneTimeUrlFacadeLocal.findByUrl(url);

        if (oneTimeUrl == null) {
            return false;
        }

        if (!oneTimeUrl.getActionType().equals("e-mail") || Instant.now().isAfter(oneTimeUrl.getExpireDate().toInstant())) {
            return false;
        }

        if (url.equals(oneTimeUrl.getUrl())) {
            Account account = accountFacadeLocal.findByLogin(oneTimeUrl.getAccount().getLogin());
            account.setEmail(oneTimeUrl.getNewEmail());
            account.setModifiedBy(null);
            account.setModificationDate(new Timestamp(new Date().getTime()));
            accountFacadeLocal.edit(account);
            oneTimeUrlFacadeLocal.remove(oneTimeUrl);
            return true;
        }

        return false;
    }

    @Override
    public void sendChangeEmailAddressUrl(String login, String newEmailAddress) {
        Account account = accountFacadeLocal.findByLogin(login);

        if (accountFacadeLocal.findAll().stream().anyMatch(a -> newEmailAddress.equals(a.getEmail()))) {
            throw new WebApplicationException("Provided email already exists in the database", 409);
        }

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("system.properties")) {
            prop.load(input);
            expirationTime = Long.parseLong(prop.getProperty("system.time.account.confirmation"));
        } catch (IOException e) {
            expirationTime = 86400;
            e.printStackTrace();
        }

        OneTimeUrl oneTimeUrl = new OneTimeUrl();
        oneTimeUrl.setUrl(RandomStringUtils.randomAlphanumeric(32));
        oneTimeUrl.setAccount(account);
        oneTimeUrl.setNewEmail(newEmailAddress);
        oneTimeUrl.setActionType("e-mail");
        oneTimeUrl.setExpireDate(Timestamp.from(Instant.now().plus(expirationTime, SECONDS)));

        oneTimeUrlFacadeLocal.create(oneTimeUrl);

        emailSender.sendEmailChangeConfirmationEmail(account.getLanguage(), account.getFirstName(), newEmailAddress, oneTimeUrl.getUrl());

    }

    @Override
    public void sendPasswordResetAddressUrl(String email) {
        Account account = accountFacadeLocal.findByEmail(email);

        if (account == null) {
            return;
        }

        long expirationTime = 20 * 60;

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("system.properties")) {
            prop.load(input);
            expirationTime = Long.parseLong(prop.getProperty("system.time.password.reset"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<OneTimeUrl> oneTimeUrls = oneTimeUrlFacadeLocal.findByAccount(account).stream()
                .filter(oneTimeUrl -> oneTimeUrl.getActionType().equals("passwd"))
                .collect(Collectors.toList());

        OneTimeUrl oneTimeUrl;

        if (!oneTimeUrls.isEmpty()) {
            oneTimeUrl = oneTimeUrls.get(0);
            oneTimeUrl.setExpireDate(Timestamp.from(Instant.now().plus(expirationTime, SECONDS)));
        } else {
            oneTimeUrl = new OneTimeUrl();
            oneTimeUrl.setUrl(RandomStringUtils.randomAlphanumeric(32));
            oneTimeUrl.setAccount(account);
            oneTimeUrl.setActionType("passwd");
            oneTimeUrl.setExpireDate(Timestamp.from(Instant.now().plus(expirationTime, SECONDS)));
            oneTimeUrlFacadeLocal.create(oneTimeUrl);
        }

        emailSender.sendPasswordResetEmail(account.getLanguage(), account.getFirstName(), email, oneTimeUrl.getUrl());
    }

    @Override
    public boolean resetPassword(String url, String newPassword) {
        OneTimeUrl oneTimeUrl = oneTimeUrlFacadeLocal.findByUrl(url);

        if (oneTimeUrl == null
                || oneTimeUrl.getExpireDate().before(Timestamp.from(Instant.now()))
                || !oneTimeUrl.getActionType().equals("passwd")) {
            return false;
        }

        oneTimeUrl.getAccount().setPassword(DigestUtils.sha512Hex(newPassword));
        oneTimeUrl.getAccount().setModifiedBy(null);
        oneTimeUrl.getAccount().setModificationDate(Timestamp.from(Instant.now()));

        oneTimeUrlFacadeLocal.remove(oneTimeUrl);
        return true;
    }
}
