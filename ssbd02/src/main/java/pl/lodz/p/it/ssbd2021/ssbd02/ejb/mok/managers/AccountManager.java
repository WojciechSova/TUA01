package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccessLevelFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccountFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.OneTimeUrlFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces.AccountManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.utils.interfaces.EmailSenderLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.OneTimeUrl;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mok.AccessLevelExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mok.AccountExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mok.OneTimeUrlExceptions;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.security.enterprise.credential.Password;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.SECONDS;

/**
 * Manager kont
 *
 * @author Daniel Łondka
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
@Interceptors(TrackerInterceptor.class)
public class AccountManager extends AbstractManager implements AccountManagerLocal, SessionSynchronization {

    private static final Properties prop = new Properties();
    private static final Logger logger = LogManager.getLogger();
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
    @RolesAllowed({"ADMIN"})
    public List<Pair<Account, List<AccessLevel>>> getAllAccountsWithActiveAccessLevels() {
        return accountFacadeLocal.findAll().stream()
                .map(account -> Pair.of(account, accessLevelFacadeLocal.findAllActiveByAccount(account)))
                .collect(Collectors.toList());
    }

    @Override
    @RolesAllowed({"ADMIN", "EMPLOYEE", "CLIENT"})
    public Pair<Account, List<AccessLevel>> getAccountWithLogin(String login) {
        Account account = accountFacadeLocal.findByLogin(login);
        List<AccessLevel> accessLevels = accessLevelFacadeLocal.findAllByAccount(account);
        return Pair.of(account, accessLevels);
    }

    @Override
    @RolesAllowed({"ADMIN", "EMPLOYEE", "CLIENT"})
    public Pair<Account, List<AccessLevel>> getAccountWithActiveAccessLevels(String login) {
        Account account = accountFacadeLocal.findByLogin(login);
        List<AccessLevel> accessLevels = accessLevelFacadeLocal.findAllActiveByAccount(account);
        return Pair.of(account, accessLevels);
    }

    @Override
    @PermitAll
    public void createAccount(Account account) {
        if (oneTimeUrlFacadeLocal.findListByEmail(account.getEmail()).size() != 0) {
            throw OneTimeUrlExceptions.createExceptionConflict(OneTimeUrlExceptions.ERROR_NEW_EMAIL_UNIQUE);
        }

        if (account.getPhoneNumber() == null || account.getPhoneNumber().isEmpty()) {
            account.setPhoneNumber(null);
        }
        account.setPassword(DigestUtils.sha512Hex(account.getPassword()));
        AccessLevel accessLevel = new AccessLevel();
        accessLevel.setLevel("CLIENT");
        accessLevel.setAccount(account);

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("system.properties")) {
            prop.load(input);
            expirationTime = Long.parseLong(prop.getProperty("system.time.account.confirmation"));
        } catch (IOException | NullPointerException | NumberFormatException e) {
            expirationTime = 86400;
            logger.warn(e);
        }

        OneTimeUrl oneTimeUrl = new OneTimeUrl();
        oneTimeUrl.setUrl(RandomStringUtils.randomAlphanumeric(32));
        oneTimeUrl.setAccount(account);
        oneTimeUrl.setActionType("verify");
        oneTimeUrl.setExpireDate(Timestamp.from(Instant.now().plus(expirationTime, SECONDS)));

        accountFacadeLocal.create(account);
        accessLevelFacadeLocal.create(accessLevel);
        oneTimeUrlFacadeLocal.create(oneTimeUrl);
        logger.info("The user with login {} created their account",
                account.getLogin());
        emailSender.sendRegistrationEmail(account.getLanguage(), account.getFirstName(), account.getEmail(), oneTimeUrl.getUrl());
    }

    @Override
    @PermitAll
    public void registerBadLogin(String login, String clientAddress) {
        Account account = accountFacadeLocal.findByLogin(login);

        account.setLastKnownBadLogin(Timestamp.from(Instant.now()));
        account.setLastKnownBadLoginIp(clientAddress);
        int badLogins = account.getNumberOfBadLogins() + 1;
        int badLoginsThreshold = 3;

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("system.properties")) {
            prop.load(input);
            badLoginsThreshold = Integer.parseInt(prop.getProperty("system.login.incorrect.threshold"));
        } catch (IOException | NullPointerException | NumberFormatException e) {
            logger.warn(e);
        }

        account.setNumberOfBadLogins(badLogins);

        if (badLogins >= badLoginsThreshold) {
            changeActivity(account.getLogin(), false, null);
        }

    }

    @Override
    @RolesAllowed({"ADMIN", "EMPLOYEE", "CLIENT"})
    public void updateAccount(Account account, String modifiedBy) {
        if (account.getPhoneNumber() == null || account.getPhoneNumber().isEmpty()) {
            account.setPhoneNumber(null);
        }
        Account accountFromDB = accountFacadeLocal.findByLogin(account.getLogin());
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
            Account accModifiedBy = accountFacadeLocal.findByLogin(modifiedBy);
            acc.setModifiedBy(accModifiedBy);
        }

        accountFacadeLocal.edit(acc);
        logger.info("The user with login {} updated the account with login {}",
                this.getInvokerId(), accountFromDB.getLogin());
        emailSender.sendModificationEmail(acc.getLanguage(), account.getFirstName(), accountFromDB.getEmail());
    }

    @Override
    @RolesAllowed({"ADMIN"})
    public void addAccessLevel(String login, String targetLogin, String accessLevel) {
        if (!List.of("ADMIN", "EMPLOYEE", "CLIENT").contains(accessLevel)) {
            throw AccessLevelExceptions.createBadRequestException(AccessLevelExceptions.ERROR_NO_ACCESS_LEVEL);
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
            logger.info("The user with login {} created {} access level for account with login {}",
                    this.getInvokerId(), accessLevel, login);
            emailSender.sendAddAccessLevelEmail(account.getLanguage(), account.getFirstName(), account.getEmail(), accessLevel);
            return;
        }

        accessLevels.forEach(x -> {
            if (x.getLevel().equals(accessLevel) && !x.getActive()) {
                x.setActive(true);
                x.setModifiedBy(accountFacadeLocal.findByLogin(login));
                x.setModificationDate(Timestamp.from(Instant.now()));
                accessLevelFacadeLocal.edit(x);
                logger.info("The user with login {} activated {} access level for account with login {}",
                        this.getInvokerId(), accessLevel, login);
                emailSender.sendAddAccessLevelEmail(account.getLanguage(), account.getFirstName(), account.getEmail(), accessLevel);
            }
        });
    }

    @Override
    @RolesAllowed({"ADMIN"})
    public void removeAccessLevel(String login, String targetLogin, String accessLevel) {
        if (!List.of("ADMIN", "EMPLOYEE", "CLIENT").contains(accessLevel)) {
            throw AccessLevelExceptions.createBadRequestException(AccessLevelExceptions.ERROR_NO_ACCESS_LEVEL);
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
                logger.info("The user with login {} removed {} access level from account with login {}",
                        this.getInvokerId(), accessLevel, login);
                emailSender.sendRemoveAccessLevelEmail(account.getLanguage(), account.getFirstName(), account.getEmail(), accessLevel);
            }
        });
    }

    @Override
    @RolesAllowed({"ADMIN", "EMPLOYEE", "CLIENT"})
    public void changePassword(String login, Password oldPassword, Password newPassword) {
        Account account = accountFacadeLocal.findByLogin(login);
        String hashedOldPassword = DigestUtils.sha512Hex(String.valueOf(oldPassword.getValue()));
        if (!hashedOldPassword.equals(account.getPassword())) {
            throw AccountExceptions.createBadRequestException(AccountExceptions.ERROR_PASSWORD_NOT_CORRECT);
        } else if (String.valueOf(newPassword.getValue()).equals(String.valueOf(oldPassword.getValue()))) {
            throw AccountExceptions.createExceptionConflict(AccountExceptions.ERROR_SAME_PASSWORD);
        }

        account.setPassword(DigestUtils.sha512Hex(String.valueOf(newPassword.getValue())));
        account.setPasswordModificationDate(Timestamp.from(Instant.now()));
        logger.info("The user with login {} changed their password",
                login);
        accountFacadeLocal.edit(account);
    }

    @Override
    @PermitAll
    public void changeActivity(String login, boolean newActivity, String modifiedBy) {
        Account account = accountFacadeLocal.findByLogin(login);

        account.setActive(newActivity);
        account.setActivityModificationDate(Timestamp.from(Instant.now()));
        if (modifiedBy == null || login.equals(modifiedBy)) {
            account.setActivityModifiedBy(null);
        } else {
            account.setActivityModifiedBy(accountFacadeLocal.findByLogin(modifiedBy));
        }
        if (newActivity) {
            account.setNumberOfBadLogins(0);
        }

        accountFacadeLocal.edit(account);
        logger.info("The user with login {} changed activity of the account with login {} to {}",
                this.getInvokerId(), login, newActivity);
        emailSender.sendChangedActivityEmail(account.getLanguage(), account.getFirstName(), account.getEmail(), account.getActive());
    }

    @Override
    @PermitAll
    public void confirmAccount(String url) {
        if (url == null) {
            throw AccountExceptions.createNotFoundException(AccountExceptions.ERROR_URL_NOT_FOUND);
        }

        OneTimeUrl oneTimeUrl = oneTimeUrlFacadeLocal.findByUrl(url);

        if (Instant.now().isAfter(oneTimeUrl.getExpireDate().toInstant())) {
            throw AccountExceptions.createGoneException(AccountExceptions.ERROR_URL_EXPIRED);
        } else if (!oneTimeUrl.getActionType().equals("verify")) {
            throw AccountExceptions.createBadRequestException(AccountExceptions.ERROR_URL_TYPE);
        }

        if (url.equals(oneTimeUrl.getUrl())) {
            Account account = accountFacadeLocal.findByLogin(oneTimeUrl.getAccount().getLogin());
            account.setConfirmed(true);
            account.setConfirmedModificationDate(Timestamp.from(Instant.now()));
            accountFacadeLocal.edit(account);
            logger.info("The user with login {} confirmed their account",
                    oneTimeUrl.getAccount().getLogin());
            oneTimeUrlFacadeLocal.remove(oneTimeUrl);
            return;
        }

        throw AccountExceptions.createNotFoundException(AccountExceptions.ERROR_URL_NOT_FOUND);
    }

    @Override
    @PermitAll
    public void changeEmailAddress(String url) {
        if (url == null) {
            throw AccountExceptions.createNotFoundException(AccountExceptions.ERROR_URL_NOT_FOUND);
        }

        OneTimeUrl oneTimeUrl = oneTimeUrlFacadeLocal.findByUrl(url);

        if (Instant.now().isAfter(oneTimeUrl.getExpireDate().toInstant())) {
            throw AccountExceptions.createGoneException(AccountExceptions.ERROR_URL_EXPIRED);
        } else if (!oneTimeUrl.getActionType().equals("e-mail")) {
            throw AccountExceptions.createBadRequestException(AccountExceptions.ERROR_URL_TYPE);
        }

        if (url.equals(oneTimeUrl.getUrl())) {
            Account account = accountFacadeLocal.findByLogin(oneTimeUrl.getAccount().getLogin());
            account.setEmail(oneTimeUrl.getNewEmail());
            account.setEmailModificationDate(Timestamp.from(Instant.now()));
            accountFacadeLocal.edit(account);
            logger.info("The user with login {} changed the email address of the account with login {}",
                    this.getInvokerId(), oneTimeUrl.getAccount().getLogin());
            oneTimeUrlFacadeLocal.remove(oneTimeUrl);
            return;
        }

        throw AccountExceptions.createNotFoundException(AccountExceptions.ERROR_URL_NOT_FOUND);
    }

    @Override
    @RolesAllowed({"ADMIN", "CLIENT", "EMPLOYEE"})
    public void sendChangeEmailAddressUrl(String login, String newEmailAddress, String requestedBy) {
        Account account = accountFacadeLocal.findByLogin(login);

        if (accountFacadeLocal.findListByEmail(newEmailAddress).size() != 0) {
            throw AccountExceptions.createExceptionConflict(AccountExceptions.ERROR_EMAIL_UNIQUE);
        }

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("system.properties")) {
            prop.load(input);
            expirationTime = Long.parseLong(prop.getProperty("system.time.account.confirmation"));
        } catch (IOException | NullPointerException | NumberFormatException e) {
            expirationTime = 86400;
            logger.warn(e);
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
            oneTimeUrl.setModifiedBy(accountFacadeLocal.findByLogin(requestedBy));
            oneTimeUrl.setModificationDate(Timestamp.from(Instant.now()));
            oneTimeUrlFacadeLocal.edit(oneTimeUrl);
        } else {
            oneTimeUrl = new OneTimeUrl();
            oneTimeUrl.setUrl(RandomStringUtils.randomAlphanumeric(32));
            oneTimeUrl.setAccount(account);
            oneTimeUrl.setNewEmail(newEmailAddress);
            oneTimeUrl.setActionType("e-mail");
            oneTimeUrl.setExpireDate(Timestamp.from(Instant.now().plus(expirationTime, SECONDS)));
            oneTimeUrl.setCreatedBy(accountFacadeLocal.findByLogin(requestedBy));
            oneTimeUrl.setCreationDate(Timestamp.from(Instant.now()));
            oneTimeUrlFacadeLocal.create(oneTimeUrl);
        }

        emailSender.sendEmailChangeConfirmationEmail(account.getLanguage(), account.getFirstName(), newEmailAddress, oneTimeUrl.getUrl());
    }

    @Override
    @PermitAll
    public void sendPasswordResetAddressUrl(String email, String requestedBy) {
        Account account = accountFacadeLocal.findByEmail(email);

        long expirationTime = 20 * 60;

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("system.properties")) {
            prop.load(input);
            expirationTime = Long.parseLong(prop.getProperty("system.time.password.reset"));
        } catch (IOException | NullPointerException | NumberFormatException e) {
            logger.warn(e);
        }

        List<OneTimeUrl> oneTimeUrls = oneTimeUrlFacadeLocal.findByAccount(account).stream()
                .filter(oneTimeUrl -> oneTimeUrl.getActionType().equals("passwd"))
                .collect(Collectors.toList());

        OneTimeUrl oneTimeUrl;

        if (!oneTimeUrls.isEmpty()) {
            oneTimeUrl = oneTimeUrls.get(0);
            oneTimeUrl.setExpireDate(Timestamp.from(Instant.now().plus(expirationTime, SECONDS)));
            if (requestedBy != null) {
                oneTimeUrl.setModifiedBy(accountFacadeLocal.findByLogin(requestedBy));
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
                oneTimeUrl.setCreatedBy(accountFacadeLocal.findByLogin(requestedBy));
            }
            oneTimeUrl.setCreationDate(Timestamp.from(Instant.now()));
            oneTimeUrlFacadeLocal.create(oneTimeUrl);
        }

        emailSender.sendPasswordResetEmail(account.getLanguage(), account.getFirstName(), email, oneTimeUrl.getUrl());
    }

    @Override
    @PermitAll
    public void resetPassword(String url, Password newPassword) {
        OneTimeUrl oneTimeUrl = oneTimeUrlFacadeLocal.findByUrl(url);

        if (Instant.now().isAfter(oneTimeUrl.getExpireDate().toInstant())) {
            throw AccountExceptions.createGoneException(AccountExceptions.ERROR_URL_EXPIRED);
        } else if (!oneTimeUrl.getActionType().equals("passwd")) {
            throw AccountExceptions.createBadRequestException(AccountExceptions.ERROR_URL_TYPE);
        }

        oneTimeUrl.getAccount().setPassword(DigestUtils.sha512Hex(String.valueOf(newPassword.getValue())));
        oneTimeUrl.getAccount().setPasswordModificationDate(Timestamp.from(Instant.now()));
        logger.info("The user with login {} reset their password",
                oneTimeUrl.getAccount().getLogin());
        oneTimeUrlFacadeLocal.remove(oneTimeUrl);
    }

    @Override
    @PermitAll
    public String registerGoodLoginAndGetTimezone(String login, Set<String> callerGroups, String clientAddress, String language) {
        Account account = accountFacadeLocal.findByLogin(login);

        if (callerGroups.contains("ADMIN")) {
            emailSender.sendAdminAuthenticationEmail(account.getLanguage(), account.getFirstName(), account.getEmail(), clientAddress);
        }

        account.setLastKnownGoodLogin(Timestamp.from(Instant.now()));
        account.setLastKnownGoodLoginIp(clientAddress);
        account.setNumberOfBadLogins(0);

        account.setLanguage(language);
        logger.info("The language of the account with login {} changed to {}",
                this.getInvokerId(), language);

        return account.getTimeZone();
    }
}
