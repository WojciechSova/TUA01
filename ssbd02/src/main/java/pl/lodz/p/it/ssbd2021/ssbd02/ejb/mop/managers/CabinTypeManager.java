package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.CabinTypeFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.CabinTypeManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.CabinType;
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
 * Manager typów kajut
 *
 * @author Wojciech Sowa
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
@Interceptors(TrackerInterceptor.class)
public class CabinTypeManager extends AbstractManager implements CabinTypeManagerLocal, SessionSynchronization {

    @Inject
    private CabinTypeFacadeLocal cabinTypeFacadeLocal;

    @Override
    @RolesAllowed({"CLIENT", "EMPLOYEE"})
    public List<CabinType> getAllCabinTypes() {
        return null;
    }

    @Override
    @RolesAllowed({"CLIENT", "EMPLOYEE"})
    public CabinType getCabinTypeByName(String name){
        return Optional.ofNullable(cabinTypeFacadeLocal.findByName(name)).orElseThrow(CommonExceptions::createNoResultException);
    }
}
