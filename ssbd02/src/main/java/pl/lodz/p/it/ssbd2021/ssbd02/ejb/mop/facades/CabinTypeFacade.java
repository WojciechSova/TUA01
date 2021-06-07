package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.CabinTypeFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.CabinType;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.GeneralInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.PersistenceInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.TrackerInterceptor;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Klasa rozszerzająca abstrakcyjną klasę {@link AbstractFacade}.
 * Używa konkretnego typu {@link CabinType} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji do ssbd02mopPU.
 *
 * @author Artur Madaj
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
@Interceptors({GeneralInterceptor.class, PersistenceInterceptor.class, TrackerInterceptor.class})
public class CabinTypeFacade extends AbstractFacade<CabinType> implements CabinTypeFacadeLocal {

    @PersistenceContext(unitName = "ssbd02mopPU")
    private EntityManager entityManager;

    public CabinTypeFacade() {
        super(CabinType.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public CabinType findByName(String name) {
        return null;
    }

    @Override
    @DenyAll
    public void create(CabinType entity) {
        super.create(entity);
    }

    @Override
    @DenyAll
    public CabinType find(Object id) {
        return super.find(id);
    }

    @Override
    @DenyAll
    public void edit(CabinType entity) {
        super.edit(entity);
    }

    @Override
    @DenyAll
    public void remove(CabinType entity) {
        super.remove(entity);
    }

    @Override
    @RolesAllowed({"CLIENT", "EMPLOYEE"})
    public List<CabinType> findAll() {
        return super.findAll();
    }

    @Override
    @DenyAll
    public List<CabinType> findInRange(int start, int end) {
        return super.findInRange(start, end);
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public int count() {
        return super.count();
    }

    @Override
    @RolesAllowed({"EMPLOYEE"})
    public List<CabinType> findWithNamedQuery(String namedQuery) {
        return super.findWithNamedQuery(namedQuery);
    }

    @Override
    @DenyAll
    public List<CabinType> findWithQuery(String query) {
        return super.findWithQuery(query);
    }
}
