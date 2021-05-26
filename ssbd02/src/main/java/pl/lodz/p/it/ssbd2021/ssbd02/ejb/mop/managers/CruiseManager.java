package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.CruiseManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.TrackerInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.List;

/**
 * Manager rejsów
 *
 * @author Wojciech Sowa
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
@Interceptors(TrackerInterceptor.class)
public class CruiseManager extends AbstractManager implements CruiseManagerLocal, SessionSynchronization {

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public List<Cruise> getAllCruises() {
        return null;
    }

    @Override
    @PermitAll
    public List<Cruise> getAllCurrentCruises() {
        return null;
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public List<Cruise> getAllCompletedCruises() {
        return null;
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public Cruise getCruiseByNumber(String number) {
        return null;
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public List<Cruise> getCruisesByRouteCode(String code) {
        return null;
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public List<Cruise> getCruisesByFerryName(String name) {
        return null;
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void createCruise(Cruise cruise) {

    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void updateCruise(Cruise cruise, String modifiedBy) {

    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void removeCruise(Cruise cruise) {

    }
}
