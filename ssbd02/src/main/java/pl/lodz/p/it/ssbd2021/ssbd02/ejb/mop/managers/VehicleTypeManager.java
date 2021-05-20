package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.VehicleTypeFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.VehicleTypeManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.VehicleType;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

/**
 * Manager typów pojazdów
 *
 * @author Wojciech Sowa
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class VehicleTypeManager implements VehicleTypeManagerLocal {

    @Override
    public List<VehicleType> getAllVehicleTypes() {
        return null;
    }
}
