package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Booking;

import javax.ejb.Local;
import java.util.List;

/**
 * Lokalny interfejs managera rezerwacji
 *
 * @author Wojciech Sowa
 */
@Local
public interface BookingManagerLocal {

    /**
     * Metoda wyszukująca wszystkie rezerwacje.
     *
     * @return Lista rezerwacji {@link Booking}
     */
    List<Booking> getAllBookings();

    /**
     * Metoda wyszukująca wszystkie aktualne rezerwacje.
     *
     * @return Lista aktualnych rezerwacji {@link Booking}
     */
    List<Booking> getAllCurrentBookings();

    /**
     * Metoda wyszukująca wszystkie zakończone rezerwacje.
     *
     * @return Lista zakończonych rezerwacji {@link Booking}
     */
    List<Booking> getAllFinishedBookings();

    /**
     * Metoda wyszukująca wszystkie rezerwacje użytkownika o podanym loginie.
     *
     * @param login Login użytkownika, którego rezerwacje chcemy uzyskać
     * @return Lista rezerwacji {@link Booking} danego użytkownika
     */
    List<Booking> getAllBookingsByAccount(String login);

    /**
     * Metoda wyszukująca rezerwacje o podanym numerze.
     *
     * @param number Numer rezerwacji, który chcemy wyszukać
     * @return Encja typu {@link Booking}
     */
    Booking getBookingByNumber(String number);

    /**
     * Metoda wyszukująca rezerwację o podanym loginie i o podanym numerze.
     *
     * @param login  Login użytkownika, którego rezerwacji chcemy wyszukać
     * @param number Numer rezerwacji, który chcemy wyszukać
     * @return Encja typu {@link Booking}
     */
    Booking getBookingByAccountAndNumber(String login, String number);

    /**
     * Metoda zwracająca potrzebne miejsce na promie na pojazd o podanej nazwie typu.
     *
     * @param name Nazwa typu pojazdu
     * @return Wartość typu {@link Float}
     */
    Float getRequiredSpaceByVehicleTypeName(String name);

    /**
     * Metoda tworząca rezerwacje.
     *
     * @param numberOfPeople Liczba osób przypisanych do rezerwacji
     * @param cruiseNumber Numer rejsu przypisanego do rezerwacji
     * @param cabinNumber Numer kajuty przypisanej do rezerwacji
     * @param login Login użytkownika tworzącego rezerwację
     * @param vehicleTypeName Typ pojazdu wybrany przez użytkownika
     */
    void createBooking(int numberOfPeople, String cruiseNumber, String cabinNumber, String login, String vehicleTypeName);

    /**
     * Metoda usuwa rezerwacje o kodzie zawartym w encji {@link Booking}.
     *
     * @param login  Login użytkownika, który usuwa swoją rezerwację
     * @param number Numer rezerwacji, którą chcemy usunąć
     */
    void removeBooking(String login, String number);

    /**
     * Metoda zwracająca status transakcji.
     *
     * @return Status transakcji - true w przypadku jej powodzenia, false w przypadku jej wycofania
     */
    boolean isTransactionRolledBack();
}
