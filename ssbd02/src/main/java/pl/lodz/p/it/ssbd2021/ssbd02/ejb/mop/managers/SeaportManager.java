package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.SeaportManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Seaport;

import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

/**
 * Manager portów
 *
 * @author Wojciech Sowa
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
public class SeaportManager extends AbstractManager implements SeaportManagerLocal, SessionSynchronization {

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public List<Seaport> getAllSeaports() {
        return null;
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public Seaport getSeaportByCode(String code) {
        return null;
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void createSeaport(Seaport seaport) {

    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void updateSeaport(Seaport seaport, String modifiedBy) {

    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void removeSeaport(Seaport seaport) {

    }
}
