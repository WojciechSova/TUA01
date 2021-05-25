package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.VehicleTypeFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.VehicleType;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.GeneralInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.PersistenceInterceptor;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Klasa rozszerzająca abstrakcyjną klasę {@link AbstractFacade}.
 * Używa konkretnego typu {@link VehicleType} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji do ssbd02mopPU.
 *
 * @author Artur Madaj
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({GeneralInterceptor.class, PersistenceInterceptor.class})
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
}
