package pl.lodz.p.it.ssbd2021.ssbd02.facades;

import pl.lodz.p.it.ssbd2021.ssbd02.model.ClientData;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
