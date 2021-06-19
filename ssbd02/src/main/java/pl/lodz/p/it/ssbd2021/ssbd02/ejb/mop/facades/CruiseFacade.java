package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.CruiseFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Route;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.GeneralInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.PersistenceInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.mop.CruiseInterceptor;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
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
@Interceptors({GeneralInterceptor.class, CruiseInterceptor.class, PersistenceInterceptor.class, TrackerInterceptor.class})
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
    @RolesAllowed({"EMPLOYEE"})
    public Cruise findByNumber(String number) {
        TypedQuery<Cruise> typedQuery = entityManager.createNamedQuery("Cruise.findByNumber", Cruise.class);
        typedQuery.setParameter("number", number);
        return typedQuery.getSingleResult();
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public List<Cruise> findAllByRoute(Route route) {
        TypedQuery<Cruise> typedQuery = entityManager.createNamedQuery("Cruise.findByRoute", Cruise.class);
        typedQuery.setParameter("route", route);
        return typedQuery.getResultList();
    }

    @Override
    @PermitAll
    public List<Cruise> findAllFutureDate() {
        return entityManager.createNamedQuery("Cruise.findCurrentCruises", Cruise.class).getResultList();
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public List<Cruise> findAllUsingFerryInTime(Ferry ferry, Timestamp startDate, Timestamp endDate) {
        TypedQuery<Cruise> typedQuery = entityManager.createNamedQuery("Cruise.findAllUsingFerryInTime", Cruise.class);
        typedQuery.setParameter("ferry", ferry);
        typedQuery.setParameter("startDate", startDate);
        typedQuery.setParameter("endDate", endDate);
        return typedQuery.getResultList();
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public void create(Cruise entity) {
        super.create(entity);
    }

    @Override
    @DenyAll
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
    @RolesAllowed({"EMPLOYEE"})
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
