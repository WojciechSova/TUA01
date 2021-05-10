package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccessLevelFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccountFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces.SystemManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mail.EmailSender;

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
    AccountFacadeLocal accountFacadeLocal;

    @Inject
    AccessLevelFacadeLocal accessLevelFacadeLocal;

    @Override
    @Schedule(hour = "*/1", persistent = false)
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

        accountsToDelete.forEach(account -> EmailSender.sendRemovalEmail(account.getFirstName(), account.getEmail()));
    }
}
