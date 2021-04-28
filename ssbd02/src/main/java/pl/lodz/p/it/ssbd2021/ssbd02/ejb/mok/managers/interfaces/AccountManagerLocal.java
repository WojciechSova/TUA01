package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;

import javax.ejb.Local;
import javax.ws.rs.WebApplicationException;
import java.util.List;
import java.util.Map;

/**
 * Lokalny interfejs managera kont
 *
 * @author Daniel Łondka
 */
@Local
public interface AccountManagerLocal {

    /**
     * Metoda wyszukujące wszystkie konta wraz z ich poziomami dostępu
     *
     * @return Mapa o kluczu {@link Account} i wartości będącej listą {@link AccessLevel} reprezentująca konta
     * i ich poziomy dostępu
     */
    Map<Account, List<AccessLevel>> getAllAccountsWithAccessLevels();

    /**
     * Metoda tworząca konto wraz z początkowym poziomem dostępu klienta
     *
     * @param account Encja typu {@link Account}
     * @throws WebApplicationException Wyjątek zwracający kod odpowiedzi 409 w przypadku, gdy istnieje już konto
     * o podanym loginie bądź emailu
     */
    void createAccount(Account account) throws WebApplicationException;

}
