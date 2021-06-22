package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.tuple.Pair;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.*;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.FerryManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.TrackerInterceptor;

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
import java.util.stream.Collectors;

/**
 * Manager promów
 *
 * @author Wojciech Sowa
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
@Interceptors(TrackerInterceptor.class)
public class FerryManager extends AbstractManager implements FerryManagerLocal, SessionSynchronization {

    @Inject
    private FerryFacadeLocal ferryFacadeLocal;

    @Inject
    private CabinFacadeLocal cabinFacadeLocal;

    @Inject
    private AccountMopFacadeLocal accountMopFacadeLocal;

    @Inject
    private BookingFacadeLocal bookingFacadeLocal;

    @Inject
    private CruiseFacadeLocal cruiseFacadeLocal;

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public List<Ferry> getAllFerries() {
        return ferryFacadeLocal.findAll();
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public Ferry getFerryByName(String name) {
        return ferryFacadeLocal.findByName(name);
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public Pair<Ferry, List<Cabin>> getFerryAndCabinsByFerryName(String name) {
        Ferry ferry = ferryFacadeLocal.findByName(name);
        List<Cabin> cabins = cabinFacadeLocal.findAllByFerry(ferry);

        return Pair.of(ferry, cabins);
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void createFerry(String login, Ferry ferry) {
        ferry.setVersion(0L);
        ferry.setCreatedBy(accountMopFacadeLocal.findByLogin(login));
        ferryFacadeLocal.create(ferry);
        logger.info("The user with login {} created ferry with name {}",
                login, ferry.getName());
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void updateFerry(Ferry ferry, String modifiedBy) {
        Ferry ferryFromDB = ferryFacadeLocal.findByName(ferry.getName());

        Ferry ferryClone = SerializationUtils.clone(ferryFromDB);

        ferryClone.setVersion(ferry.getVersion());
        if (ferry.getOnDeckCapacity() != null) {
            ferryClone.setOnDeckCapacity(ferry.getOnDeckCapacity());
        }
        if (ferry.getVehicleCapacity() != null) {
            ferryClone.setVehicleCapacity(ferry.getVehicleCapacity());
        }
        ferryClone.setModifiedBy(accountMopFacadeLocal.findByLogin(modifiedBy));
        ferryClone.setModificationDate(Timestamp.from(Instant.now()));
        ferryClone.setCreatedBy(ferryFromDB.getCreatedBy());

        ferryFacadeLocal.edit(ferryClone);

        List<Cruise> cruisesWithFerry = cruiseFacadeLocal.findAll().stream()
                .filter(cr -> cr.getFerry().getName().equals(ferryClone.getName())).collect(Collectors.toList());

        for (Cruise cruise: cruisesWithFerry) {
            if (cruise.getStartDate().compareTo(Timestamp.from(Instant.now())) > 0) {
                cruise.setPopularity(calculatePopularity(cruise));
                cruiseFacadeLocal.edit(cruise);
                logger.info("The popularity of cruise {} has been recalculated",
                        cruise.getNumber());
            }
        }

        logger.info("The user with login {} updated the ferry with name {}",
                this.getInvokerId(), ferryFromDB.getName());
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void removeFerry(String ferryName, String login) {
        Ferry ferry = ferryFacadeLocal.findByName(ferryName);
        ferryFacadeLocal.remove(ferry);

        logger.info("The user with login {} removed the ferry with name {}",
                login, ferry.getName());
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
