package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.OneTimeUrlFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.OneTimeUrl;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.GeneralInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.PersistenceInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.mok.OneTimeUrlInterceptor;

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
 * Używa konkretnego typu {@link OneTimeUrl} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji to ssbd02mokPU.
 *
 * @author Artur Madaj
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
@Interceptors({GeneralInterceptor.class, OneTimeUrlInterceptor.class, PersistenceInterceptor.class})
public class OneTimeUrlFacade extends AbstractFacade<OneTimeUrl> implements OneTimeUrlFacadeLocal {

    @PersistenceContext(unitName = "ssbd02mokPU")
    private EntityManager entityManager;

    public OneTimeUrlFacade() {
        super(OneTimeUrl.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    @PermitAll
    public OneTimeUrl findByUrl(String url) {
        TypedQuery<OneTimeUrl> typedQuery = entityManager.createNamedQuery("OneTimeUrl.findByUrl", OneTimeUrl.class);
        typedQuery.setParameter("url", url);
        return typedQuery.getSingleResult();
    }

    @Override
    @PermitAll
    public List<OneTimeUrl> findByAccount(Account account) {
        TypedQuery<OneTimeUrl> typedQuery = entityManager.createNamedQuery("OneTimeUrl.findByAccount", OneTimeUrl.class);
        typedQuery.setParameter("account", account);
        return typedQuery.getResultList();
    }

    @Override
    public List<OneTimeUrl> findExpired() {
        TypedQuery<OneTimeUrl> typedQuery = entityManager.createNamedQuery("OneTimeUrl.findExpiredUrl", OneTimeUrl.class);
        return typedQuery.getResultList();
    }

    @Override
    @PermitAll
    public List<OneTimeUrl> findListByEmail(String email) {
        TypedQuery<OneTimeUrl> typedQuery = entityManager.createNamedQuery("OneTimeUrl.findByNewEmail", OneTimeUrl.class);
        typedQuery.setParameter("newEmail", email);
        return typedQuery.getResultList();
    }

    @Override
    @PermitAll
    public void create(OneTimeUrl entity) {
        super.create(entity);
    }

    @Override
    @DenyAll
    public OneTimeUrl find(Object id) {
        return super.find(id);
    }

    @Override
    @PermitAll
    public void edit(OneTimeUrl entity) {
        super.edit(entity);
    }

    @Override
    @PermitAll
    public void remove(OneTimeUrl entity) {
        super.remove(entity);
    }

    @Override
    @DenyAll
    public List<OneTimeUrl> findAll() {
        return super.findAll();
    }

    @Override
    @DenyAll
    public List<OneTimeUrl> findInRange(int start, int end) {
        return super.findInRange(start, end);
    }

    @Override
    @DenyAll
    public int count() {
        return super.count();
    }

    @Override
    @DenyAll
    public List<OneTimeUrl> findWithNamedQuery(String namedQuery) {
        return super.findWithNamedQuery(namedQuery);
    }

    @Override
    @DenyAll
    public List<OneTimeUrl> findWithQuery(String query) {
        return super.findWithQuery(query);
    }
}
