package pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

/**
 * Klasa związana z wyjątkami dotyczącymi rejsu rozszerzająca {@link GeneralException}.
 *
 * @author Wojciech Sowa
 */
@ApplicationException(rollback = true)
public class CruiseExceptions extends GeneralException {
    public static String ERROR_CRUISE_NUMBER_UNIQUE = "ERROR.CRUISE_NUMBER_UNIQUE";
    public static String ERROR_CRUISE_END_DATE_AFTER_START_DATE = "ERROR.CRUISE_END_DATE_AFTER_START_DATE";
    public static String ERROR_CRUISE_NOT_FOUND = "ERROR.CRUISE_NOT_FOUND";
    public static String ERROR_CRUISE_IS_BEING_USED = "ERROR.CRUISE_IS_BEING_USED";
    public static String ERROR_CRUISE_ALREADY_STARTED = "ERROR.CRUISE_ALREADY_STARTED";

    public CruiseExceptions(Response.Status status, String key) {
        super(status, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 409 (Conflict).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link CruiseExceptions}
     */
    public static CruiseExceptions createConflictException(String key) {
        return new CruiseExceptions(Response.Status.CONFLICT, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 406 (Not Acceptable).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link CruiseExceptions}
     */
    public static CruiseExceptions createNotAcceptableException(String key) {
        return new CruiseExceptions(Response.Status.NOT_ACCEPTABLE, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 404 (Not Found).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link CruiseExceptions}
     */
    public static CruiseExceptions createNotFoundException(String key) {
        return new CruiseExceptions(Response.Status.NOT_FOUND, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 410 (Gone).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link CruiseExceptions}
     */
    public static CruiseExceptions createGoneException(String key) {
        return new CruiseExceptions(Response.Status.GONE, key);
    }
}
