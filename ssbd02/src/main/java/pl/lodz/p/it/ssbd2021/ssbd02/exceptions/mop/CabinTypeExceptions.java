package pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

/**
 * Klasa związana z wyjątkami dotyczącymi typów kabin rozszerzająca {@link GeneralException}.
 *
 * @author Wojciech Sowa
 */
@ApplicationException(rollback = true)
public class CabinTypeExceptions extends GeneralException {
    public static String ERROR_CABIN_TYPE_NOT_FOUND = "ERROR.CABIN_TYPE_NOT_FOUND";

    public CabinTypeExceptions(Response.Status status, String key) {
        super(status, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 404 (Not Found).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link CabinTypeExceptions}
     */
    public static CabinTypeExceptions createNotFoundException(String key) {
        return new CabinTypeExceptions(Response.Status.NOT_FOUND, key);
    }
}
