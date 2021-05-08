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

    /**
     * Metoda aktualizuje konto o loginie zawartym w encji {@link Account} oraz ustawia konto w polu modifiedBy na konto
     * użytkownika dokonującego zmiany
     *
     * @param account Encja typu {@link Account}
     * @param modifiedBy Login użytkownika, który edytuje encje
     * @throws WebApplicationException Wyjątek zwracający kod odpowiedzi 409 w przypadku, gdy istnieje już konto
     * o podanym emailu bądź numerze telefonu, kod odpowiedzi 406 w przypadku, gdy nie podano loginu
     */
    void updateAccount(Account account, String modifiedBy) throws WebApplicationException;

    /**
     * Metoda zmieniająca hasło użytkownika do konta
     *
     * @param login Login użytkownika
     * @param oldPassword Dotychczasowe hasło użytkownika do konta
     * @param newPassword Nowe hasło użytkownika do konta
     * @throws WebApplicationException Wyjątek zwracający kod odpowiedzi:
     * 406 w przypadku, gdy podane dotychczasowe hasło do konta jest nieprawidłowe,
     * 409 gdy podane nowe hasło jest identyczne jak hasło poprzednie
     */
    void changePassword(String login, String oldPassword, String newPassword) throws WebApplicationException;

    /**
     * Metoda zmieniająca aktywność użytkownika
     *
     * @param login Login użytkownika
     */
    void changeActivity(String login, boolean newActivity, String modifiedBy);

    /**
     * Metoda powiadamjająca administratora o logowaniu na jego konto.
     *
     * @param login Login administratora
     * @param clientAddress Adres IP, z którego nastapiło logowanie
     */
    void notifyAdminAboutLogin(String login, String clientAddress);
}
