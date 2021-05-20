package pl.lodz.p.it.ssbd2021.ssbd02.exceptions;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

/**
 * Klasa związana z wyjątkami ogólnymi rozszerzająca {@link GeneralException}.
 *
 * @author Wojciech Sowa
 */
@ApplicationException(rollback = true)
public class CommonExceptions extends GeneralException {
    protected final static String ERROR_OPTIMISTIC_LOCK = "ERROR.OPTIMISTIC_LOCK";
    protected final static String ERROR_UNKNOWN = "ERROR.UNKNOWN";
    protected final static String ERROR_NO_RESULT = "ERROR.NO_RESULT";
    protected final static String ERROR_PRECONDITION_FAILED = "ERROR.PRECONDITION_FAILED";
    protected final static String ERROR_ACCESS_DENIED = "ERROR.ACCESS_DENIED";
    protected final static String ERROR_JDBC_CONNECTION = "ERROR.JDBC_CONNECTION";

    protected CommonExceptions(Response.Status status, String key) {
        super(status, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 409 (Conflict) dotyczący blokady optymistycznej.
     *
     * @return wyjątek typu {@link CommonExceptions}
     */
    public static CommonExceptions createOptimisticLockException() {
        return new CommonExceptions(Response.Status.CONFLICT, ERROR_OPTIMISTIC_LOCK);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 410 (Gone) dotyczący braku znalezienia rekordu.
     *
     * @return wyjątek typu {@link CommonExceptions}
     */
    public static CommonExceptions createNoResultException() {
        return new CommonExceptions(Response.Status.GONE, ERROR_NO_RESULT);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 503 (Service Unavailable) dotyczący braku połączenia z bazą danych.
     *
     * @return wyjątek typu {@link CommonExceptions}
     */
    public static CommonExceptions createJDBCConnectionException() {
        return new CommonExceptions(Response.Status.SERVICE_UNAVAILABLE, ERROR_JDBC_CONNECTION);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 412 (Precondition Failed) dotyczący braku spełnienia początkowych założeń.
     *
     * @return wyjątek typu {@link CommonExceptions}
     */
    public static CommonExceptions createPreconditionFailedException() {
        return new CommonExceptions(Response.Status.PRECONDITION_FAILED, ERROR_PRECONDITION_FAILED);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 403 (Forbidden) dotyczący braku dostępu do zasobu.
     *
     * @return wyjątek typu {@link CommonExceptions}
     */
    public static CommonExceptions createForbiddenException() {
        return new CommonExceptions(Response.Status.FORBIDDEN, ERROR_ACCESS_DENIED);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 500 (Internal Server Error) dotyczący wszystkich nieprzewidzianych sytuacji.
     *
     * @return wyjątek typu {@link CommonExceptions}
     */
    public static CommonExceptions createUnknownException() {
        return new CommonExceptions(Response.Status.INTERNAL_SERVER_ERROR, ERROR_UNKNOWN);
    }
}
