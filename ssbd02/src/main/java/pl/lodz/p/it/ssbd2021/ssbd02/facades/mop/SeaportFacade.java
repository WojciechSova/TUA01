package pl.lodz.p.it.ssbd2021.ssbd02.facades.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.facades.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.model.mop.Seaport;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Klasa rozszerzająca abstrakcyjną klasę {@link AbstractFacade}.
 * Używa konkretnego typu {@link Seaport} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji do ssbd02mopPU.
 *
 * @author Artur Madaj
 */
@Stateless
public class SeaportFacade extends AbstractFacade<Seaport> {

    @PersistenceContext(unitName = "ssbd02mopPU")
    private EntityManager entityManager;

    public SeaportFacade() {
        super(Seaport.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Metoda wyszukująca encje typu {@link Seaport} o przekazanym kodzie portu.
     *
     * @param code Kod portu.
     * @return Obiekt typu {@link Seaport} o przekazanym kodzie portu.
     */
    public Seaport findByCode(String code) {
        TypedQuery<Seaport> typedQuery = entityManager.createNamedQuery("Seaport.findByCode", Seaport.class);
        typedQuery.setParameter("code", code);
        return typedQuery.getSingleResult();
    }
}
