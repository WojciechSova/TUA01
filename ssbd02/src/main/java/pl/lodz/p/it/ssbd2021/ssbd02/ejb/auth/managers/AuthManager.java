package pl.lodz.p.it.ssbd2021.ssbd02.ejb.auth.managers;

import org.apache.commons.codec.digest.DigestUtils;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.auth.facades.interfaces.AuthViewFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.auth.managers.interfaces.AuthManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.TrackerInterceptor;

import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.security.enterprise.credential.Password;
import java.util.List;

/**
 * Manager uwierzytelnienia
 *
 * @author Julia Kołodziej
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Interceptors(TrackerInterceptor.class)
public class AuthManager extends AbstractManager implements AuthManagerLocal, SessionSynchronization {

    @Inject
    private AuthViewFacadeLocal authViewFacadeLocal;

    public List<String> getAccessLevels(String login, Password password) {
        String hashedPassword = DigestUtils.sha512Hex(String.valueOf(password.getValue()));
        return authViewFacadeLocal.findLevelsByCredentials(login, new Password(hashedPassword));
    }
}
