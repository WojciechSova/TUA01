package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.RouteFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Route;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.GeneralInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.PersistenceInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.mop.RouteInterceptor;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Klasa rozszerzająca abstrakcyjną klasę {@link AbstractFacade}.
 * Używa konkretnego typu {@link Route} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji do ssbd02mopPU.
 *
 * @author Artur Madaj
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({GeneralInterceptor.class, RouteInterceptor.class, PersistenceInterceptor.class})
public class RouteFacade extends AbstractFacade<Route> implements RouteFacadeLocal {

    @PersistenceContext(unitName = "ssbd02mopPU")
    private EntityManager entityManager;

    public RouteFacade() {
        super(Route.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public Route findByCode(String code) {
        TypedQuery<Route> typedQuery = entityManager.createNamedQuery("Route.findByCode", Route.class);
        typedQuery.setParameter("code", code);
        return typedQuery.getSingleResult();
    }
}
