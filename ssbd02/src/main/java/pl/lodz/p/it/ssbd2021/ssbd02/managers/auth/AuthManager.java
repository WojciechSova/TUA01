package pl.lodz.p.it.ssbd2021.ssbd02.managers.auth;

import org.apache.commons.codec.digest.DigestUtils;
import pl.lodz.p.it.ssbd2021.ssbd02.facades.auth.AuthViewFacade;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.List;

/**
 * Manager uwierzytelnienia
 *
 * @author Julia Kołodziej
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AuthManager implements AuthManagerLocal {

    @Inject
    private AuthViewFacade authViewFacade;

    /**
     * Metoda wyszukująca poziomy dostępu użytkownika o podanym loginie i haśle.
     *
     * @param login    Login użytkownika, którego poziomy dostępu są wyszukiwane
     * @param password Hasło użytkownika, którego poziomy dostępu są wyszukiwane
     * @return Lista obiektów typu {@link String} zawierająca poziomy dostępu danego użytkownika,
     * pusta lista w przypadku niezgodności loginu lub hasła
     */
    public List<String> getAccessLevels(String login, String password) {
        String hashedPassword = DigestUtils.sha512Hex(password);
        return authViewFacade.findLevelsByCredentials(login, hashedPassword);
    }
}
