package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacadeInterface;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.OneTimeUrl;

import javax.ejb.Local;
import java.util.List;

/**
 * Interfejs encji modułu obsługi kont.
 * Używa konkretnego typu {@link OneTimeUrl} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji to ssbd02mokPU.
 *
 * @author Artur Madaj
 */
@Local
public interface OneTimeUrlFacadeLocal extends AbstractFacadeInterface<OneTimeUrl> {

    /**
     * Metoda wyszukująca encję typu {@link OneTimeUrl} o przekazanym adresie URL.
     *
     * @param url Adres URL encji
     * @return Obiekt typu {@link OneTimeUrl} o przekazanym adresie URL
     */
    OneTimeUrl findByUrl(String url);

    /**
     * Metoda wyszukująca listę encji typu {@link OneTimeUrl} o przekazanym koncie.
     *
     * @param account Konto którego adresów URL szukamy
     * @return Lista obiektów typu {@link OneTimeUrl} z przekazanym kontem
     */
    List<OneTimeUrl> findByAccount(Account account);

    /**
     * Metoda wyszukująca listę encji typu {@link OneTimeUrl}, których czas wygaśnięcia minął.
     *
     * @return Lista obiektów typu {@link OneTimeUrl}, których czas wygaśnięcia minął
     */
    List<OneTimeUrl> findExpired();

    /**
     * Metoda wyszukująca listę encji typu {@link OneTimeUrl}, które zawieraja przekazany adres email.
     *
     * @return Lista obiektów typu {@link OneTimeUrl}, które zawierają przekazany adres email
     */
    List<OneTimeUrl> findByEmail();
}
