package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccessLevelFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.GeneralInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.PersistenceInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.mok.AccessLevelInterceptor;

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
import java.util.List;

/**
 * Klasa rozszerzająca abstrakcyjną klasę {@link AbstractFacade}.
 * Używa konkretnego typu {@link AccessLevel} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji to ssbd02mokPU.
 *
 * @author Patryk Kolanek
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
@Interceptors({GeneralInterceptor.class, AccessLevelInterceptor.class, PersistenceInterceptor.class, TrackerInterceptor.class})
public class AccessLevelFacade extends AbstractFacade<AccessLevel> implements AccessLevelFacadeLocal {

    @PersistenceContext(unitName = "ssbd02mokPU")
    private EntityManager entityManager;

    public AccessLevelFacade() {
        super(AccessLevel.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    @DenyAll
    public List<AccessLevel> findByLogin(String login) {
        TypedQuery<AccessLevel> typedQuery = entityManager.createNamedQuery("AccessLevel.findByLogin", AccessLevel.class);
        typedQuery.setParameter("login", login);
        return typedQuery.getResultList();
    }

    @Override
    @RolesAllowed({"ADMIN", "EMPLOYEE", "CLIENT"})
    public List<AccessLevel> findAllByAccount(Account account) {
        TypedQuery<AccessLevel> typedQuery = entityManager.createNamedQuery("AccessLevel.findByAccount", AccessLevel.class);
        typedQuery.setParameter("account", account);
        return typedQuery.getResultList();
    }

    @Override
    @RolesAllowed({"ADMIN", "EMPLOYEE", "CLIENT"})
    public List<AccessLevel> findAllActiveByAccount(Account account) {
        TypedQuery<AccessLevel> typedQuery = entityManager.createNamedQuery("AccessLevel.findAllActiveByAccount", AccessLevel.class);
        typedQuery.setParameter("account", account);
        return typedQuery.getResultList();
    }

    @Override
    @PermitAll
    public void create(AccessLevel entity) {
        super.create(entity);
    }

    @Override
    @DenyAll
    public AccessLevel find(Object id) {
        return super.find(id);
    }

    @Override
    @RolesAllowed({"ADMIN"})
    public void edit(AccessLevel entity) {
        super.edit(entity);
    }

    @Override
    @DenyAll
    public void remove(AccessLevel entity) {
        super.remove(entity);
    }

    @Override
    @DenyAll
    public List<AccessLevel> findAll() {
        return super.findAll();
    }

    @Override
    @DenyAll
    public List<AccessLevel> findInRange(int start, int end) {
        return super.findInRange(start, end);
    }

    @Override
    @DenyAll
    public int count() {
        return super.count();
    }

    @Override
    @DenyAll
    public List<AccessLevel> findWithNamedQuery(String namedQuery) {
        return super.findWithNamedQuery(namedQuery);
    }

    @Override
    @DenyAll
    public List<AccessLevel> findWithQuery(String query) {
        return super.findWithQuery(query);
    }
}
