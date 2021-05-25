package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.CruiseFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.GeneralInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.PersistenceInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.mop.CruiseInterceptor;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Klasa rozszerzająca abstrakcyjną klasę {@link AbstractFacade}.
 * Używa konkretnego typu {@link Cruise} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji do ssbd02mopPU.
 *
 * @author Artur Madaj
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({GeneralInterceptor.class, CruiseInterceptor.class, PersistenceInterceptor.class})
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

    public Cruise findByNumber(String number) {
        TypedQuery<Cruise> typedQuery = entityManager.createNamedQuery("Cruise.findByNumber", Cruise.class);
        typedQuery.setParameter("number", number);
        return typedQuery.getSingleResult();
    }
}
