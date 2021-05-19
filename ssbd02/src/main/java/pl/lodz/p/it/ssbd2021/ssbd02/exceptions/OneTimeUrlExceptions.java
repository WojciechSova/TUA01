package pl.lodz.p.it.ssbd2021.ssbd02.exceptions;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

@ApplicationException(rollback = true)
public class OneTimeUrlExceptions extends GeneralException {
    public static String ERROR_ONE_TIME_URL_URL_UNIQUE = "ERROR.ONE_TIME_URL_URL_UNIQUE";
    public static String ERROR_ONE_TIME_URL_ACCOUNT_ACTION_TYPE_UNIQUE = "ERROR.ONE_TIME_URL_ACCOUNT_ACTION_TYPE_UNIQUE";
    public static String ERROR_EXPIRE_DATE_IN_FUTURE = "ERROR.EXPIRE_DATE_IN_FUTURE";


    public OneTimeUrlExceptions(Response.Status status, String key) {
        super(status, key);
    }

    public static OneTimeUrlExceptions createExceptionConflict(String key) {
        return new OneTimeUrlExceptions(Response.Status.CONFLICT, key);
    }
}
