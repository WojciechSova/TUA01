package pl.lodz.p.it.ssbd2021.ssbd02.ejb.auth.facades.interfaces;

import javax.ejb.Local;
import java.util.List;

@Local
public interface AuthViewFacadeLocal {

    List<String> findLevelsByCredentials(String login, String password);
}
