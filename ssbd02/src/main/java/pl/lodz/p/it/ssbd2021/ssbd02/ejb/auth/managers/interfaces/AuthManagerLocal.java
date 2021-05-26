package pl.lodz.p.it.ssbd2021.ssbd02.ejb.auth.managers.interfaces;

import javax.ejb.Local;
import javax.security.enterprise.credential.Password;
import java.util.List;

/**
 * Lokalny interfejs managera uwierzytelnienia
 *
 * @author Julia Kołodziej
 */
@Local
public interface AuthManagerLocal {

    /**
     * Metoda wyszukująca poziomy dostępu użytkownika o podanym loginie i haśle.
     *
     * @param login    Login użytkownika, którego poziomy dostępu są wyszukiwane
     * @param password Hasło użytkownika, którego poziomy dostępu są wyszukiwane
     * @return Lista obiektów typu {@link String} zawierająca poziomy dostępu danego użytkownika,
     * pusta lista w przypadku niezgodności loginu lub hasła
     */
    List<String> getAccessLevels(String login, Password password);
}
