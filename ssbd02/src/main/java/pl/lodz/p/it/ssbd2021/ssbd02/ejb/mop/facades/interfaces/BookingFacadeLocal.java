package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacadeInterface;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Booking;

import javax.ejb.Local;
import java.util.List;

/**
 * Interfejs encji modułu obsługi promów.
 * Używa konkretnego typu {@link Booking} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji do ssbd02mopPU.
 *
 * @author Daniel Łondka
 */
@Local
public interface BookingFacadeLocal extends AbstractFacadeInterface<Booking> {

    /**
     * Metoda wyszukująca encje typu {@link Booking} o przekazanym biznesowym numerze identyfikacyjnym.
     *
     * @param number Biznesowy numer identyfikacyjny.
     * @return Obiekt typu {@link Booking} o przekazanym biznesowym numerze identyfikacyjnym.
     */
    Booking findByNumber(String number);

    /**
     * Metoda wyszukująca listę encji typu {@link Booking}, które należą do podanego konta.
     *
     * @param account Encja typu {@link Account}.
     * @return Listę obiektów typu {@link Booking}, które należą do podanego konta.
     */
    List<Booking> findAllByAccount(Account account);

    /**
     * Metoda wyszukująca listę encji typu {@link Booking} o przekazanym biznesowym numerze identyfikacyjnym
     * która należy do podanego konta.
     *
     * @param number Biznesowy numer identyfikacyjny.
     * @param account Encja typu {@link Account}.
     * @return Obiekt typu {@link Booking} o przekazanym biznesowym numerze identyfikacyjnym, który należy do podanego konta.
     */
    Booking findByAccountAndNumber(Account account, String number);
}
