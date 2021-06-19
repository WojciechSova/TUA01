package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.AccountMopFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.AccountMopFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.CruiseFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.FerryFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.RouteFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.CruiseManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Route;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.CruiseExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.TrackerInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

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

    @Inject
    private CruiseFacadeLocal cruiseFacadeLocal;

    @Inject
    private RouteFacadeLocal routeFacadeLocal;

    @Inject
    private FerryFacadeLocal ferryFacadeLocal;

    @Inject
    private AccountMopFacadeLocal accountMopFacade;

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public List<Cruise> getAllCruises() {
        return null;
    }

    @Override
    @PermitAll
    public List<Cruise> getAllCurrentCruises() {
        return Optional.ofNullable(cruiseFacadeLocal.findAllFutureDate()).orElseThrow(CommonExceptions::createNoResultException);
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public List<Cruise> getAllCompletedCruises() {
        return null;
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public Cruise getCruiseByNumber(String number) {
        return cruiseFacadeLocal.findByNumber(number);
    }


    @Override
    @RolesAllowed({"EMPLOYEE"})
    public List<Cruise> getCruisesByFerryName(String name) {
        return null;
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void createCruise(Cruise cruise, String name, String code, String login) {
        Account account = Optional.ofNullable(accountMopFacade.findByLogin(login))
                .orElseThrow(CommonExceptions::createNoResultException);

        Ferry ferry = Optional.ofNullable(ferryFacadeLocal.findByName(name))
                .orElseThrow(CommonExceptions::createNoResultException);

        Route route = Optional.ofNullable(routeFacadeLocal.findByCode(code))
                .orElseThrow(CommonExceptions::createNoResultException);

        cruise.setVersion(0L);
        cruise.setFerry(ferry);
        cruise.setRoute(route);
        cruise.setModificationDate(null);
        cruise.setModifiedBy(null);
        cruise.setCreationDate(Timestamp.from(Instant.now()));
        cruise.setCreatedBy(account);
        cruiseFacadeLocal.create(cruise);
        logger.info("The user with login {} has created cruise with number {}",
                login, cruise.getNumber());
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void updateCruise(Cruise cruise, String modifiedBy) {

    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void removeCruise(String cruiseNumber, String userLogin) {
        Cruise cruise = cruiseFacadeLocal.findByNumber(cruiseNumber);

        if (cruise.getStartDate().before(Timestamp.from(Instant.now()))) {
            throw CruiseExceptions.createConflictException(CruiseExceptions.ERROR_CRUISE_ALREADY_STARTED);
        }

        cruiseFacadeLocal.remove(cruise);

        logger.info("The user with login {} has removed the cruise with number {}",
                userLogin, cruiseNumber);
    }
}
