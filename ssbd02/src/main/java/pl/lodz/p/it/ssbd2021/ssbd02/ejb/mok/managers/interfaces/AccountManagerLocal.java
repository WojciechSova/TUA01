package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces;

import org.apache.commons.lang3.tuple.Pair;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;

import javax.ejb.Local;
import javax.ws.rs.WebApplicationException;
import java.util.List;

/**
 * Lokalny interfejs managera kont
 *
 * @author Daniel Łondka
 */
@Local
public interface AccountManagerLocal {

    /**
     * Metoda wyszukująca wszystkie konta wraz z ich poziomami dostępu
     *
     * @return Lista par kont {@link Account} i listy poziomów dostępu {@link AccessLevel} reprezentująca konta
     * i ich poziomy dostępu.
     */
    List<Pair<Account, List<AccessLevel>>> getAllAccountsWithAccessLevels();

    /**
     * Metoda wyszukująca konto o podanym loginie wraz z jego poziomami dostępu
     *
     * @param login Login konta, które chcemy wyszukać
     * @return Para reprezentująca konto, składająca się z klucza typu {@link Account} i wartości będącej listą obiektów typu {@link AccessLevel}
     */
    Pair<Account, List<AccessLevel>> getAccountWithLogin(String login);

    /**
     * Metoda tworząca konto wraz z początkowym poziomem dostępu klienta
     *
     * @param account Encja typu {@link Account}
     * @throws WebApplicationException Wyjątek zwracający kod odpowiedzi 409 w przypadku, gdy istnieje już konto
     * o podanym loginie, emailu bądź numerze telefonu
     */
    void createAccount(Account account) throws WebApplicationException;

    /**
     * Metoda dołączająca poziom dostępu do konta o podanym loginie
     *
     * @param login Login użytkownika, który nadaje poziom dostępu
     * @param targetLogin Login użytkownika
     * @param accessLevel Poziom dostępu jaki ma zostać dołączony
     */
    void addAccessLevel(String login, String targetLogin, String accessLevel);

    /**
     * Metoda odłączająca poziom dostępu do konta o podanym loginie
     *
     * @param login Login użytkownika, który dokonuje usunięcia poziomu dostępu
     * @param targetLogin Login użytkownika
     * @param accessLevel Poziom dostępu jaki ma zostać odłączony
     */
    void removeAccessLevel(String login, String targetLogin, String accessLevel);
}
