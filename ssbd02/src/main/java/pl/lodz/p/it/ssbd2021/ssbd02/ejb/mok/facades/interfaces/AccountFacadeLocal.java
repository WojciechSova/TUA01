package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacadeInterface;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;

import javax.ejb.Local;
import java.util.List;

/**
 * Interfejs encji modułu obsługi kont.
 * Używa konkretnego typu {@link Account} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji to ssbd02mokPU.
 *
 * @author Daniel Łondka
 */
@Local
public interface AccountFacadeLocal extends AbstractFacadeInterface<Account> {

    /**
     * Metoda wyszukująca encję typu {@link Account} o przekazanym loginie.
     *
     * @param login Login encji.
     * @return Obiekt typu {@link Account}, o przekazanym loginie.
     */
    Account findByLogin(String login);

    /**
     * Metoda wyszukująca encję typu {@link Account} o przekazanym adresie email.
     *
     * @param email Email encji.
     * @return Obiekt typu {@link Account} o przekazanym adresie email.
     */
    Account findByEmail(String email);

    /**
     * Metoda wyszukująca listę encji typu {@link Account} o przekazanym stanie potwierdzenia konta.
     *
     * @param confirmed Wartość typu {@link Boolean} informująca o stanie potwierdzenia konta.
     * @return Lista obiektów typu {@link Account}.
     */
    List<Account> findByConfirmed(boolean confirmed);

    /**
     * Metoda wyszukująca listę encji typu {@link Account}, które nie zostały jeszcze potwierdzone,
     * a czas ich potwierdzenia minął.
     *
     * @param removalTime Wartość typu {@link Integer} informująca o liczbie sekund, po którym konto zostanie usunięte.
     * @return Lista obiektów typu {@link Account}.
     */
    List<Account> findByUnconfirmedAndExpired(int removalTime);
}
