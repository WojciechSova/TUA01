package pl.lodz.p.it.ssbd2021.ssbd02.exceptions;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

@ApplicationException(rollback = true)
public class OneTimeUrlExceptions extends GeneralException {
    public static String ERROR__UNIQUE = "ERROR._UNIQUE";


    public OneTimeUrlExceptions(Response.Status status, String key) {
        super(status, key);
    }

    public static OneTimeUrlExceptions createExceptionConflict(String key) {
        return new OneTimeUrlExceptions(Response.Status.CONFLICT, key);
    }
}
