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
     * Metoda wyszukująca wszystkie konta wraz z ich poziomami dostępu.
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
     * Metoda rejestrująca niepoprawne uwierzytelnienie użytkownika.
     * W bazie danych zapisywana jest data oraz adres IP, z którego próbowano się uwierzytelnić.
     *
     * @param login Login użytkownika, na którego konto próbowano się uwierzytelnić.
     * @param clientAddress Adres IP, z którego próbowano się uwierzytelnić.
     */
    void registerBadLogin(String login, String clientAddress);

    /**
     * Metoda rejestrująca poprawne uwierzytelnienie użytkownika.
     * W bazie danych zapisywana jest data oraz adres IP, z którego się uwierzytelniono.
     *
     * @param login Login użytkownika, który się uwierzytelnił.
     * @param clientAddress Adres IP, z którego się uwierzytelniono.
     */
    void registerGoodLogin(String login, String clientAddress);
}
