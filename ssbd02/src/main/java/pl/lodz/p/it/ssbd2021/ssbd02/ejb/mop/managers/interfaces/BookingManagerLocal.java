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
     * Metoda wyszukująca rezerwacje o podanym loginie i o podanym numerze.
     *
     * @param login Login użytkownika, którego rezerwacji chcemy wyszukać
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
     * @param booking Encja typu {@link Booking}
     */
    void createBooking(Booking booking);

    /**
     * Metoda usuwa rezerwacje o kodzie zawartym w encji {@link Booking}.
     *
     * @param booking Encja typu {@link Booking}
     */
    void removeBooking(Booking booking);
}
