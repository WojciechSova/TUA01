package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccountFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.CabinFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.CabinManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
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
    private AccountFacadeLocal accountFacadeLocal;

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
    public void createCabin(Cabin cabin, String createdBy) {
        Account accCreatedBy = Optional.ofNullable(accountFacadeLocal.findByLogin(createdBy)).orElseThrow(CommonExceptions::createNoResultException);
        cabin.setVersion(0L);
        cabin.setCreatedBy(accCreatedBy);
        cabinFacadeLocal.create(cabin);
        logger.info("The user with login {} has created cabin with code {}",
                createdBy, cabin.getNumber());
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void updateCabin(Cabin cabin, String modifiedBy) {
        Cabin cabinFromDB = Optional.ofNullable(cabinFacadeLocal.findByNumber(cabin.getNumber())).
                orElseThrow(CommonExceptions::createNoResultException);

        cabinFromDB.setVersion(cabin.getVersion());
        if (cabin.getCabinType() != null) {
            cabinFromDB.setCabinType(cabin.getCabinType());
        }
        if (cabin.getCapacity() != null) {
            cabinFromDB.setCapacity(cabin.getCapacity());
        }
        Account cabModifiedBy = Optional.ofNullable(accountFacadeLocal.findByLogin(modifiedBy)).orElseThrow(CommonExceptions::createNoResultException);
        cabinFromDB.setModifiedBy(cabModifiedBy);
        cabinFromDB.setModificationDate(Timestamp.from(Instant.now()));

        cabinFacadeLocal.edit(cabinFromDB);
        logger.info("The user with login {} updated the cabin with number {}",
                this.getInvokerId(), cabinFromDB.getNumber());
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void removeCabin(Cabin cabin) {

    }
}
