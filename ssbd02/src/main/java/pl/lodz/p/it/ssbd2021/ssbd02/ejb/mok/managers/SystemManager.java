package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccessLevelFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccountFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.OneTimeUrlFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces.SystemManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.utils.interfaces.EmailSenderLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.OneTimeUrl;

import javax.ejb.*;
import javax.inject.Inject;
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
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SystemManager implements SystemManagerLocal {

    private static final Properties prop = new Properties();


    @Inject
    private AccountFacadeLocal accountFacadeLocal;

    @Inject
    private AccessLevelFacadeLocal accessLevelFacadeLocal;

    @Inject
    private OneTimeUrlFacadeLocal oneTimeUrlFacadeLocal;

    @Inject
    private EmailSenderLocal emailSender;

    @Override
    @Schedule(hour = "*", persistent = false)
    public void removeUnconfirmedAccounts() {
        long removalTime = 86400000;
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("system.properties")) {

            prop.load(input);
            removalTime = Long.parseLong(prop.getProperty("system.time.account.confirmation"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        final long finalRemovalTime = removalTime;

        List<Account> unconfirmedAccounts = accountFacadeLocal.findByConfirmed(false);
        List<Account> accountsToDelete = unconfirmedAccounts.stream()
                .filter(account -> Timestamp.from(Instant.now()).getTime() - (account.getCreationDate()).getTime() > finalRemovalTime)
                .collect(Collectors.toList());
        List<List<AccessLevel>> accessLevelsToDelete = new ArrayList<>();
        accountsToDelete.forEach(
                account -> accessLevelsToDelete.add(accessLevelFacadeLocal.findAllByAccount(account)));

        accessLevelsToDelete.stream()
                .flatMap(List::stream)
                .forEach(accessLevel -> accessLevelFacadeLocal.remove(accessLevel));
        accountsToDelete.forEach(account -> accountFacadeLocal.remove(account));

        accountsToDelete.forEach(account -> emailSender.sendRemovalEmail(account.getLanguage(), account.getFirstName(), account.getEmail()));
    }

    @Override
    @Schedule(minute = "20", hour = "*", persistent = false)
    public void removeInactiveUrl() {
        List<OneTimeUrl> expired = oneTimeUrlFacadeLocal.findExpired();

        expired.forEach(
                oneTimeUrl -> oneTimeUrlFacadeLocal.remove(oneTimeUrl)
        );
    }
}
