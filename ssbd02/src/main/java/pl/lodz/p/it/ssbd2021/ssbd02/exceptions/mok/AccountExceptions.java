package pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mok;

import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;

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
    public static String ERROR_EMAIL_NOT_FOUND = "ERROR.EMAIL_NOT_FOUND";
    public static String ERROR_PHONE_NUMBER_UNIQUE = "ERROR.PHONE_NUMBER_UNIQUE";
    public static String ERROR_SAME_PASSWORD = "ERROR.SAME_PASSWORD";
    public static String ERROR_PASSWORD_NOT_CORRECT = "ERROR.PASSWORD_NOT_CORRECT";
    public static String ERROR_URL_NOT_FOUND = "ERROR.URL_NOT_FOUND";
    public static String ERROR_URL_EXPIRED = "ERROR.URL_EXPIRED";
    public static String ERROR_URL_TYPE = "ERROR.URL_TYPE";
    public static String ERROR_ACCOUNT_INACTIVE = "ERROR.ACCOUNT_INACTIVE";

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

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 400 (Bad Request).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link AccountExceptions}
     */
    public static AccountExceptions createBadRequestException(String key) {
        return new AccountExceptions(Response.Status.BAD_REQUEST, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 404 (Not Found).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link AccountExceptions}
     */
    public static AccountExceptions createNotFoundException(String key) {
        return new AccountExceptions(Response.Status.NOT_FOUND, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 410 (Gone).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link AccountExceptions}
     */
    public static AccountExceptions createGoneException(String key) {
        return new AccountExceptions(Response.Status.GONE, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 403 (Forbidden).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link AccountExceptions}
     */
    public static AccountExceptions createForbiddenException(String key) {
        return new AccountExceptions(Response.Status.FORBIDDEN, key);
    }
}
