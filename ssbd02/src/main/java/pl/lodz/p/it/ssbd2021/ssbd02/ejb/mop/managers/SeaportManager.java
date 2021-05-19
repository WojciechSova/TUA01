package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.SeaportManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Seaport;

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
public class SeaportManager implements SeaportManagerLocal {

    @Override
    public List<Seaport> getAllSeaports() {
        return null;
    }

    @Override
    public Seaport getSeaportByCode(String code) {
        return null;
    }

    @Override
    public void createSeaport(Seaport seaport) {

    }

    @Override
    public void updateSeaport(Seaport seaport, String modifiedBy) {

    }

    @Override
    public void removeSeaport(Seaport seaport) {

    }
}
