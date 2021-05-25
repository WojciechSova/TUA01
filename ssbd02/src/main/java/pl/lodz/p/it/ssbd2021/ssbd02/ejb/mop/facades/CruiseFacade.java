package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.CruiseFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Klasa rozszerzająca abstrakcyjną klasę {@link AbstractFacade}.
 * Używa konkretnego typu {@link Cruise} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji do ssbd02mopPU.
 *
 * @author Artur Madaj
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
public class CruiseFacade extends AbstractFacade<Cruise> implements CruiseFacadeLocal {

    @PersistenceContext(unitName = "ssbd02mopPU")
    private EntityManager entityManager;

    public CruiseFacade() {
        super(Cruise.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public Cruise findByNumber(String number) {
        TypedQuery<Cruise> typedQuery = entityManager.createNamedQuery("Cruise.findByNumber", Cruise.class);
        typedQuery.setParameter("number", number);
        return typedQuery.getSingleResult();
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void create(Cruise entity) {
        super.create(entity);
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public Cruise find(Object id) {
        return super.find(id);
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void edit(Cruise entity) {
        super.edit(entity);
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void remove(Cruise entity) {
        super.remove(entity);
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public List<Cruise> findAll() {
        return super.findAll();
    }

    @Override
    @DenyAll
    public List<Cruise> findInRange(int start, int end) {
        return super.findInRange(start, end);
    }

    @Override
    @DenyAll
    public int count() {
        return super.count();
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public List<Cruise> findWithNamedQuery(String namedQuery) {
        return super.findWithNamedQuery(namedQuery);
    }

    @Override
    @DenyAll
    public List<Cruise> findWithQuery(String query) {
        return super.findWithQuery(query);
    }
}
