package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.OneTimeUrlFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.OneTimeUrl;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.GeneralInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.PersistenceInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.mok.OneTimeUrlInterceptor;

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
    public OneTimeUrl findByUrl(String url) {
        TypedQuery<OneTimeUrl> typedQuery = entityManager.createNamedQuery("OneTimeUrl.findByUrl", OneTimeUrl.class);
        typedQuery.setParameter("url", url);
        return typedQuery.getSingleResult();
    }

    @Override
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
    public List<OneTimeUrl> findByEmail() {
        TypedQuery<OneTimeUrl> typedQuery = entityManager.createNamedQuery("OneTimeUrl.findByNewEmail", OneTimeUrl.class);
        return typedQuery.getResultList();
    }
}
