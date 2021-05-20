package pl.lodz.p.it.ssbd2021.ssbd02.exceptions;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

/**
 * Klasa związana z wyjątkami dotyczącymi kont rozszerzająca {@link GeneralException}.
 *
 * @author Wojciech Sowa
 */
@ApplicationException(rollback = true)
public class AccountExceptions extends GeneralException {
    public static String ERROR_LOGIN_UNIQUE = "ERROR.LOGIN_UNIQUE";
    public static String ERROR_EMAIL_UNIQUE = "ERROR.EMAIL_UNIQUE";
    public static String ERROR_PHONE_NUMBER_UNIQUE = "ERROR.PHONE_NUMBER_UNIQUE";

    public AccountExceptions(Response.Status status, String key) {
        super(status, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 409 (Conflict).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link AccountExceptions}
     */
    public static AccountExceptions createExceptionConflict(String key) {
        return new AccountExceptions(Response.Status.CONFLICT, key);
    }
}
