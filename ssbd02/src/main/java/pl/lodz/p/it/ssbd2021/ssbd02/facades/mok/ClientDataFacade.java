package pl.lodz.p.it.ssbd2021.ssbd02.facades.mok;

import pl.lodz.p.it.ssbd2021.ssbd02.facades.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.model.ClientData;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Klasa rozszerzająca abstrakcyjną klasę {@link AbstractFacade}.
 * Używa konkretnego typu {@link ClientData} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji to ssbd02mokPU.
 *
 * @author Patryk Kolanek
 */
@Stateless
public class ClientDataFacade extends AbstractFacade<ClientData> {

    @PersistenceContext(unitName = "ssbd02mokPU")
    private EntityManager entityManager;

    public ClientDataFacade() {
        super(ClientData.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Metoda wyszukująca encje typu {@link ClientData} o przekazanym identyfikatorze encji.
     *
     * @param id Identyfikator encji.
     * @return Zwraca obiekt typu {@link ClientData}, o przekazanym identyfikatorze encji.
     */
    public ClientData findById(Long id) {
        TypedQuery<ClientData> typedQuery = entityManager.createNamedQuery("ClientData.findById", ClientData.class);
        typedQuery.setParameter("id", id);
        return typedQuery.getSingleResult();
    }
}
