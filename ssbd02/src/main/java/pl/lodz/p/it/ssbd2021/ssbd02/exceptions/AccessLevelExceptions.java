package pl.lodz.p.it.ssbd2021.ssbd02.exceptions;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

/**
 * Klasa związana z wyjątkami dotyczącymi poziomów dostępu rozszerzająca {@link GeneralException}.
 *
 * @author Wojciech Sowa
 */
@ApplicationException(rollback = true)
public class AccessLevelExceptions extends GeneralException {
    public static String ERROR_ACCOUNT_LEVEL_UNIQUE = "ERROR.ACCOUNT_LEVEL_UNIQUE";
    public static String ERROR_NO_ACCESS_LEVEL = "ERROR.NO_ACCESS_LEVEL";

    public AccessLevelExceptions(Response.Status status, String key) {
        super(status, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 409 (Conflict).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link AccessLevelExceptions}
     */
    public static AccessLevelExceptions createConflictException(String key) {
        return new AccessLevelExceptions(Response.Status.CONFLICT, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 406 (Not Acceptable).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link AccessLevelExceptions}
     */
    public static AccessLevelExceptions createNotAcceptableException(String key) {
        return new AccessLevelExceptions(Response.Status.NOT_ACCEPTABLE, key);
    }
}
