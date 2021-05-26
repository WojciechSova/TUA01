package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.BookingFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Booking;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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

    public Booking findByNumber(String number) {
        TypedQuery<Booking> typedQuery = entityManager.createNamedQuery("Booking.findByNumber", Booking.class);
        typedQuery.setParameter("number", number);
        return typedQuery.getSingleResult();
    }
}
