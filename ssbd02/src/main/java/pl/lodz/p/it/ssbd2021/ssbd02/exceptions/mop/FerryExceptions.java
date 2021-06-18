package pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

/**
 * Klasa związana z wyjątkami dotyczącymi promu rozszerzająca {@link GeneralException}.
 *
 * @author Wojciech Sowa
 */
@ApplicationException(rollback = true)
public class FerryExceptions extends GeneralException {
    public static String ERROR_FERRY_NAME_UNIQUE = "ERROR.FERRY_NAME_UNIQUE";
    public static String ERROR_FERRY_IS_BEING_USED = "ERROR.FERRY_IS_BEING_USED";
    public static String ERROR_FERRY_VEHICLE_CAPACITY_NOT_NEGATIVE = "ERROR.FERRY_VEHICLE_CAPACITY_NOT_NEGATIVE";
    public static String ERROR_FERRY_ON_DECK_CAPACITY_GREATER_THAN_ZERO = "ERROR.FERRY_ON_DECK_CAPACITY_GREATER_THAN_ZERO";
    public static String ERROR_FERRY_NOT_FOUND = "ERROR.FERRY_NOT_FOUND";

    public FerryExceptions(Response.Status status, String key) {
        super(status, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 409 (Conflict).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link FerryExceptions}
     */
    public static FerryExceptions createConflictException(String key) {
        return new FerryExceptions(Response.Status.CONFLICT, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 406 (Not Acceptable).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link FerryExceptions}
     */
    public static FerryExceptions createNotAcceptableException(String key) {
        return new FerryExceptions(Response.Status.NOT_ACCEPTABLE, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 404 (Not Found).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link FerryExceptions}
     */
    public static FerryExceptions createNotFoundException(String key) {
        return new FerryExceptions(Response.Status.NOT_FOUND, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 410 (Gone).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link FerryExceptions}
     */
    public static FerryExceptions createGoneException(String key) {
        return new FerryExceptions(Response.Status.GONE, key);
    }
}
