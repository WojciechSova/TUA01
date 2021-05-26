package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccountFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.GeneralInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.PersistenceInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.mok.AccountInterceptor;

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
 * Używa konkretnego typu {@link Account} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji to ssbd02mokPU.
 *
 * @author Patryk Kolanek
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
@Interceptors({GeneralInterceptor.class, AccountInterceptor.class, PersistenceInterceptor.class, TrackerInterceptor.class})
public class AccountFacade extends AbstractFacade<Account> implements AccountFacadeLocal {

    @PersistenceContext(unitName = "ssbd02mokPU")
    private EntityManager entityManager;

    public AccountFacade() {
        super(Account.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    @PermitAll
    public Account findByLogin(String login) {
        TypedQuery<Account> typedQuery = entityManager.createNamedQuery("Account.findByLogin", Account.class);
        typedQuery.setParameter("login", login);
        return typedQuery.getSingleResult();
    }

    @Override
    @PermitAll
    public Account findByEmail(String email) {
        TypedQuery<Account> typedQuery = entityManager.createNamedQuery("Account.findByEmail", Account.class);
        typedQuery.setParameter("email", email);
        return typedQuery.getSingleResult();
    }

    @Override
    public List<Account> findByConfirmed(boolean confirmed) {
        TypedQuery<Account> typedQuery = entityManager.createNamedQuery("Account.findByConfirmed", Account.class);
        typedQuery.setParameter("confirmed", confirmed);
        return typedQuery.getResultList();
    }

    @Override
    public List<Account> findByUnconfirmedAndExpired(int removalTime) {
        TypedQuery<Account> typedQuery = entityManager.createNamedQuery("Account.findByUnconfirmedAndExpired", Account.class);
        typedQuery.setParameter("removalTime", removalTime);
        return typedQuery.getResultList();
    }

    @Override
    @RolesAllowed({"ADMIN", "CLIENT", "EMPLOYEE"})
    public List<Account> findListByEmail(String email) {
        TypedQuery<Account> typedQuery = entityManager.createNamedQuery("Account.findByEmail", Account.class);
        typedQuery.setParameter("email", email);
        return typedQuery.getResultList();
    }

    @Override
    @PermitAll
    public void create(Account entity) {
        super.create(entity);
    }

    @Override
    @DenyAll
    public Account find(Object id) {
        return super.find(id);
    }

    @Override
    @RolesAllowed({"ADMIN", "EMPLOYEE", "CLIENT"})
    public void edit(Account entity) {
        super.edit(entity);
    }

    @Override
    @DenyAll
    public void remove(Account entity) {
        super.remove(entity);
    }

    @Override
    @RolesAllowed({"ADMIN"})
    public List<Account> findAll() {
        return super.findAll();
    }

    @Override
    @DenyAll
    public List<Account> findInRange(int start, int end) {
        return super.findInRange(start, end);
    }

    @Override
    @DenyAll
    public int count() {
        return super.count();
    }

    @Override
    @DenyAll
    public List<Account> findWithNamedQuery(String namedQuery) {
        return super.findWithNamedQuery(namedQuery);
    }

    @Override
    @DenyAll
    public List<Account> findWithQuery(String query) {
        return super.findWithQuery(query);
    }
}
