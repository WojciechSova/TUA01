package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.VehicleTypeFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.VehicleType;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Klasa rozszerzająca abstrakcyjną klasę {@link AbstractFacade}.
 * Używa konkretnego typu {@link VehicleType} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji do ssbd02mopPU.
 *
 * @author Artur Madaj
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
public class VehicleTypeFacade extends AbstractFacade<VehicleType> implements VehicleTypeFacadeLocal {

    @PersistenceContext(unitName = "ssbd02mopPU")
    private EntityManager entityManager;

    public VehicleTypeFacade() {
        super(VehicleType.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    @DenyAll
    public void create(VehicleType entity) {
        super.create(entity);
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public VehicleType find(Object id) {
        return super.find(id);
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void edit(VehicleType entity) {
        super.edit(entity);
    }

    @Override
    @DenyAll
    public void remove(VehicleType entity) {
        super.remove(entity);
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public List<VehicleType> findAll() {
        return super.findAll();
    }

    @Override
    @DenyAll
    public List<VehicleType> findInRange(int start, int end) {
        return super.findInRange(start, end);
    }

    @Override
    @DenyAll
    public int count() {
        return super.count();
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public List<VehicleType> findWithNamedQuery(String namedQuery) {
        return super.findWithNamedQuery(namedQuery);
    }

    @Override
    @DenyAll
    public List<VehicleType> findWithQuery(String query) {
        return super.findWithQuery(query);
    }
}
