package pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

/**
 * Klasa związana z wyjątkami dotyczącymi typu pojazdu rozszerzająca {@link GeneralException}.
 *
 * @author Wojciech Sowa
 */
@ApplicationException(rollback = true)
public class VehicleTypeExceptions extends GeneralException {
    public static String ERROR_VEHICLE_TYPE_NOT_FOUND = "ERROR.VEHICLE_TYPE_NOT_FOUND";

    public VehicleTypeExceptions(Response.Status status, String key) {
        super(status, key);
    }

    /**
     * Metoda tworząca wyjątek aplikacyjny o kodzie 404 (Not Found).
     *
     * @param key klucz typu {@link String}
     * @return wyjątek typu {@link VehicleTypeExceptions}
     */
    public static VehicleTypeExceptions createNotFoundException(String key) {
        return new VehicleTypeExceptions(Response.Status.NOT_FOUND, key);
    }

}
