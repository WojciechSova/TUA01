package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.tuple.Pair;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccessLevelFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccountFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.OneTimeUrlFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces.AccountManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.utils.interfaces.EmailSenderLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.OneTimeUrl;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.AccessLevelExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.AccountExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;

import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
public class AccountManager extends AbstractManager implements AccountManagerLocal, SessionSynchronization {

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
    public List<Pair<Account, List<AccessLevel>>> getAllAccountsWithActiveAccessLevels() throws CommonExceptions {
        return Optional.of(accountFacadeLocal.findAll().stream()
                .map(account -> Pair.of(account, accessLevelFacadeLocal.findAllActiveByAccount(account)))
                .collect(Collectors.toList()))
                .orElseThrow(CommonExceptions::createNoResultException);
    }

    @Override
    public Pair<Account, List<AccessLevel>> getAccountWithLogin(String login) throws CommonExceptions {
        Account account = Optional.ofNullable(accountFacadeLocal.findByLogin(login)).orElseThrow(CommonExceptions::createNoResultException);
        List<AccessLevel> accessLevels = Optional.ofNullable(accessLevelFacadeLocal.findAllByAccount(account)).orElseThrow(CommonExceptions::createNoResultException);
        return Pair.of(account, accessLevels);
    }

    @Override
    public Pair<Account, List<AccessLevel>> getAccountWithActiveAccessLevels(String login) throws CommonExceptions {
        Account account = Optional.ofNullable(accountFacadeLocal.findByLogin(login)).orElseThrow(CommonExceptions::createNoResultException);
        List<AccessLevel> accessLevels = Optional.ofNullable(accessLevelFacadeLocal.findAllActiveByAccount(account)).orElseThrow(CommonExceptions::createNoResultException);
        return Pair.of(account, accessLevels);
    }

    @Override
    public void createAccount(Account account) {
        if (account.getPhoneNumber().isEmpty()) {
            account.setPhoneNumber(null);
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
    public void registerBadLogin(String login, String clientAddress) throws CommonExceptions {
        Account account = Optional.ofNullable(accountFacadeLocal.findByLogin(login)).orElseThrow(CommonExceptions::createNoResultException);

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
    public void registerGoodLogin(String login, String clientAddress) throws CommonExceptions {
        Account account = Optional.ofNullable(accountFacadeLocal.findByLogin(login)).orElseThrow(CommonExceptions::createNoResultException);
        account.setLastKnownGoodLogin(Timestamp.from(Instant.now()));
        account.setLastKnownGoodLoginIp(clientAddress);
        account.setNumberOfBadLogins(0);
    }

    @Override
    public void updateLanguage(String login, String language) throws CommonExceptions {
        Account account = Optional.ofNullable(accountFacadeLocal.findByLogin(login)).orElseThrow(CommonExceptions::createNoResultException);
        account.setLanguage(language);
        account.setModificationDate(Timestamp.from(Instant.now()));
        account.setModifiedBy(null);
    }

    @Override
    public void updateAccount(Account account, String modifiedBy) throws CommonExceptions {
        if (account.getPhoneNumber().isEmpty()) {
            account.setPhoneNumber(null);
        }
        Account accountFromDB = Optional.ofNullable(accountFacadeLocal.findByLogin(account.getLogin())).orElseThrow(CommonExceptions::createNoResultException);
        Account acc = SerializationUtils.clone(accountFromDB);
        acc.setActivityModifiedBy(accountFromDB.getActivityModifiedBy());

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

        if (modifiedBy.equals(account.getLogin())) {
            acc.setModifiedBy(null);
        } else {
            Account accModifiedBy = Optional.ofNullable(accountFacadeLocal.findByLogin(modifiedBy)).orElseThrow(CommonExceptions::createNoResultException);
            acc.setModifiedBy(accModifiedBy);
        }

        accountFacadeLocal.edit(acc);

        emailSender.sendModificationEmail(acc.getLanguage(), account.getFirstName(), accountFromDB.getEmail());
    }

    @Override
    public void addAccessLevel(String login, String targetLogin, String accessLevel) throws CommonExceptions, AccessLevelExceptions {
        if (!List.of("ADMIN", "EMPLOYEE", "CLIENT").contains(accessLevel)) {
            throw AccessLevelExceptions.createNotAcceptableException(AccessLevelExceptions.ERROR_NO_ACCESS_LEVEL);
        }

        Account account = Optional.ofNullable(accountFacadeLocal.findByLogin(targetLogin)).orElseThrow(CommonExceptions::createNoResultException);
        List<AccessLevel> accessLevels = Optional.ofNullable(accessLevelFacadeLocal.findAllByAccount(account)).orElseThrow(CommonExceptions::createNoResultException);

        if (accessLevels.stream().noneMatch(x -> x.getLevel().equals(accessLevel))) {
            AccessLevel newAccessLevel = new AccessLevel();
            newAccessLevel.setAccount(account);
            newAccessLevel.setLevel(accessLevel);
            newAccessLevel.setActive(true);
            newAccessLevel.setCreatedBy(Optional.ofNullable(accountFacadeLocal.findByLogin(login)).orElseThrow(CommonExceptions::createNoResultException));
            accessLevelFacadeLocal.create(newAccessLevel);
            emailSender.sendAddAccessLevelEmail(account.getLanguage(), account.getFirstName(), account.getEmail(), accessLevel);
            return;
        }

        accessLevels.forEach(x -> {
            if (x.getLevel().equals(accessLevel) && !x.getActive()) {
                x.setActive(true);
                x.setModifiedBy(Optional.ofNullable(accountFacadeLocal.findByLogin(login)).orElseThrow(CommonExceptions::createNoResultException));
                x.setModificationDate(Timestamp.from(Instant.now()));
                accessLevelFacadeLocal.edit(x);
                emailSender.sendAddAccessLevelEmail(account.getLanguage(), account.getFirstName(), account.getEmail(), accessLevel);
            }
        });
    }

    @Override
    public void removeAccessLevel(String login, String targetLogin, String accessLevel) {
        if (!List.of("ADMIN", "EMPLOYEE", "CLIENT").contains(accessLevel)) {
            throw AccessLevelExceptions.createNotAcceptableException(AccessLevelExceptions.ERROR_NO_ACCESS_LEVEL);
        }

        Account account = Optional.ofNullable(accountFacadeLocal.findByLogin(targetLogin)).orElseThrow(CommonExceptions::createNoResultException);
        List<AccessLevel> accessLevels = Optional.ofNullable(accessLevelFacadeLocal.findAllByAccount(account)).orElseThrow(CommonExceptions::createNoResultException);

        if (accessLevels.stream().noneMatch(x -> x.getLevel().equals(accessLevel))) {
            return;
        }

        accessLevels.forEach(x -> {
            if (x.getLevel().equals(accessLevel) && x.getActive()) {
                x.setActive(false);
                x.setModifiedBy(Optional.ofNullable(accountFacadeLocal.findByLogin(login)).orElseThrow(CommonExceptions::createNoResultException));
                x.setModificationDate(Timestamp.from(Instant.now()));
                accessLevelFacadeLocal.edit(x);
                emailSender.sendRemoveAccessLevelEmail(account.getLanguage(), account.getFirstName(), account.getEmail(), accessLevel);
            }
        });
    }

    public void changePassword(String login, String oldPassword, String newPassword) throws CommonExceptions, AccountExceptions {
        Account account = Optional.ofNullable(accountFacadeLocal.findByLogin(login)).orElseThrow(CommonExceptions::createNoResultException);
        String hashedOldPassword = DigestUtils.sha512Hex(oldPassword);
        if (!hashedOldPassword.equals(account.getPassword())) {
            throw AccountExceptions.createNotAcceptableException(AccountExceptions.ERROR_PASSWORD_NOT_CORRECT);
        } else if (newPassword.equals(oldPassword)) {
            throw AccountExceptions.createExceptionConflict(AccountExceptions.ERROR_SAME_PASSWORD);
        }

        account.setPassword(DigestUtils.sha512Hex(newPassword));
        account.setPasswordModificationDate(Timestamp.from(Instant.now()));
        accountFacadeLocal.edit(account);
    }

    @Override
    public void changeActivity(String login, boolean newActivity, String modifiedBy) throws CommonExceptions {
        Account account = Optional.ofNullable(accountFacadeLocal.findByLogin(login)).orElseThrow(CommonExceptions::createNoResultException);

        account.setActive(newActivity);
        account.setActivityModificationDate(new Timestamp(new Date().getTime()));
        if (modifiedBy == null || login.equals(modifiedBy)) {
            account.setActivityModifiedBy(null);
        } else {
            account.setActivityModifiedBy(Optional.ofNullable(accountFacadeLocal.findByLogin(modifiedBy)).orElseThrow(CommonExceptions::createNoResultException));
        }
        if (newActivity) {
            account.setNumberOfBadLogins(0);
        }

        accountFacadeLocal.edit(account);

        emailSender.sendChangedActivityEmail(account.getLanguage(), account.getFirstName(), account.getEmail(), account.getActive());
    }

    @Override
    public void notifyAdminAboutLogin(String login, String clientAddress) throws CommonExceptions {
        Account account = Optional.ofNullable(accountFacadeLocal.findByLogin(login)).orElseThrow(CommonExceptions::createNoResultException);

        emailSender.sendAdminAuthenticationEmail(account.getLanguage(), account.getFirstName(), account.getEmail(), clientAddress);
    }

    @Override
    public void confirmAccount(String url) throws CommonExceptions, AccountExceptions {
        if (url == null) {
            throw AccountExceptions.createNotFoundException(AccountExceptions.ERROR_URL_NOT_FOUND);
        }

        OneTimeUrl oneTimeUrl = Optional.ofNullable(oneTimeUrlFacadeLocal.findByUrl(url)).orElseThrow(CommonExceptions::createNoResultException);

        if (Instant.now().isAfter(oneTimeUrl.getExpireDate().toInstant())) {
            throw AccountExceptions.createGoneException(AccountExceptions.ERROR_URL_EXPIRED);
        } else if (!oneTimeUrl.getActionType().equals("verify")) {
            throw AccountExceptions.createNotAcceptableException(AccountExceptions.ERROR_URL_TYPE);
        }

        if (url.equals(oneTimeUrl.getUrl())) {
            Account account = accountFacadeLocal.findByLogin(Optional.ofNullable(oneTimeUrl.getAccount().getLogin()).orElseThrow(CommonExceptions::createNoResultException));
            account.setConfirmed(true);
            account.setConfirmedModificationDate(Timestamp.from(Instant.now()));
            accountFacadeLocal.edit(account);
            oneTimeUrlFacadeLocal.remove(oneTimeUrl);
            return;
        }

        throw AccountExceptions.createNotFoundException(AccountExceptions.ERROR_URL_NOT_FOUND);
    }

    @Override
    public void changeEmailAddress(String url) throws CommonExceptions, AccountExceptions {
        if (url == null) {
            throw AccountExceptions.createNotFoundException(AccountExceptions.ERROR_URL_NOT_FOUND);
        }

        OneTimeUrl oneTimeUrl = Optional.ofNullable(oneTimeUrlFacadeLocal.findByUrl(url)).orElseThrow(CommonExceptions::createNoResultException);

        if (Instant.now().isAfter(oneTimeUrl.getExpireDate().toInstant())) {
            throw AccountExceptions.createGoneException(AccountExceptions.ERROR_URL_EXPIRED);
        } else if (!oneTimeUrl.getActionType().equals("e-mail")) {
            throw AccountExceptions.createNotAcceptableException(AccountExceptions.ERROR_URL_TYPE);
        }

        if (url.equals(oneTimeUrl.getUrl())) {
            Account account = accountFacadeLocal.findByLogin(Optional.ofNullable(oneTimeUrl.getAccount().getLogin()).orElseThrow(CommonExceptions::createNoResultException));
            account.setEmail(oneTimeUrl.getNewEmail());
            account.setEmailModificationDate(new Timestamp(new Date().getTime()));
            accountFacadeLocal.edit(account);
            oneTimeUrlFacadeLocal.remove(oneTimeUrl);
            return;
        }

        throw AccountExceptions.createNotFoundException(AccountExceptions.ERROR_URL_NOT_FOUND);
    }

    @Override
    public void sendChangeEmailAddressUrl(String login, String newEmailAddress, String requestedBy) {
        Account account = Optional.ofNullable(accountFacadeLocal.findByLogin(login)).orElseThrow(CommonExceptions::createNoResultException);

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("system.properties")) {
            prop.load(input);
            expirationTime = Long.parseLong(prop.getProperty("system.time.account.confirmation"));
        } catch (IOException e) {
            expirationTime = 86400;
            e.printStackTrace();
        }

        List<OneTimeUrl> oneTimeUrls = oneTimeUrlFacadeLocal.findByAccount(account).stream()
                .filter(oneTimeUrl -> oneTimeUrl.getActionType().equals("e-mail"))
                .collect(Collectors.toList());

        OneTimeUrl oneTimeUrl;

        if (!oneTimeUrls.isEmpty()) {
            oneTimeUrl = oneTimeUrls.get(0);
            oneTimeUrl.setUrl(RandomStringUtils.randomAlphanumeric(32));
            oneTimeUrl.setNewEmail(newEmailAddress);
            oneTimeUrl.setExpireDate(Timestamp.from(Instant.now().plus(expirationTime, SECONDS)));
            oneTimeUrl.setModifiedBy(Optional.ofNullable(accountFacadeLocal.findByLogin(requestedBy)).orElseThrow(CommonExceptions::createNoResultException));
            oneTimeUrl.setModificationDate(Timestamp.from(Instant.now()));
            oneTimeUrlFacadeLocal.edit(oneTimeUrl);
        } else {
            oneTimeUrl = new OneTimeUrl();
            oneTimeUrl.setUrl(RandomStringUtils.randomAlphanumeric(32));
            oneTimeUrl.setAccount(account);
            oneTimeUrl.setNewEmail(newEmailAddress);
            oneTimeUrl.setActionType("e-mail");
            oneTimeUrl.setExpireDate(Timestamp.from(Instant.now().plus(expirationTime, SECONDS)));
            oneTimeUrl.setCreatedBy(Optional.ofNullable(accountFacadeLocal.findByLogin(requestedBy)).orElseThrow(CommonExceptions::createNoResultException));
            oneTimeUrl.setCreationDate(Timestamp.from(Instant.now()));
            oneTimeUrlFacadeLocal.create(oneTimeUrl);
        }

        emailSender.sendEmailChangeConfirmationEmail(account.getLanguage(), account.getFirstName(), newEmailAddress, oneTimeUrl.getUrl());

    }

    @Override
    public void sendPasswordResetAddressUrl(String email, String requestedBy) {
        Account account = Optional.ofNullable(accountFacadeLocal.findByEmail(email))
                .orElseThrow(() -> AccountExceptions.createNotFoundException(AccountExceptions.ERROR_EMAIL_NOT_FOUND));

        long expirationTime = 20 * 60;

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("system.properties")) {
            prop.load(input);
            expirationTime = Long.parseLong(prop.getProperty("system.time.password.reset"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<OneTimeUrl> oneTimeUrls = Optional.of(oneTimeUrlFacadeLocal.findByAccount(account).stream()
                .filter(oneTimeUrl -> oneTimeUrl.getActionType().equals("passwd"))
                .collect(Collectors.toList()))
                .orElseThrow(CommonExceptions::createNoResultException);

        OneTimeUrl oneTimeUrl;

        if (!oneTimeUrls.isEmpty()) {
            oneTimeUrl = oneTimeUrls.get(0);
            oneTimeUrl.setExpireDate(Timestamp.from(Instant.now().plus(expirationTime, SECONDS)));
            if (requestedBy != null) {
                oneTimeUrl.setModifiedBy(Optional.ofNullable(accountFacadeLocal.findByLogin(requestedBy)).orElseThrow(CommonExceptions::createNoResultException));
            } else {
                oneTimeUrl.setModifiedBy(null);
            }
            oneTimeUrl.setModificationDate(Timestamp.from(Instant.now()));
            oneTimeUrlFacadeLocal.edit(oneTimeUrl);
        } else {
            oneTimeUrl = new OneTimeUrl();
            oneTimeUrl.setUrl(RandomStringUtils.randomAlphanumeric(32));
            oneTimeUrl.setAccount(account);
            oneTimeUrl.setActionType("passwd");
            oneTimeUrl.setExpireDate(Timestamp.from(Instant.now().plus(expirationTime, SECONDS)));
            if (requestedBy != null) {
                oneTimeUrl.setCreatedBy(Optional.ofNullable(accountFacadeLocal.findByLogin(requestedBy)).orElseThrow(CommonExceptions::createNoResultException));
            }
            oneTimeUrl.setCreationDate(Timestamp.from(Instant.now()));
            oneTimeUrlFacadeLocal.create(oneTimeUrl);
        }

        emailSender.sendPasswordResetEmail(account.getLanguage(), account.getFirstName(), email, oneTimeUrl.getUrl());
    }

    @Override
    public void resetPassword(String url, String newPassword) {
        OneTimeUrl oneTimeUrl = Optional.ofNullable(oneTimeUrlFacadeLocal.findByUrl(url)).orElseThrow(CommonExceptions::createNoResultException);

        if (Instant.now().isAfter(oneTimeUrl.getExpireDate().toInstant())) {
            throw AccountExceptions.createGoneException(AccountExceptions.ERROR_URL_EXPIRED);
        } else if (!oneTimeUrl.getActionType().equals("passwd")) {
            throw AccountExceptions.createNotAcceptableException(AccountExceptions.ERROR_URL_TYPE);
        }

        oneTimeUrl.getAccount().setPassword(DigestUtils.sha512Hex(newPassword));
        oneTimeUrl.getAccount().setPasswordModificationDate(Timestamp.from(Instant.now()));

        oneTimeUrlFacadeLocal.remove(oneTimeUrl);
    }

    @Override
    public String getTimezone(String login) {
        return accountFacadeLocal.findByLogin(login).getTimeZone();
    }
}
