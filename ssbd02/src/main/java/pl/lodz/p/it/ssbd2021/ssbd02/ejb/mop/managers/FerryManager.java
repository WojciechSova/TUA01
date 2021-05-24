package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.apache.commons.lang3.tuple.Pair;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.FerryManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;

import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

/**
 * Manager promów
 *
 * @author Wojciech Sowa
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class FerryManager extends AbstractManager implements FerryManagerLocal, SessionSynchronization {

    @Override
    public List<Ferry> getAllFerries() {
        return null;
    }

    @Override
    public Ferry getFerryByName(String name) {
        return null;
    }

    @Override
    public Pair<Ferry, List<Cabin>> getFerryAndCabinsByFerryName(String name) {
        return null;
    }

    @Override
    public void createFerry(Ferry ferry) {

    }

    @Override
    public void updateFerry(Ferry ferry, String modifiedBy) {

    }

    @Override
    public void removeFerry(Ferry ferry) {

    }
}
