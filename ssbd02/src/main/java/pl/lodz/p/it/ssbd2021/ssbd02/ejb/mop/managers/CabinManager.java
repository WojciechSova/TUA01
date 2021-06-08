package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.CabinFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.CabinManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.TrackerInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.List;

/**
 * Manager kajut
 *
 * @author Wojciech Sowa
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
@Interceptors(TrackerInterceptor.class)
public class CabinManager extends AbstractManager implements CabinManagerLocal, SessionSynchronization {

    @Inject
    CabinFacadeLocal cabinFacadeLocal;

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public List<Cabin> getAllCabins() {
        return null;
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public List<Cabin> getAllCabinsByFerryCode(String code) {
        return null;
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public List<Cabin> getAllCabinsByFerryCodeAndCabinType(String code, String cabinType) {
        return null;
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public Cabin getCabinByNumber(String number) {
        return cabinFacadeLocal.findByNumber(number);
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void createCabin(Cabin cabin) {

    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void updateCabin(Cabin cabin, String modifiedBy) {

    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void removeCabin(Cabin cabin) {

    }
}
