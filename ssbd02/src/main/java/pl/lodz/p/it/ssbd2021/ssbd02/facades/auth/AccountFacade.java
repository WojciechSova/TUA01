package pl.lodz.p.it.ssbd2021.ssbd02.facades.auth;

import pl.lodz.p.it.ssbd2021.ssbd02.facades.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.model.mok.Account;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Klasa rozszerzająca abstrakcyjną klasę {@link AbstractFacade}.
 * Używa konkretnego typu {@link Account} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji to ssbd02authPU.
 *
 * @author Kacper Świercz
 */
@Stateless
public class AccountFacade extends AbstractFacade<Account> {

    @PersistenceContext(unitName = "ssbd02authPU")
    private EntityManager entityManager;

    public AccountFacade() {
        super(Account.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Metoda wyszukująca encję typu {@link Account} o przekazanym loginie.
     *
     * @param login Login encji.
     * @return Zwraca encję typu {@link Account} o przekazanym loginie.
     */
    public Account findByLogin(String login) {
        TypedQuery<Account> tq = entityManager.createNamedQuery("Account.findByLogin", Account.class);
        tq.setParameter("login", login);
        return tq.getSingleResult();
    }
}
