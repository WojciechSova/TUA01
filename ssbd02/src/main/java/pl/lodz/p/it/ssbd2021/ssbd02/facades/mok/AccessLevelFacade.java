package pl.lodz.p.it.ssbd2021.ssbd02.facades.mok;

import pl.lodz.p.it.ssbd2021.ssbd02.facades.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.model.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.model.mok.Account;

import javax.ejb.Stateless;
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
public class AccessLevelFacade extends AbstractFacade<AccessLevel> {

    @PersistenceContext(unitName = "ssbd02mokPU")
    private EntityManager entityManager;

    public AccessLevelFacade() {
        super(AccessLevel.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Metoda wyszukująca encję typu {@link AccessLevel} o przekazanym identyfikatorze encji.
     *
     * @param id Identyfikator encji.
     * @return Zwraca obiekt typu {@link AccessLevel}, o przekazanym identyfikatorze encji.
     */
    public AccessLevel findById(String id) {
        TypedQuery<AccessLevel> typedQuery = entityManager.createNamedQuery("AccessLevel.findById", AccessLevel.class);
        typedQuery.setParameter("id", id);
        return typedQuery.getSingleResult();
    }

    /**
     * Metoda wyszukująca encje typu {@link AccessLevel}, w których znajduje się dowiązanie do encji typu {@link Account} o przekazanym loginie.
     *
     * @param login Login encji.
     * @return Zwraca kolekcję encji typu {@link AccessLevel}, w których znajduje się dowiązanie do encji typu {@link Account} o przekazanym loginie.
     */
    public List<AccessLevel> findByLogin(String login) {
        TypedQuery<AccessLevel> typedQuery = entityManager.createNamedQuery("AccessLevel.findByLogin", AccessLevel.class);
        typedQuery.setParameter("login", login);
        return typedQuery.getResultList();
    }
}
