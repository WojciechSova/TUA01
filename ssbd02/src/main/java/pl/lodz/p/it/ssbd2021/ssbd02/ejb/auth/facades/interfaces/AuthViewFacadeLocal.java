package pl.lodz.p.it.ssbd2021.ssbd02.ejb.auth.facades.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacadeInterface;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.auth.AuthView;

import javax.ejb.Local;
import java.util.List;

@Local
public interface AuthViewFacadeLocal extends AbstractFacadeInterface<AuthView> {

    List<String> findLevelsByCredentials(String login, String password);
}
