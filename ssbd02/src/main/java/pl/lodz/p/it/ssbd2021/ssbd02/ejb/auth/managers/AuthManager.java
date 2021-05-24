package pl.lodz.p.it.ssbd2021.ssbd02.ejb.auth.managers;

import org.apache.commons.codec.digest.DigestUtils;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.auth.facades.interfaces.AuthViewFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.auth.managers.interfaces.AuthManagerLocal;

import javax.annotation.security.PermitAll;
import javax.ejb.SessionSynchronization;
import javax.annotation.security.RolesAllowed;
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
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
public class AuthManager extends AbstractManager implements AuthManagerLocal, SessionSynchronization {


    @Inject
    private AuthViewFacadeLocal authViewFacadeLocal;

    @Override
    @PermitAll
    public List<String> getAccessLevels(String login, String password) {
        String hashedPassword = DigestUtils.sha512Hex(password);
        return authViewFacadeLocal.findLevelsByCredentials(login, hashedPassword);
    }
}
