package pl.lodz.p.it.ssbd2021.ssbd02.facades.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.facades.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.model.mop.Cabin;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Klasa rozszerzająca abstrakcyjną klasę {@link AbstractFacade}.
 * Używa konkretnego typu {@link Cabin} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji do ssbd02mopPU.
 *
 * @author Artur Madaj
 */
@Stateless
public class CabinFacade extends AbstractFacade<Cabin> {

    @PersistenceContext(unitName = "ssbd02mopPU")
    private EntityManager entityManager;

    public CabinFacade() {
        super(Cabin.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Metoda wyszukująca encje typu {@link Cabin} o przekazanym biznesowym numerze identyfikacyjnym.
     *
     * @param number Biznesowy numer identyfikacyjny.
     * @return Obiekt typu {@link Cabin} o przekazanym biznesowym numerze identyfikacyjnym.
     */
    public Cabin findByNumber(String number) {
        TypedQuery<Cabin> typedQuery = entityManager.createNamedQuery("Cabin.findByNumber", Cabin.class);
        typedQuery.setParameter("number", number);
        return typedQuery.getSingleResult();
    }
}
