package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;

import javax.ejb.Local;
import java.util.List;

/**
 * Lokalny interfejs managera kont.
 *
 * @author Daniel Łondka
 */
@Local
public interface AccountManagerLocal {

    /**
     * Metoda wyszukujące wszystkie konta
     *
     * @return lista obiektów typu {@link Account} reprezentujących konta
     */
    List<Account> getAllAccounts();
}
