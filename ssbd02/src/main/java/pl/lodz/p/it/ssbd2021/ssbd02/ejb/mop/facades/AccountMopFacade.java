package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.AccountMopFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.GeneralInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.PersistenceInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.mok.AccountInterceptor;

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
 * Używa konkretnego typu {@link Account} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji to ssbd02mopPU.
 *
 * @author Karolina Kowalczyk
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
@Interceptors({GeneralInterceptor.class, AccountInterceptor.class, PersistenceInterceptor.class})
public class AccountMopFacade extends AbstractFacade<Account> implements AccountMopFacadeLocal {

    @PersistenceContext(unitName = "ssbd02mopPU")
    private EntityManager entityManager;

    public AccountMopFacade() {
        super(Account.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public Account findByLogin(String login) {
        TypedQuery<Account> typedQuery = entityManager.createNamedQuery("Account.findByLogin", Account.class);
        typedQuery.setParameter("login", login);
        return typedQuery.getSingleResult();
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public Account findByEmail(String email) {
        TypedQuery<Account> typedQuery = entityManager.createNamedQuery("Account.findByEmail", Account.class);
        typedQuery.setParameter("email", email);
        return typedQuery.getSingleResult();
    }

    @Override
    @DenyAll
    public void create(Account entity) {
        super.create(entity);
    }

    @Override
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public Account find(Object id) {
        return super.find(id);
    }

    @Override
    @DenyAll
    public void edit(Account entity) {
        super.edit(entity);
    }

    @Override
    @DenyAll
    public void remove(Account entity) {
        super.remove(entity);
    }

    @Override
    @DenyAll
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
    @RolesAllowed({"EMPLOYEE", "CLIENT"})
    public List<Account> findWithNamedQuery(String namedQuery) {
        return super.findWithNamedQuery(namedQuery);
    }

    @Override
    @DenyAll
    public List<Account> findWithQuery(String query) {
        return super.findWithQuery(query);
    }
}
