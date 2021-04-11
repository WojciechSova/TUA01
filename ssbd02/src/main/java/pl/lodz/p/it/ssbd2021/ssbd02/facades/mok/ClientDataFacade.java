package pl.lodz.p.it.ssbd2021.ssbd02.facades.mok;

import pl.lodz.p.it.ssbd2021.ssbd02.facades.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.model.mok.ClientData;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Klasa rozszerzająca abstrakcyjną klasę {@link AbstractFacade}.
 * Używa konkretnego typu {@link ClientData} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji to ssbd02mokPU.
 *
 * @author Patryk Kolanek
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
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
}
