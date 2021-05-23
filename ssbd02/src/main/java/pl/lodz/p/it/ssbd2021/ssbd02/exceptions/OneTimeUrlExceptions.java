package pl.lodz.p.it.ssbd2021.ssbd02.exceptions;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

/**
 * Klasa związana z wyjątkami dotyczącymi jednokrotnych linków rozszerzająca {@link GeneralException}.
 *
 * @author Wojciech Sowa
 */
@ApplicationException(rollback = true)
public class OneTimeUrlExceptions extends GeneralException {
    public static String ERROR_ONE_TIME_URL_URL_UNIQUE = "ERROR.ONE_TIME_URL_URL_UNIQUE";
    public static String ERROR_ONE_TIME_URL_ACCOUNT_ACTION_TYPE_UNIQUE = "ERROR.ONE_TIME_URL_ACCOUNT_ACTION_TYPE_UNIQUE";
    public static String ERROR_EXPIRE_DATE_IN_FUTURE = "ERROR.EXPIRE_DATE_IN_FUTURE";
    public static String ERROR_NEW_EMAIL_UNIQUE = "ERROR.ONE_TIME_URL_NEW_EMAIL_UNIQUE";


    public OneTimeUrlExceptions(Response.Status status, String key) {
        super(status, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 409 (Conflict).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link OneTimeUrlExceptions}
     */
    public static OneTimeUrlExceptions createExceptionConflict(String key) {
        return new OneTimeUrlExceptions(Response.Status.CONFLICT, key);
    }
}
