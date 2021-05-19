package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.VehicleTypeFacadeLocal;
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
public class VehicleTypeManager implements VehicleTypeFacadeLocal {
    @Override
    public void create(VehicleType entity) {

    }

    @Override
    public VehicleType find(Object id) {
        return null;
    }

    @Override
    public void edit(VehicleType entity) {

    }

    @Override
    public void remove(VehicleType entity) {

    }

    @Override
    public List<VehicleType> findAll() {
        return null;
    }

    @Override
    public List<VehicleType> findInRange(int start, int end) {
        return null;
    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public List<VehicleType> findWithNamedQuery(String namedQuery) {
        return null;
    }

    @Override
    public List<VehicleType> findWithQuery(String query) {
        return null;
    }
}
