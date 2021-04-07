package pl.lodz.p.it.ssbd2021.ssbd02.facades.mok;

import pl.lodz.p.it.ssbd2021.ssbd02.facades.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.model.Account;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Klasa rozszerzająca abstrakcyjną klasę {@link AbstractFacade}.
 * Używa konkretnego typu {@link Account} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji to ssbd02mokPU.
 *
 * @author Patryk Kolanek
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AccountFacade extends AbstractFacade<Account> {

    @PersistenceContext(unitName = "ssbd02mokPU")
    private EntityManager entityManager;

    public AccountFacade() {
        super(Account.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Metoda wyszukująca encję typu {@link Account} o przekazanym identyfikatorze encji.
     *
     * @param id Identyfikator encji.
     * @return Zwraca obiekt typu {@link Account}, o przekazanym identyfikatorze encji.
     */
    public Account findById(Long id) {
        TypedQuery<Account> typedQuery = entityManager.createNamedQuery("Account.findById", Account.class);
        typedQuery.setParameter("id", id);
        return typedQuery.getSingleResult();
    }

    /**
     * Metoda wyszukująca encję typu {@link Account} o przekazanym loginie.
     *
     * @param login Login encji.
     * @return Zwraca obiekt typu {@link Account}, o przekazanym loginie encji.
     */
    public Account findByLogin(String login) {
        TypedQuery<Account> typedQuery = entityManager.createNamedQuery("Account.findByLogin", Account.class);
        typedQuery.setParameter("login", login);
        return typedQuery.getSingleResult();
    }

    /**
     * Metoda wyszukująca encję typu {@link Account} o przekazanym adresie email.
     *
     * @param email Email encji.
     * @return Zwraca obiekt typu {@link Account}, o przekazanym emailu encji.
     */
    public Account findByEmail(String email) {
        TypedQuery<Account> typedQuery = entityManager.createNamedQuery("Account.findByEmail", Account.class);
        typedQuery.setParameter("email", email);
        return typedQuery.getSingleResult();
    }
}
