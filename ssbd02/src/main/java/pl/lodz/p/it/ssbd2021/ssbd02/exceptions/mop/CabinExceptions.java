package pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

/**
 * Klasa związana z wyjątkami dotyczącymi kajut rozszerzająca {@link GeneralException}.
 *
 * @author Wojciech Sowa
 */
@ApplicationException(rollback = true)
public class CabinExceptions extends GeneralException {
    public static String ERROR_CABIN_FERRY_NUMBER_UNIQUE = "ERROR.CABIN_FERRY_NUMBER_UNIQUE";
    public static String ERROR_CABIN_CAPACITY_GREATER_THAN_ZERO = "ERROR.CABIN_CAPACITY_GREATER_THAN_ZERO";
    public static String ERROR_CABIN_NOT_FOUND = "ERROR.CABIN_NOT_FOUND";
    public static String ERROR_CABIN_USED_BY_BOOKING = "ERROR.CABIN_USED_BY_BOOKING";

    public CabinExceptions(Response.Status status, String key) {
        super(status, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 409 (Conflict).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link CabinExceptions}
     */
    public static CabinExceptions createConflictException(String key) {
        return new CabinExceptions(Response.Status.CONFLICT, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 406 (Not Acceptable).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link CabinExceptions}
     */
    public static CabinExceptions createNotAcceptableException(String key) {
        return new CabinExceptions(Response.Status.NOT_ACCEPTABLE, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 404 (Not Found).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link CabinExceptions}
     */
    public static CabinExceptions createNotFoundException(String key) {
        return new CabinExceptions(Response.Status.NOT_FOUND, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 410 (Gone).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link CabinExceptions}
     */
    public static CabinExceptions createGoneException(String key) {
        return new CabinExceptions(Response.Status.GONE, key);
    }
}
