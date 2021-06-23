package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.apache.commons.lang3.SerializationUtils;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.*;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.CabinManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.CabinExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.TrackerInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manager kajut
 *
 * @author Wojciech Sowa
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
@Interceptors(TrackerInterceptor.class)
public class CabinManager extends AbstractManager implements CabinManagerLocal, SessionSynchronization {

    @Inject
    private CabinFacadeLocal cabinFacadeLocal;
    @Inject
    private AccountMopFacadeLocal accountMopFacadeLocal;
    @Inject
    private FerryFacadeLocal ferryFacadeLocal;
    @Inject
    private CruiseFacadeLocal cruiseFacadeLocal;
    @Inject
    private BookingFacadeLocal bookingFacadeLocal;

    @Override
    @RolesAllowed({"CLIENT"})
    public List<Cabin> getFreeCabinsOnCruise(String number) {
        Cruise cruise = cruiseFacadeLocal.findByNumber(number);
        List<Cabin> occupiedCabins = cabinFacadeLocal.findOccupiedCabinsOnCruise(cruise);
        return cabinFacadeLocal.findCabinsOnCruise(cruise).stream()
                .filter(c -> !occupiedCabins.contains(c))
                .collect(Collectors.toList());
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public Cabin getCabinByFerryAndNumber(String ferryName, String cabinNumber) {
        Ferry ferry = ferryFacadeLocal.findByName(ferryName);
        return cabinFacadeLocal.findByFerryAndNumber(ferry, cabinNumber);
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void createCabin(Cabin cabin, String createdBy, String ferryName) {
        Account accCreatedBy = accountMopFacadeLocal.findByLogin(createdBy);
        Ferry ferry = ferryFacadeLocal.findByName(ferryName);
        cabin.setVersion(0L);
        cabin.setCreatedBy(accCreatedBy);
        cabin.setFerry(ferry);
        cabinFacadeLocal.create(cabin);

        List<Cruise> cruisesWithFerryFromCabin = cruiseFacadeLocal.findAll().stream()
                .filter(cr -> cr.getFerry().getName().equals(ferryName)).collect(Collectors.toList());

        for (Cruise cruise: cruisesWithFerryFromCabin) {
            if (cruise.getStartDate().compareTo(Timestamp.from(Instant.now())) > 0) {
                cruise.setPopularity(calculatePopularity(cruise));
                cruiseFacadeLocal.edit(cruise);
                logger.info("The popularity of cruise {} has been recalculated",
                        cruise.getNumber());
            }
        }

        logger.info("The user with login {} has created cabin with code {}",
                createdBy, cabin.getNumber());
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void updateCabin(Cabin cabin, String modifiedBy, String ferryName) {
        Ferry ferry = ferryFacadeLocal.findByName(ferryName);
        Cabin cabinFromDB = cabinFacadeLocal.findByFerryAndNumber(ferry, cabin.getNumber());
        Cabin cab = SerializationUtils.clone(cabinFromDB);

        cab.setVersion(cabin.getVersion());
        if (cabin.getCabinType() != null) {
            cab.setCabinType(cabin.getCabinType());
        }
        if (cabin.getCapacity() != null) {
            cab.setCapacity(cabin.getCapacity());
        }
        Account cabModifiedBy = accountMopFacadeLocal.findByLogin(modifiedBy);
        cab.setModifiedBy(cabModifiedBy);
        cab.setModificationDate(Timestamp.from(Instant.now()));
        cab.setCreatedBy(cabinFromDB.getCreatedBy());
        cab.setFerry(ferry);
        cabinFacadeLocal.edit(cab);

        List<Cruise> cruisesWithFerryFromCabin = cruiseFacadeLocal.findAll().stream()
                .filter(cr -> cr.getFerry().getName().equals(ferryName)).collect(Collectors.toList());

        for (Cruise cruise: cruisesWithFerryFromCabin) {
            if (cruise.getStartDate().compareTo(Timestamp.from(Instant.now())) > 0) {
                cruise.setPopularity(calculatePopularity(cruise));
                cruiseFacadeLocal.edit(cruise);
                logger.info("The popularity of cruise {} has been recalculated",
                        cruise.getNumber());
            }
        }

        logger.info("The user with login {} updated the cabin with number {}",
                this.getInvokerId(), cabinFromDB.getNumber());
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void removeCabin(String number, String removedBy) {
        try {
            Cabin cabin = cabinFacadeLocal.findByNumber(number);

            cabinFacadeLocal.remove(cabin);

            List<Cruise> cruisesWithFerryFromCabin = cruiseFacadeLocal.findAll().stream()
                    .filter(cr -> cr.getFerry().getName().equals(cabin.getFerry().getName())).collect(Collectors.toList());

            for (Cruise cruise: cruisesWithFerryFromCabin) {
                if (cruise.getStartDate().compareTo(Timestamp.from(Instant.now())) > 0) {
                    cruise.setPopularity(calculatePopularity(cruise));
                    cruiseFacadeLocal.edit(cruise);
                    logger.info("The popularity of cruise {} has been recalculated",
                            cruise.getNumber());
                }
            }

            logger.info("The user with login {} has deleted cabin with number {}",
                    removedBy, number);

        } catch (CommonExceptions ce) {
            if (ce.getResponse().getStatus() == Response.Status.BAD_REQUEST.getStatusCode() &&
                    ce.getResponse().getEntity().equals(CommonExceptions.ERROR_CONSTRAINT_VIOLATION)) {
                throw CabinExceptions.createConflictException(CabinExceptions.ERROR_CABIN_USED_BY_BOOKING);
            } else {
                throw ce;
            }
        }
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public double calculatePopularity(Cruise cruise) {

        double taken = cabinFacadeLocal.findOccupiedCabinsOnCruise(cruise).stream().mapToInt(Cabin::getCapacity).sum();
        double all = cabinFacadeLocal.findCabinsOnCruise(cruise).stream().mapToInt(Cabin::getCapacity).sum();

        taken += bookingFacadeLocal.getSumNumberOfPeopleByCruise(cruise);
        all += cruise.getFerry().getOnDeckCapacity();

        double popularity = taken / all * 100;

        if (popularity > 100) {
            return 100;
        }
        return popularity;
    }
}
