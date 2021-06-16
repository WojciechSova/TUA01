package pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

/**
 * Klasa związana z wyjątkami dotyczącymi trasy rozszerzająca {@link GeneralException}.
 *
 * @author Wojciech Sowa
 */
@ApplicationException(rollback = true)
public class RouteExceptions extends GeneralException {
    public static String ERROR_ROUTE_START_DESTINATION_UNIQUE = "ERROR.ROUTE_START_DESTINATION_UNIQUE";
    public static String ERROR_ROUTE_CODE_UNIQUE = "ERROR.ROUTE_CODE_UNIQUE";
    public static String ERROR_ROUTE_NOT_FOUND = "ERROR.ROUTE_NOT_FOUND";
    public static String ERROR_ROUTE_USED_BY_CRUISE = "ERROR.ROUTE_USER_BY_CRUISE";

    public RouteExceptions(Response.Status status, String key) {
        super(status, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 409 (Conflict).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link RouteExceptions}
     */
    public static RouteExceptions createConflictException(String key) {
        return new RouteExceptions(Response.Status.CONFLICT, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 406 (Not Acceptable).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link RouteExceptions}
     */
    public static RouteExceptions createNotAcceptableException(String key) {
        return new RouteExceptions(Response.Status.NOT_ACCEPTABLE, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 404 (Not Found).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link RouteExceptions}
     */
    public static RouteExceptions createNotFoundException(String key) {
        return new RouteExceptions(Response.Status.NOT_FOUND, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 410 (Gone).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link RouteExceptions}
     */
    public static RouteExceptions createGoneException(String key) {
        return new RouteExceptions(Response.Status.GONE, key);
    }
}
