package pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

/**
 * Klasa związana z wyjątkami dotyczącymi rezerwacji rozszerzająca {@link GeneralException}.
 *
 * @author Wojciech Sowa
 */
@ApplicationException(rollback = true)
public class BookingExceptions extends GeneralException {
    public static String ERROR_BOOKING_NUMBER_UNIQUE = "ERROR.BOOKING_NUMBER_UNIQUE";
    public static String ERROR_BOOKING_NUMBER_OF_PEOPLE_GREATER_THAN_ZERO = "ERROR.BOOKING_NUMBER_OF_PEOPLE_GREATER_THAN_ZERO";
    public static String ERROR_BOOKING_PRICE_GREATER_THAN_ZERO = "ERROR.BOOKING_PRICE_GREATER_THAN_ZERO";
    public static String ERROR_BOOKING_NOT_FOUND = "ERROR.BOOKING_NOT_FOUND";
    public static String ERROR_CANNOT_CANCEL_RESERVATION = "ERROR.CANNOT_CANCEL_RESERVATION";

    public BookingExceptions(Response.Status status, String key) {
        super(status, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 409 (Conflict).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link BookingExceptions}
     */
    public static BookingExceptions createConflictException(String key) {
        return new BookingExceptions(Response.Status.CONFLICT, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 406 (Not Acceptable).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link BookingExceptions}
     */
    public static BookingExceptions createNotAcceptableException(String key) {
        return new BookingExceptions(Response.Status.NOT_ACCEPTABLE, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 404 (Not Found).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link BookingExceptions}
     */
    public static BookingExceptions createNotFoundException(String key) {
        return new BookingExceptions(Response.Status.NOT_FOUND, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 410 (Gone).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link BookingExceptions}
     */
    public static BookingExceptions createGoneException(String key) {
        return new BookingExceptions(Response.Status.GONE, key);
    }
}
