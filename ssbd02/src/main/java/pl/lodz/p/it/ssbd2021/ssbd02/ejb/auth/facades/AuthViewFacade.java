package pl.lodz.p.it.ssbd2021.ssbd02.ejb.auth.facades;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.auth.facades.interfaces.AuthViewFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.auth.AuthView;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Klasa rozszerzająca abstrakcyjną klasę {@link AbstractFacade}.
 * Używa konkretnego typu {@link AuthView} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji to jednostka
 * służąca do uwierzytelniania ssbd02authPU.
 *
 * @author Daniel Łondka
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
public class AuthViewFacade extends AbstractFacade<AuthView> implements AuthViewFacadeLocal {

    @PersistenceContext(unitName = "ssbd02authPU")
    private EntityManager entityManager;

    public AuthViewFacade() {
        super(AuthView.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    @PermitAll
    public List<String> findLevelsByCredentials(String login, String password) {
        TypedQuery<String> typedQuery = entityManager.createNamedQuery("AuthView.findLevelByCredentials", String.class);
        typedQuery.setParameter("login", login);
        typedQuery.setParameter("password", password);
        return typedQuery.getResultList();
    }
}
