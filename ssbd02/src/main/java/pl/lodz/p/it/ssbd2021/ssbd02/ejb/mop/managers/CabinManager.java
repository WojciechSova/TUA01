package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.CabinManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;

import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

/**
 * Manager kajut
 *
 * @author Wojciech Sowa
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CabinManager extends AbstractManager implements CabinManagerLocal, SessionSynchronization {

    @Override
    public List<Cabin> getAllCabins() {
        return null;
    }

    @Override
    public List<Cabin> getAllCabinsByFerryCode(String code) {
        return null;
    }

    @Override
    public List<Cabin> getAllCabinsByFerryCodeAndCabinType(String code, String cabinType) {
        return null;
    }

    @Override
    public Cabin getCabinByNumber(String number) {
        return null;
    }

    @Override
    public void createCabin(Cabin cabin) {

    }

    @Override
    public void updateCabin(Cabin cabin, String modifiedBy) {

    }

    @Override
    public void removeCabin(Cabin cabin) {

    }
}
