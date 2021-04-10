package pl.lodz.p.it.ssbd2021.ssbd02.facades.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.facades.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.model.mop.Ferry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Klasa rozszerzająca abstrakcyjną klasę {@link AbstractFacade}.
 * Używa konkretnego typu {@link Ferry} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji do ssbd02mopPU.
 *
 * @author Artur Madaj
 */
@Stateless
public class FerryFacade extends AbstractFacade<Ferry> {

    @PersistenceContext(unitName = "ssbd02mopPU")
    private EntityManager entityManager;

    public FerryFacade() {
        super(Ferry.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Metoda wyszukująca encje typu {@link Ferry} o przekazanej nazwie promu.
     *
     * @param name Nazwa promu.
     * @return Obiekt typu {@link Ferry} o przekazanej nazwie promu.
     */
    public Ferry findByName(String name) {
        TypedQuery<Ferry> typedQuery = entityManager.createNamedQuery("Ferry.findByName", Ferry.class);
        typedQuery.setParameter("name", name);
        return typedQuery.getSingleResult();
    }
}
