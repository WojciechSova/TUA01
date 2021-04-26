package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import org.apache.commons.lang3.tuple.Pair;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.ClientData;

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

        return new AccountGeneralDTO(acc.getLogin(), acc.getActive(), acc.getFirstName(),
                acc.getLastName(), accessLvls.stream().map(AccessLevel::getLevel).collect(Collectors.toList()));
    }

    /**
     * Metoda mapująca obiekt {@link Account} na obiekt {@link AccountGeneralDTO}
     * Używana jedynie w pakiecie
     *
     * @param account Obiekt {@link Account}, który chcemy mapować
     * @return Obiekt typu {@link AccountGeneralDTO}
     */
    static AccountGeneralDTO createAccountGeneralDTOFromEntity(Account account) {
        return new AccountGeneralDTO(account.getLogin(), account.getActive(), account.getFirstName(),
                account.getLastName(), null);
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
        String phoneNumber = null;
        for (AccessLevel al : accessLvls) {
            if (al instanceof ClientData) {
                phoneNumber = ((ClientData) al).getPhoneNumber();
            }
        }

        return new AccountDetailsDTO(acc.getLogin(), acc.getPassword(), acc.getActive(), acc.getConfirmed(),
                acc.getFirstName(), acc.getLastName(), acc.getEmail(), phoneNumber,
                accessLvls.stream().map(AccessLevelMapper::createAccessLevelDTOFromEntity).collect(Collectors.toList()),
                acc.getLanguage(), acc.getTimeZone(), acc.getModificationDate(), createAccountGeneralDTOFromEntity(acc.getModifiedBy()),
                acc.getCreationDate(), acc.getLastKnownGoodLogin(), acc.getLastKnownGoodLoginIp(), acc.getLastKnownBadLogin(),
                acc.getLastKnownBadLoginIp(), acc.getNumberOfBadLogins());
    }

    /**
     * Metoda mapująca obiekt {@link AccountDetailsDTO} na parę obiektów: {@link Account}
     * oraz numer telefonu typu String
     *
     * @param acc Obiekt {@link AccountDetailsDTO}, który chcemy mapować
     * @return Para obiektów: {@link Account} oraz numer telefonu typu String
     */
    public static Pair<Account, String> createPairAccountPhoneNumberFromAccountDetailsDTO(AccountDetailsDTO acc) {
        Account account = new Account();
        account.setLogin(acc.getLogin());
        account.setPassword(acc.getPassword());
        account.setFirstName(acc.getFirstName());
        account.setLastName(acc.getLastName());
        account.setEmail(acc.getEmail());
        account.setLanguage(acc.getLanguage());
        account.setTimeZone(acc.getTimeZone());
        return Pair.of(account, acc.getPhoneNumber());
    }
}
