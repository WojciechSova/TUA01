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
     * Metoda wyszukująca wszystkie konta wraz z ich aktywnymi poziomami dostępu.
     *
     * @return Lista par kont {@link Account} i listy poziomów dostępu {@link AccessLevel} reprezentująca konta
     * i ich poziomy dostępu.
     */
    List<Pair<Account, List<AccessLevel>>> getAllAccountsWithActiveAccessLevels();

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
     * Inkrementuje licznik nieudanych logowań konta.
     * W przypadku przekroczenia ustalonej liczby nieudanych prób uwierzytelnienia konto zostaje zablokowane.
     *
     * @param login Login użytkownika, na którego konto próbowano się uwierzytelnić.
     * @param clientAddress Adres IP, z którego próbowano się uwierzytelnić.
     */
    void registerBadLogin(String login, String clientAddress);

    /**
     * Metoda rejestrująca poprawne uwierzytelnienie użytkownika.
     * W bazie danych zapisywana jest data oraz adres IP, z którego się uwierzytelniono.
     * Ustawia licznik nieudanych logowań konta na 0.
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
     * Metoda zmieniająca aktywność użytkownika.
     * Podczas odblokowania konta ustawia jego licznik nieudanych logowań na 0.
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

    /**
     * Metoda aktywująca zarejestrowanego użytkownika.
     *
     * @param url Kod potwierdzający konto użytkownika
     * @return Prawda, jeżeli uda się potwierdzić użytkownika, w przeciwnym wypadku fałsz
     */
    boolean confirmAccount(String url);

    /**
     * Metoda zmieniająca adres email użytkownika.
     *
     * @param url Kod potwierdzający nowy adres email
     * @return Prawda jeżeli uda się potwierdzić adres email, w przeciwnym wypadku fałsz
     */
    boolean changeEmailAddress(String url, String modifiedBy);

    /**
     * Metoda wysyłająca wiadomość na nowy adres email z linkiem potwierdzającym jego zmianę.
     *
     * @param login Login użytkownika, którego adres email ma ulec zmianie
     * @param newEmailAddress Nowy adres email
     */
    void sendChangeEmailAddressUrl(String login, String newEmailAddress);
}
