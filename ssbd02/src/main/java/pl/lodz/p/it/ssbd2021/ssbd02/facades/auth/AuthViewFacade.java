package pl.lodz.p.it.ssbd2021.ssbd02.facades.auth;

import pl.lodz.p.it.ssbd2021.ssbd02.facades.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd02.model.auth.AuthView;

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
public class AuthViewFacade extends AbstractFacade<AuthView> {

    @PersistenceContext(unitName = "ssbd02authPU")
    private EntityManager entityManager;

    public AuthViewFacade() {
        super(AuthView.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Metoda wyszukująca poziomy dostępu użytkownika.
     *
     * @param login Login konta, którego poziomy dostępu pozyskujemy.
     * @param password Hasło konta, którego poziomy dostępu pozyskujemy.
     * @return Lista poziomów dostępu typu {@link String}.
     */
    public List<String> findLevelsByCredentials(String login, String password) {
        TypedQuery<String> typedQuery = entityManager.createNamedQuery("AuthView.findLevelByCredentials", String.class);
        typedQuery.setParameter("login", login);
        typedQuery.setParameter("password", password);
        return typedQuery.getResultList();
    }
}
