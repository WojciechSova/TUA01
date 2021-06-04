package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.apache.commons.lang3.tuple.Pair;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.CabinFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.FerryFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.FerryManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.TrackerInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.List;
import java.util.Optional;

/**
 * Manager promów
 *
 * @author Wojciech Sowa
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
@Interceptors(TrackerInterceptor.class)
public class FerryManager extends AbstractManager implements FerryManagerLocal, SessionSynchronization {

    @Inject
    FerryFacadeLocal ferryFacadeLocal;

    @Inject
    CabinFacadeLocal cabinFacadeLocal;

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public List<Ferry> getAllFerries() {
        return Optional.ofNullable(ferryFacadeLocal.findAll()).orElseThrow(CommonExceptions::createNoResultException);
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public Ferry getFerryByName(String name) {
        return Optional.ofNullable(ferryFacadeLocal.findByName(name)).orElseThrow(CommonExceptions::createNoResultException);
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public Pair<Ferry, List<Cabin>> getFerryAndCabinsByFerryName(String name) {
        Ferry ferry = Optional.ofNullable(ferryFacadeLocal.findByName(name)).orElseThrow(CommonExceptions::createNoResultException);
        List<Cabin> cabins = Optional.ofNullable(cabinFacadeLocal.findAllByFerry(ferry))
                .orElseThrow(CommonExceptions::createNoResultException);
        return Pair.of(ferry, cabins);
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void createFerry(Ferry ferry) {

    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void updateFerry(Ferry ferry, String modifiedBy) {

    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void removeFerry(Ferry ferry) {

    }
}
