package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.CabinTypeManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.CabinType;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

/**
 * Manager typów kajut
 *
 * @author Wojciech Sowa
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CabinTypeManager implements CabinTypeManagerLocal {

    @Override
    public List<CabinType> getAllCabinTypes() {
        return null;
    }
}
