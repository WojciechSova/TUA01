package pl.lodz.p.it.ssbd2021.ssbd02.facades.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.facades.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.model.mop.Cruise;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Klasa rozszerzająca abstrakcyjną klasę {@link AbstractFacade}.
 * Używa konkretnego typu {@link Cruise} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji do ssbd02mopPU.
 *
 * @author Artur Madaj
 */
@Stateless
public class CruiseFacade extends AbstractFacade<Cruise> {

    @PersistenceContext(unitName = "ssbd02mopPU")
    private EntityManager entityManager;

    public CruiseFacade() {
        super(Cruise.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Metoda wyszukująca encje typu {@link Cruise} o przekazanym biznesowym numerze identyfikacyjnym.
     *
     * @param number Biznesowy numer identyfikacyjny.
     * @return Obiekt typu {@link Cruise} o przekazanym biznesowym numerze identyfikacyjnym.
     */
    public Cruise findByNumber(String number) {
        TypedQuery<Cruise> typedQuery = entityManager.createNamedQuery("Cruise.findByNumber", Cruise.class);
        typedQuery.setParameter("number", number);
        return typedQuery.getSingleResult();
    }
}
