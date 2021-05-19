package pl.lodz.p.it.ssbd2021.ssbd02.exceptions;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

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

    public static CommonExceptions createOptimisticLockException() {
        return new CommonExceptions(Response.Status.CONFLICT, ERROR_OPTIMISTIC_LOCK);
    }

    public static CommonExceptions createNoResultException() {
        return new CommonExceptions(Response.Status.GONE, ERROR_NO_RESULT);
    }

    public static CommonExceptions createJDBCConnectionException() {
        return new CommonExceptions(Response.Status.SERVICE_UNAVAILABLE, ERROR_JDBC_CONNECTION);
    }

    public static CommonExceptions createPreconditionFailedException() {
        return new CommonExceptions(Response.Status.PRECONDITION_FAILED, ERROR_PRECONDITION_FAILED);
    }

    public static CommonExceptions createForbiddenException() {
        return new CommonExceptions(Response.Status.FORBIDDEN, ERROR_ACCESS_DENIED);
    }

    public static CommonExceptions createUnknownException() {
        return new CommonExceptions(Response.Status.INTERNAL_SERVER_ERROR, ERROR_UNKNOWN);
    }
}
