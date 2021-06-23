package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.VehicleTypeManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.VehicleType;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.TrackerInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.List;

/**
 * Manager typów pojazdów
 *
 * @author Wojciech Sowa
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
@Interceptors(TrackerInterceptor.class)
public class VehicleTypeManager extends AbstractManager implements VehicleTypeManagerLocal, SessionSynchronization {

    @Override
    @RolesAllowed({"CLIENT", "EMPLOYEE"})
    public List<VehicleType> getAllVehicleTypes() {
        return null;
    }
}
