package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.apache.commons.lang3.SerializationUtils;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.AccountMopFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.CabinFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.CruiseFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.FerryFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.CabinManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
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
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private CabinFacadeLocal cabinFacadeLocal;
    @Inject
    private AccountMopFacadeLocal accountMopFacadeLocal;
    @Inject
    private FerryFacadeLocal ferryFacadeLocal;
    @Inject
    private CruiseFacadeLocal cruiseFacadeLocal;

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
    @RolesAllowed({"CLIENT"})
    public List<Cabin> getFreeCabinsOnCruise(String number) {
        Cruise cruise = cruiseFacadeLocal.findByNumber(number);
        return cabinFacadeLocal.findCabinsOnCruise(cruise).stream()
                .filter(c -> !cabinFacadeLocal.findOccupiedCabinsOnCruise(cruise).contains(c))
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
        Account accCreatedBy = Optional.ofNullable(accountMopFacadeLocal.findByLogin(createdBy)).orElseThrow(CommonExceptions::createNoResultException);
        Ferry ferry = Optional.ofNullable(ferryFacadeLocal.findByName(ferryName)).orElseThrow(CommonExceptions::createNoResultException);
        cabin.setVersion(0L);
        cabin.setCreatedBy(accCreatedBy);
        cabin.setFerry(ferry);
        cabinFacadeLocal.create(cabin);
        logger.info("The user with login {} has created cabin with code {}",
                createdBy, cabin.getNumber());
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void updateCabin(Cabin cabin, String modifiedBy) {
        Cabin cabinFromDB = Optional.ofNullable(cabinFacadeLocal.findByNumber(cabin.getNumber())).
                orElseThrow(CommonExceptions::createNoResultException);
        Cabin cab = SerializationUtils.clone(cabinFromDB);

        cab.setVersion(cabin.getVersion());
        if (cabin.getCabinType() != null) {
            cab.setCabinType(cabin.getCabinType());
        }
        if (cabin.getCapacity() != null) {
            cab.setCapacity(cabin.getCapacity());
        }
        Account cabModifiedBy = Optional.ofNullable(accountMopFacadeLocal.findByLogin(modifiedBy)).orElseThrow(CommonExceptions::createNoResultException);
        cab.setModifiedBy(cabModifiedBy);
        cab.setModificationDate(Timestamp.from(Instant.now()));

        cabinFacadeLocal.edit(cab);
        logger.info("The user with login {} updated the cabin with number {}",
                this.getInvokerId(), cabinFromDB.getNumber());
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void removeCabin(Cabin cabin) {

    }
}
