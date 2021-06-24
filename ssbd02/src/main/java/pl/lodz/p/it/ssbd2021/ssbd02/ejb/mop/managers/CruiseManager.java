package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.apache.commons.lang3.SerializationUtils;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.*;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.CruiseManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Route;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.CruiseExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.FerryExceptions;
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

/**
 * Manager rejsów
 *
 * @author Wojciech Sowa
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
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

    @Inject
    private CabinFacadeLocal cabinFacade;

    @Inject
    private BookingFacadeLocal bookingFacade;

    @Override
    @PermitAll
    public List<Cruise> getAllCurrentCruises() {
        return cruiseFacadeLocal.findAllFutureDate();
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public Cruise getCruiseByNumber(String number) {
        return cruiseFacadeLocal.findByNumber(number);
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void createCruise(Cruise cruise, String name, String code, String login) {
        Account account = accountMopFacade.findByLogin(login);

        Ferry ferry = ferryFacadeLocal.findByName(name);

        Route route = routeFacadeLocal.findByCode(code);

        List<Cruise> cruises = cruiseFacadeLocal.findAllUsingFerryInTime(ferry, cruise.getStartDate(), cruise.getEndDate());

        if (!cruises.isEmpty()) {
            throw FerryExceptions.createConflictException(FerryExceptions.ERROR_FERRY_IS_BEING_USED);
        }

        cruise.setVersion(0L);
        cruise.setFerry(ferry);
        cruise.setRoute(route);
        cruise.setModificationDate(null);
        cruise.setModifiedBy(null);
        cruise.setCreationDate(Timestamp.from(Instant.now()));
        cruise.setCreatedBy(account);
        cruise.setPopularity(0D);
        cruiseFacadeLocal.create(cruise);
        logger.info("The user with login {} has created cruise with number {}",
                login, cruise.getNumber());
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void updateCruise(Cruise cruise, String modifiedBy) {
        Timestamp modificationDate = Timestamp.from(Instant.now());

        Cruise databaseCruise = cruiseFacadeLocal.findByNumber(cruise.getNumber());

        Cruise cruiseClone = SerializationUtils.clone(databaseCruise);

        if (modificationDate.after(databaseCruise.getStartDate())) {
            throw CommonExceptions.createConstraintViolationException();
        }

        List<Cruise> cruises = cruiseFacadeLocal
                .findAllUsingFerryInTime(databaseCruise.getFerry(), cruise.getStartDate(), cruise.getEndDate());

        if (!cruises.isEmpty() && !(cruises.size() == 1 && cruises.get(0).equals(databaseCruise))) {
            throw FerryExceptions.createConflictException(FerryExceptions.ERROR_FERRY_IS_BEING_USED);
        }

        cruiseClone.setVersion(cruise.getVersion());

        cruiseClone.setStartDate(cruise.getStartDate());
        cruiseClone.setEndDate(cruise.getEndDate());
        cruiseClone.setModifiedBy(accountMopFacade.findByLogin(modifiedBy));
        cruiseClone.setModificationDate(modificationDate);
        cruiseClone.setFerry(databaseCruise.getFerry());
        cruiseClone.setRoute(databaseCruise.getRoute());
        cruiseClone.setCreatedBy(databaseCruise.getCreatedBy());
        cruiseClone.setPopularity(calculatePopularity(cruiseClone));

        cruiseFacadeLocal.edit(cruiseClone);
        logger.info("The user with login {} updated the cruise with number {}",
                modifiedBy, cruiseClone.getNumber());
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

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public double calculatePopularity(Cruise cruise) {

        double taken = cabinFacade.findOccupiedCabinsOnCruise(cruise).stream().mapToInt(Cabin::getCapacity).sum();
        double all = cabinFacade.findCabinsOnCruise(cruise).stream().mapToInt(Cabin::getCapacity).sum();

        taken += bookingFacade.getSumNumberOfPeopleByCruise(cruise);
        all += cruise.getFerry().getOnDeckCapacity();

        double popularity = taken / all * 100;

        if (popularity > 100) {
            return 100;
        }
        return popularity;

    }
}
