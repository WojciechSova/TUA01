package pl.lodz.p.it.ssbd2021.ssbd02.facades.auth;

import pl.lodz.p.it.ssbd2021.ssbd02.facades.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.model.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.model.Account;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Klasa rozszerzająca abstrakcyjną klasę {@link AbstractFacade}.
 * Używa konkretnego typu {@link AccessLevel} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji to ssbd02authPU.
 *
 * @author Kacper Świercz
 */
@Stateless
public class AccessLevelFacade extends AbstractFacade<AccessLevel> {

    @PersistenceContext(unitName = "ssbd02authPU")
    private EntityManager entityManager;

    public AccessLevelFacade() {
        super(AccessLevel.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Metoda wyszukująca encje typu {@link AccessLevel}, w których znajduje się dowiązanie do encji typu {@link Account} o przekazanym loginie.
     *
     * @param login Login encji.
     * @return Zwraca kolekcję encji typu {@link AccessLevel}, w których znajduje się dowiązanie do encji typu {@link Account} o przekazanym loginie.
     */
    public List<AccessLevel> findByLogin(String login) {
        TypedQuery<AccessLevel> tq = entityManager.createNamedQuery("AccessLevel.findByLogin", AccessLevel.class);
        tq.setParameter("login", login);
        return tq.getResultList();
    }
}
