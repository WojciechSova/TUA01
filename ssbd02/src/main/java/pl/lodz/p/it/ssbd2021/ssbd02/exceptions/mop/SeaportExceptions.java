package pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

/**
 * Klasa związana z wyjątkami dotyczącymi portu rozszerzająca {@link GeneralException}.
 *
 * @author Wojciech Sowa
 */
@ApplicationException(rollback = true)
public class SeaportExceptions extends GeneralException {
    public static String ERROR_SEAPORT_CITY_UNIQUE = "ERROR.SEAPORT_CITY_UNIQUE";
    public static String ERROR_SEAPORT_CODE_UNIQUE = "ERROR.SEAPORT_CODE_UNIQUE";
    public static String ERROR_SEAPORT_NOT_FOUND = "ERROR.SEAPORT_NOT_FOUND";

    public SeaportExceptions(Response.Status status, String key) {
        super(status, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 409 (Conflict).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link SeaportExceptions}
     */
    public static SeaportExceptions createConflictException(String key) {
        return new SeaportExceptions(Response.Status.CONFLICT, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 404 (Not Found).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link SeaportExceptions}
     */
    public static SeaportExceptions createNotFoundException(String key) {
        return new SeaportExceptions(Response.Status.NOT_FOUND, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 410 (Gone).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link SeaportExceptions}
     */
    public static SeaportExceptions createGoneException(String key) {
        return new SeaportExceptions(Response.Status.GONE, key);
    }
}
