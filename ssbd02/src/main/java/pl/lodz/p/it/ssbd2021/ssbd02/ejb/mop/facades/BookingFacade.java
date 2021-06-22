package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.BookingFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Booking;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.GeneralInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.PersistenceInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.mop.BookingInterceptor;

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
 * Używa konkretnego typu {@link Booking} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji do ssbd02mopPU.
 *
 * @author Artur Madaj
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
@Interceptors({GeneralInterceptor.class, BookingInterceptor.class, PersistenceInterceptor.class, TrackerInterceptor.class})
public class BookingFacade extends AbstractFacade<Booking> implements BookingFacadeLocal {

    @PersistenceContext(unitName = "ssbd02mopPU")
    private EntityManager entityManager;

    public BookingFacade() {
        super(Booking.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public Booking findByNumber(String number) {
        TypedQuery<Booking> typedQuery = entityManager.createNamedQuery("Booking.findByNumber", Booking.class);
        typedQuery.setParameter("number", number);
        return typedQuery.getSingleResult();
    }

    @Override
    @RolesAllowed({"CLIENT"})
    public List<Booking> findAllByAccount(Account account) {
        TypedQuery<Booking> typedQuery = entityManager.createNamedQuery("Booking.findByAccount", Booking.class);
        typedQuery.setParameter("account", account);
        return typedQuery.getResultList();
    }

    @Override
    @RolesAllowed({"CLIENT"})
    public Booking findByAccountAndNumber(Account account, String number) {
        TypedQuery<Booking> typedQuery = entityManager.createNamedQuery("Booking.findByAccountAndNumber", Booking.class);
        typedQuery.setParameter("account", account);
        typedQuery.setParameter("number", number);
        return typedQuery.getSingleResult();
    }

    @Override
    @RolesAllowed({"CLIENT"})
    public Long getSumNumberOfPeopleByCruise(Cruise cruise){
        TypedQuery<Long> typedQuery = entityManager.createNamedQuery("Booking.getNumberOfPeopleOnDeckByCruise", Long.class);
        typedQuery.setParameter("cruise", cruise);
        if (typedQuery.getSingleResult() == null){
            return 0L;
        }
        return typedQuery.getSingleResult();
    }

    @Override
    @RolesAllowed({"CLIENT"})
    public Double getSumVehicleSpaceByCruise(Cruise cruise) {
        TypedQuery<Double> typedQuery = entityManager.createNamedQuery("Booking.getSumVehicleSpaceByCruise", Double.class);
        typedQuery.setParameter("cruise", cruise);
        if (typedQuery.getSingleResult() == null){
            return 0D;
        }
        return typedQuery.getSingleResult();
    }

    @Override
    @RolesAllowed({"CLIENT"})
    public void create(Booking entity) {
        super.create(entity);
    }

    @Override
    @DenyAll
    public Booking find(Object id) {
        return super.find(id);
    }

    @Override
    @DenyAll
    public void edit(Booking entity) {
        super.edit(entity);
    }

    @Override
    @RolesAllowed({"CLIENT"})
    public void remove(Booking entity) {
        super.remove(entity);
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public List<Booking> findAll() {
        return super.findAll();
    }

    @Override
    @DenyAll
    public List<Booking> findInRange(int start, int end) {
        return super.findInRange(start, end);
    }

    @Override
    @DenyAll
    public int count() {
        return super.count();
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public List<Booking> findWithNamedQuery(String namedQuery) {
        return super.findWithNamedQuery(namedQuery);
    }

    @Override
    @DenyAll
    public List<Booking> findWithQuery(String query) {
        return super.findWithQuery(query);
    }
}
