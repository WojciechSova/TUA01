package pl.lodz.p.it.ssbd2021.ssbd02.ejb.auth.managers;

import javax.ejb.Local;
import java.util.List;

/**
 * Lokalny interfejs managera uwierzytelnienia
 *
 * @author Julia Kołodziej
 */
@Local
public interface AuthManagerLocal {

    List<String> getAccessLevels(String login, String password);
}
