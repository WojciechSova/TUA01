package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.apache.commons.lang3.SerializationUtils;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.AccountMopFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.SeaportFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.SeaportManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Seaport;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.SeaportExceptions;
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
import java.util.Optional;

/**
 * Manager portów
 *
 * @author Wojciech Sowa
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
@Interceptors(TrackerInterceptor.class)
public class SeaportManager extends AbstractManager implements SeaportManagerLocal, SessionSynchronization {

    @Inject
    private SeaportFacadeLocal seaportFacadeLocal;

    @Inject
    private AccountMopFacadeLocal accountMopFacadeLocal;

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public List<Seaport> getAllSeaports() {
        return Optional.ofNullable(seaportFacadeLocal.findAll()).orElseThrow(CommonExceptions::createNoResultException);
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public Seaport getSeaportByCode(String code) {
        return Optional.ofNullable(seaportFacadeLocal.findByCode(code)).orElseThrow(CommonExceptions::createNoResultException);
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void createSeaport(String login, Seaport seaport) {
        seaport.setVersion(0L);
        seaportFacadeLocal.create(seaport);
        logger.info("The user with login {} has created seaport with code {}",
                login, seaport.getCode());
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void updateSeaport(Seaport seaport, String modifiedBy) {

        Seaport databaseSeaport = Optional.ofNullable(seaportFacadeLocal.findByCode(seaport.getCode()))
                .orElseThrow(CommonExceptions::createNoResultException);
        Seaport seaportClone = SerializationUtils.clone(databaseSeaport);
        seaportClone.setVersion(seaport.getVersion());

        seaportClone.setCity(seaport.getCity());

        seaportClone.setModifiedBy(Optional.ofNullable(accountMopFacadeLocal.findByLogin(modifiedBy))
                .orElseThrow(CommonExceptions::createNoResultException));
        seaportClone.setModificationDate(Timestamp.from(Instant.now()));

        seaportClone.setCreatedBy(databaseSeaport.getCreatedBy());

        seaportFacadeLocal.edit(seaportClone);
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void removeSeaport(String seaportCode, String userLogin) {
        try {
            Seaport seaport = seaportFacadeLocal.findByCode(seaportCode);
            seaportFacadeLocal.remove(seaport);
            logger.info("The employee with login {} has deleted seaport with code {}",
                    userLogin, seaportCode);
        } catch (CommonExceptions ce) {
            if (ce.getResponse().getStatus() == Response.Status.BAD_REQUEST.getStatusCode()
                && ce.getResponse().getEntity().equals(CommonExceptions.ERROR_CONSTRAINT_VIOLATION)){
                throw SeaportExceptions.createConflictException(SeaportExceptions.ERROR_SEAPORT_USED_BY_ROUTE);
            }
        }

    }
}
