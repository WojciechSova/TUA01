package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccessLevelFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccountFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.OneTimeUrlFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces.SystemManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.utils.interfaces.EmailSenderLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.OneTimeUrl;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.TrackerInterceptor;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.RunAs;
import javax.ejb.*;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Manager systemu
 *
 * @author Artur Madaj
 */
@Singleton
@Startup
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
@Interceptors(TrackerInterceptor.class)
public class SystemManager extends AbstractManager implements SystemManagerLocal, SessionSynchronization {

    private static final Properties prop = new Properties();
    private static final Logger logger = LogManager.getLogger();


    @Inject
    private AccountFacadeLocal accountFacadeLocal;

    @Inject
    private AccessLevelFacadeLocal accessLevelFacadeLocal;

    @Inject
    private OneTimeUrlFacadeLocal oneTimeUrlFacadeLocal;

    @Inject
    private EmailSenderLocal emailSender;

    @Override
    @Schedule(hour = "*", minute = "*", second = "0", persistent = false)
    @DenyAll
    public void removeUnconfirmedAccounts() {
        long removalTime = 86400L;
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("system.properties")) {

            prop.load(input);
            removalTime = Integer.parseInt(prop.getProperty("system.time.account.confirmation"));

        } catch (IOException | NullPointerException | NumberFormatException e) {
            logger.warn(e);
        }
        List<Account> accountsToDelete = accountFacadeLocal.findByUnconfirmedAndExpired(removalTime);
        List<List<AccessLevel>> accessLevelsToDelete = new ArrayList<>();
        List<List<OneTimeUrl>> urlsToDelete = new ArrayList<>();
        accountsToDelete.forEach(
                account -> {
                    accessLevelsToDelete.add(accessLevelFacadeLocal.findAllByAccount(account));
                    urlsToDelete.add(oneTimeUrlFacadeLocal.findByAccount(account));
                });

        accessLevelsToDelete.stream()
                .flatMap(List::stream)
                .forEach(accessLevel -> accessLevelFacadeLocal.remove(accessLevel));
        urlsToDelete.stream()
                .flatMap(List::stream)
                .forEach(url -> oneTimeUrlFacadeLocal.remove(url));
        accountsToDelete.forEach(account -> accountFacadeLocal.remove(account));

        accountsToDelete.forEach(account -> emailSender.sendRemovalEmail(account.getLanguage(), account.getFirstName(), account.getEmail()));
        logger.info("System removed {} unconfirmed accounts", accountsToDelete.size());
    }

    @Override
    @Schedule(hour = "*", minute = "*", second = "20", persistent = false)
    @DenyAll
    public void removeInactiveUrl() {
        List<OneTimeUrl> expired = oneTimeUrlFacadeLocal.findExpired();

        expired.forEach(
                oneTimeUrl -> oneTimeUrlFacadeLocal.remove(oneTimeUrl)
        );

        logger.info("System removed {} expired URLs", expired.size());
    }

    @Override
    @Schedule(hour = "*", minute = "*", second = "40", persistent = false)
    @DenyAll
    public void resendConfirmAccountEmail() {
        long removalTime = 86_400_000 / 2L;
        final long minute = 60_000;
        long actualTime = Timestamp.from(Instant.now()).getTime() / minute * minute + (minute / 2);

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("system.properties")) {
            prop.load(input);
            removalTime = Long.parseLong(prop.getProperty("system.time.account.confirmation")) * 1000 / 2;
        } catch (IOException | NullPointerException | NumberFormatException e) {
            logger.warn(e);
        }

        long finalRemovalTime = removalTime;

        List<OneTimeUrl> oneTimeUrls = oneTimeUrlFacadeLocal.findAll().stream()
                .filter(oneTimeUrl ->
                        "verify".equals(oneTimeUrl.getActionType()) &&
                                ((oneTimeUrl.getExpireDate()).getTime() - actualTime <= finalRemovalTime) &&
                                ((oneTimeUrl.getExpireDate()).getTime() - actualTime > finalRemovalTime - minute))
                .collect(Collectors.toList());

        oneTimeUrls.forEach(oneTimeUrl -> emailSender.sendRegistrationEmail(
                oneTimeUrl.getAccount().getLanguage(),
                oneTimeUrl.getAccount().getFirstName(),
                oneTimeUrl.getAccount().getEmail(),
                oneTimeUrl.getUrl()));
    }
}
