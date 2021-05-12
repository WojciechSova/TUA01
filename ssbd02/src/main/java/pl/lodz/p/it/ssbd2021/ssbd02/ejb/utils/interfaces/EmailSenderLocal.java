package pl.lodz.p.it.ssbd2021.ssbd02.ejb.utils.interfaces;

import javax.ejb.Local;

/**
 * Lokalny interfejs służący do wysyłania wiadomości email.
 */
@Local
public interface EmailSenderLocal {

    /**
     * Metoda wysyłająca wiadomość email z linkiem pozwalającym na potwierdzenie zmiany adresu email przypisanego do konta.
     *
     * @param recipientName         Imię odbiorcy wiadomości.
     * @param recipientEmailAddress Adres email odbiorcy wiadomości.
     * @param link                  Jednorazowy adres url, który służy do potwierdzenia zmiany adresu email przez użytkownika.
     */
    void sendEmailChangeConfirmationEmail(String recipientName, String recipientEmailAddress, String link);

    /**
     * Metoda wysyłająca wiadomość email z linkiem pozwalającym na potwierdzenie nowo założonego konta.
     *
     * @param recipientName         Imię odbiorcy wiadomości.
     * @param recipientEmailAddress Adres email odbiorcy wiadomości.
     * @param link                  Jednorazowy adres url, który służy do potwierdzenia konta przez użytkownika.
     */
    void sendRegistrationEmail(String recipientName, String recipientEmailAddress, String link);

    /**
     * Metoda wysyłająca wiadomość email z informacją o zmianie aktywności konta.
     *
     * @param recipientName         Imię odbiorcy wiadomości.
     * @param recipientEmailAddress Adres email odbiorcy wiadomości.
     * @param active                Aktualny status aktywności konta.
     */
    void sendChangedActivityEmail(String recipientName, String recipientEmailAddress, boolean active);

    /**
     * Metoda wysyłająca wiadomość email informującą użytkownika o modyfikacji danych jego konta.
     *
     * @param recipientName         Imię odbiorcy wiadomości.
     * @param recipientEmailAddress Adres email odbiorcy wiadomości.
     */
    void sendModificationEmail(String recipientName, String recipientEmailAddress);

    /**
     * Metoda wysyłająca wiadomość email informującą użytkownika o dodaniu poziomu dostępu jego konta.
     *
     * @param recipientName         Imię odbiorcy wiadomości.
     * @param recipientEmailAddress Adres email odbiorcy wiadomości.
     */
    void sendAddAccessLevelEmail(String recipientName, String recipientEmailAddress, String accessLevel);

    /**
     * Metoda wysyłająca wiadomość email informującą użytkownika o usunięciu poziomu dostępu jego konta.
     *
     * @param recipientName         Imię odbiorcy wiadomości.
     * @param recipientEmailAddress Adres email odbiorcy wiadomości.
     */
    void sendRemoveAccessLevelEmail(String recipientName, String recipientEmailAddress, String accessLevel);

    /**
     * Metoda wysyłająca wiadomość email informującą użytkownika o usunięciu jego konta.
     *
     * @param recipientName         Imię odbiorcy wiadomości.
     * @param recipientEmailAddress Adres email odbiorcy wiadomości.
     */
    void sendRemovalEmail(String recipientName, String recipientEmailAddress);

    /**
     * Metoda wysyłająca wiadomość email informującą administratora o logowaniu na jego konto.
     *
     * @param firstName     Imię administratora
     * @param email         Adres email administratora
     * @param clientAddress Adres IP, z którego nastąpiło logowanie
     */
    void sendAdminAuthenticationEmail(String firstName, String email, String clientAddress);
}
