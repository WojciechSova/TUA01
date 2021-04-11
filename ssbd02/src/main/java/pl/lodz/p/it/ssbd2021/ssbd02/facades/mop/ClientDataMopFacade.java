package pl.lodz.p.it.ssbd2021.ssbd02.facades.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.facades.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.model.mok.ClientData;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Klasa rozszerzająca abstrakcyjną klasę {@link AbstractFacade}.
 * Używa konkretnego typu {@link ClientData} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji to ssbd02mopPU.
 *
 * @author Karolina Kowalczyk
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ClientDataMopFacade extends AbstractFacade<ClientData> {

    @PersistenceContext(unitName = "ssbd02mopPU")
    private EntityManager entityManager;

    public ClientDataMopFacade() {
        super(ClientData.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Metoda wyszukująca encję typu {@link ClientData} o przekazanym loginie.
     *
     * @param login Login konta.
     * @return Obiekt typu {@link ClientData} o przekazanym loginie.
     */
    public ClientData findByLogin(String login) {
        TypedQuery<ClientData> typedQuery = entityManager.createNamedQuery("ClientData.findByLogin", ClientData.class);
        typedQuery.setParameter("login", login);
        return typedQuery.getSingleResult();
    }

}
