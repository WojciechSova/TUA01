package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces;

import org.apache.commons.lang3.tuple.Pair;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;

import javax.ejb.Local;
import javax.security.enterprise.credential.Password;
import java.util.List;
import java.util.Set;

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
     * Metoda wyszukująca konto wraz z jego aktywnymi poziomami dostępu.
     *
     * @param login Login konta, które chcemy wyszukać
     * @return Para reprezentująca konto, składająca się z klucza typu {@link Account} i wartości będącej listą obiektów typu {@link AccessLevel}
     */
    Pair<Account, List<AccessLevel>> getAccountWithActiveAccessLevels(String login);

    /**
     * Metoda tworząca konto wraz z początkowym poziomem dostępu klienta
     *
     * @param account Encja typu {@link Account}
     */
    void createAccount(Account account);

    /**
     * Metoda rejestrująca niepoprawne uwierzytelnienie użytkownika.
     * W bazie danych zapisywana jest data oraz adres IP, z którego próbowano się uwierzytelnić.
     * Inkrementuje licznik nieudanych logowań konta.
     * W przypadku przekroczenia ustalonej liczby nieudanych prób uwierzytelnienia konto zostaje zablokowane.
     *
     * @param login         Login użytkownika, na którego konto próbowano się uwierzytelnić.
     * @param clientAddress Adres IP, z którego próbowano się uwierzytelnić.
     */
    void registerBadLogin(String login, String clientAddress);

    /**
     * Metoda aktualizuje konto o loginie zawartym w encji {@link Account} oraz ustawia konto w polu modifiedBy na konto
     * użytkownika dokonującego zmiany
     *
     * @param account    Encja typu {@link Account}
     * @param modifiedBy Login użytkownika, który edytuje encje
     */
    void updateAccount(Account account, String modifiedBy);

    /**
     * Metoda dołączająca poziom dostępu do konta o podanym loginie
     *
     * @param login       Login użytkownika, który nadaje poziom dostępu
     * @param targetLogin Login użytkownika
     * @param accessLevel Poziom dostępu jaki ma zostać dołączony
     */
    void addAccessLevel(String login, String targetLogin, String accessLevel);

    /**
     * Metoda odłączająca poziom dostępu do konta o podanym loginie
     *
     * @param login       Login użytkownika, który dokonuje usunięcia poziomu dostępu
     * @param targetLogin Login użytkownika
     * @param accessLevel Poziom dostępu jaki ma zostać odłączony
     */
    void removeAccessLevel(String login, String targetLogin, String accessLevel);

    /**
     * Metoda zmieniająca hasło użytkownika do konta
     *
     * @param login       Login użytkownika
     * @param oldPassword Dotychczasowe hasło użytkownika do konta
     * @param newPassword Nowe hasło użytkownika do konta
     */
    void changePassword(String login, Password oldPassword, Password newPassword);

    /**
     * Metoda zmieniająca aktywność użytkownika.
     * Podczas odblokowania konta ustawia jego licznik nieudanych logowań na 0.
     *
     * @param login Login użytkownika
     */
    void changeActivity(String login, boolean newActivity, String modifiedBy);


    /**
     * Metoda aktywująca zarejestrowanego użytkownika.
     *
     * @param url Kod potwierdzający konto użytkownika
     */
    void confirmAccount(String url);

    /**
     * Metoda zmieniająca adres email użytkownika.
     *
     * @param url Kod potwierdzający nowy adres email
     */
    void changeEmailAddress(String url);

    /**
     * Metoda wysyłająca wiadomość na nowy adres email z linkiem potwierdzającym jego zmianę.
     *
     * @param login           Login użytkownika, którego adres email ma ulec zmianie
     * @param newEmailAddress Nowy adres email
     * @param requestedBy     Login użytkownika, który zlecił operację
     */
    void sendChangeEmailAddressUrl(String login, String newEmailAddress, String requestedBy);

    /**
     * Metoda wysyłająca wiadomość email z adresem służącym do resetowania hasła.
     * W przypadku, kiedy w bazie danych znajduje się już adres URL przypisany do tej akcji,
     * nie jest tworzony nowy adres URL a w wiadomości email znajduje się poprzednio wygenerowany adres,
     * którego okres ważności jest resetowany.
     * Okres ważności pobierany jest z pliku system.properties a domyślna wartość to 20 minut.
     *
     * @param email       Adres email użytkownika, którego hasło ma zostać zresetowane
     * @param requestedBy Login użytkownika, który zlecił operację
     */
    void sendPasswordResetAddressUrl(String email, String requestedBy);

    /**
     * Metoda resetująca hasło do konta.
     *
     * @param url         Jednorazowy url, który potwierdza możliwość resetowania hasła do konta
     * @param newPassword Nowe hasło użytkownika
     */
    void resetPassword(String url, Password newPassword);

    /**
     * Metoda zwracająca status transakcji.
     *
     * @return Status transakcji - true w przypadku jej powodzenia, false w przypadku jej wycofania
     */
    boolean isTransactionRolledBack();

    /**
     * Metoda rejestrująca poprawne uwierzytelnienie użytkownika.
     * W bazie danych zapisywana jest data oraz adres IP, z którego się uwierzytelniono.
     * Ustawia licznik nieudanych logowań konta na 0.
     * Wysyłane powiadomienie do administratora o logowaniu na jego konto.
     * Aktualizowany język użytkownika przy każdym uwierzytelnieniu.
     * W bazie danych zapisywany jest preferowany przez przeglądarkę język wyświetlania strony.
     * Metoda zwraca strefę czasową dla użytkownika.
     *
     * @param login         Login użytkownika
     * @param callerGroups  Grupy, do których należy użytkownik
     * @param clientAddress Adres IP, z którego nastapiło logowanie
     * @param language      Preferowany język wyświetlania strony
     * @return Strefa czasowa w formacie +00:00
     */
    String registerGoodLoginAndGetTimezone(String login, Set<String> callerGroups, String clientAddress, String language);
}
