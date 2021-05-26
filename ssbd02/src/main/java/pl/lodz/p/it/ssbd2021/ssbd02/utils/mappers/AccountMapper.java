package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import org.apache.commons.lang3.tuple.Pair;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Klasa mapująca obiekty kont pomiędzy encjami a DTO
 *
 * @author Wojciech Sowa
 */
public class AccountMapper {

    /**
     * Metoda mapująca parę obiektów: {@link Account} oraz Listę obiektów {@link AccessLevel}
     * na obiekt {@link AccountGeneralDTO}
     *
     * @param pair Para obiektów {@link Account} oraz lista obiektów {@link AccessLevel}, które chcemy mapować
     * @return Obiekt typu {@link AccountGeneralDTO}
     */
    public static AccountGeneralDTO createAccountGeneralDTOFromEntities(Pair<Account, List<AccessLevel>> pair) {
        Account acc = pair.getLeft();
        List<AccessLevel> accessLvls = pair.getRight();
        AccountGeneralDTO accountGeneralDTO = new AccountGeneralDTO();
        accountGeneralDTO.setLogin(acc.getLogin());
        accountGeneralDTO.setActive(acc.getActive());
        accountGeneralDTO.setFirstName(acc.getFirstName());
        accountGeneralDTO.setLastName(acc.getLastName());
        accountGeneralDTO.setAccessLevel(accessLvls.stream()
                .map(AccessLevel::getLevel)
                .collect(Collectors.toList()));
        accountGeneralDTO.setVersion(acc.getVersion());
        return accountGeneralDTO;
    }

    /**
     * Metoda mapująca obiekt {@link Account} na obiekt {@link AccountGeneralDTO}
     * Używana jedynie w pakiecie
     *
     * @param account Obiekt {@link Account}, który chcemy mapować
     * @return Obiekt typu {@link AccountGeneralDTO}
     */
    static AccountGeneralDTO createAccountGeneralDTOFromEntity(Account account) {
        if (account == null) {
            return null;
        }
        AccountGeneralDTO accountGeneralDTO = new AccountGeneralDTO();
        accountGeneralDTO.setLogin(account.getLogin());
        accountGeneralDTO.setActive(account.getActive());
        accountGeneralDTO.setFirstName(account.getFirstName());
        accountGeneralDTO.setLastName(account.getLastName());
        accountGeneralDTO.setVersion(account.getVersion());
        return accountGeneralDTO;
    }

    /**
     * Metoda mapująca parę obiektów: {@link Account} oraz Listę obiektów {@link AccessLevel}
     * na obiekt {@link AccountDetailsDTO}
     *
     * @param pair Para obiektów: {@link Account} oraz lista obiektów {@link AccessLevel}, które chcemy mapować
     * @return Obiekt typu {@link AccountDetailsDTO}
     */
    public static AccountDetailsDTO createAccountDetailsDTOFromEntities(Pair<Account, List<AccessLevel>> pair) {
        Account acc = pair.getLeft();
        List<AccessLevel> accessLvls = pair.getRight();

        AccountDetailsDTO accountDetailsDTO = new AccountDetailsDTO();
        accountDetailsDTO.setLogin(acc.getLogin());
        accountDetailsDTO.setPassword(acc.getPassword());
        accountDetailsDTO.setActive(acc.getActive());
        accountDetailsDTO.setConfirmed(acc.getConfirmed());
        accountDetailsDTO.setFirstName(acc.getFirstName());
        accountDetailsDTO.setLastName(acc.getLastName());
        accountDetailsDTO.setEmail(acc.getEmail());
        accountDetailsDTO.setPhoneNumber(acc.getPhoneNumber());
        accountDetailsDTO.setAccessLevel(accessLvls.stream()
                .map(AccessLevelMapper::createAccessLevelDTOFromEntity)
                .collect(Collectors.toList()));
        accountDetailsDTO.setLanguage(acc.getLanguage());
        accountDetailsDTO.setTimeZone(acc.getTimeZone());
        accountDetailsDTO.setModificationDate(acc.getModificationDate());
        accountDetailsDTO.setModifiedBy(createAccountGeneralDTOFromEntity(acc.getModifiedBy()));
        accountDetailsDTO.setActivityModificationDate(acc.getActivityModificationDate());
        accountDetailsDTO.setActivityModifiedBy(createAccountGeneralDTOFromEntity(acc.getActivityModifiedBy()));
        accountDetailsDTO.setConfirmedModificationDate(acc.getConfirmedModificationDate());
        accountDetailsDTO.setPasswordModificationDate(acc.getPasswordModificationDate());
        accountDetailsDTO.setEmailModificationDate(acc.getEmailModificationDate());
        accountDetailsDTO.setCreationDate(acc.getCreationDate());
        accountDetailsDTO.setLastKnownGoodLogin(acc.getLastKnownGoodLogin());
        accountDetailsDTO.setLastKnownGoodLoginIp(acc.getLastKnownGoodLoginIp());
        accountDetailsDTO.setLastKnownBadLogin(acc.getLastKnownBadLogin());
        accountDetailsDTO.setLastKnownBadLoginIp(acc.getLastKnownBadLoginIp());
        accountDetailsDTO.setNumberOfBadLogins(acc.getNumberOfBadLogins());
        accountDetailsDTO.setVersion(acc.getVersion());
        return accountDetailsDTO;
    }

    /**
     * Metoda mapująca obiekt {@link AccountDetailsDTO} na obiekt {@link Account}
     *
     * @param acc Obiekt {@link AccountDetailsDTO}, który chcemy mapować
     * @return Obiekt konta typu {@link Account}
     */
    public static Account createAccountFromAccountDetailsDTO(AccountDetailsDTO acc) {
        Account account = new Account();
        account.setLogin(acc.getLogin());
        account.setPassword(acc.getPassword());
        account.setFirstName(acc.getFirstName());
        account.setLastName(acc.getLastName());
        account.setEmail(acc.getEmail());
        account.setLanguage(acc.getLanguage());
        account.setTimeZone(acc.getTimeZone());
        account.setVersion(acc.getVersion());
        account.setPhoneNumber(acc.getPhoneNumber());
        return account;
    }
}
