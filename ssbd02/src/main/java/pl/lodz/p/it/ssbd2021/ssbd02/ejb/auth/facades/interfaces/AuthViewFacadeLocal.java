package pl.lodz.p.it.ssbd2021.ssbd02.ejb.auth.facades.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacadeInterface;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.auth.AuthView;

import javax.ejb.Local;
import java.util.List;

/**
 * Interfejs fasady wykorzystywany do uwierzytelniania.
 * Używa konkretnego typu {@link AuthView} zamiast typu generycznego.
 *
 * @author Daniel Łondka
 */
@Local
public interface AuthViewFacadeLocal extends AbstractFacadeInterface<AuthView> {

    /**
     * Metoda wyszukująca poziomy dostępu użytkownika.
     *
     * @param login Login konta, którego poziomy dostępu pozyskujemy.
     * @param password Hasło konta, którego poziomy dostępu pozyskujemy.
     * @return Lista poziomów dostępu typu {@link String}.
     */
    List<String> findLevelsByCredentials(String login, String password);
}
