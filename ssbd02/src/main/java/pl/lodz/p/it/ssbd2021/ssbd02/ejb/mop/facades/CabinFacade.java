package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.CabinFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.GeneralInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.PersistenceInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.mop.CabinInterceptor;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Klasa rozszerzająca abstrakcyjną klasę {@link AbstractFacade}.
 * Używa konkretnego typu {@link Cabin} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji do ssbd02mopPU.
 *
 * @author Artur Madaj
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
@Interceptors({GeneralInterceptor.class, CabinInterceptor.class, PersistenceInterceptor.class, TrackerInterceptor.class})
public class CabinFacade extends AbstractFacade<Cabin> implements CabinFacadeLocal {

    @PersistenceContext(unitName = "ssbd02mopPU")
    private EntityManager entityManager;

    public CabinFacade() {
        super(Cabin.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public Cabin findByNumber(String number) {
        TypedQuery<Cabin> typedQuery = entityManager.createNamedQuery("Cabin.findByNumber", Cabin.class);
        typedQuery.setParameter("number", number);
        return typedQuery.getSingleResult();
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public List<Cabin> findAllByFerry(Ferry ferry) {
        TypedQuery<Cabin> typedQuery = entityManager.createNamedQuery("Cabin.findByFerry", Cabin.class);
        typedQuery.setParameter("ferry", ferry);
        return typedQuery.getResultList();
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void create(Cabin entity) {
        super.create(entity);
    }

    @Override
    @DenyAll
    public Cabin find(Object id) {
        return super.find(id);
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void edit(Cabin entity) {
        super.edit(entity);
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void remove(Cabin entity) {
        super.remove(entity);
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public List<Cabin> findAll() {
        return super.findAll();
    }

    @Override
    @DenyAll
    public List<Cabin> findInRange(int start, int end) {
        return super.findInRange(start, end);
    }

    @Override
    @DenyAll
    public int count() {
        return super.count();
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public List<Cabin> findWithNamedQuery(String namedQuery) {
        return super.findWithNamedQuery(namedQuery);
    }

    @Override
    @DenyAll
    public List<Cabin> findWithQuery(String query) {
        return super.findWithQuery(query);
    }
}
