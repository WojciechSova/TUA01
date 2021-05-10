package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacadeInterface;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;

import javax.ejb.Local;
import java.util.List;

/**
 * Interfejs encji modułu obsługi kont.
 * Używa konkretnego typu {@link AccessLevel} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji to ssbd02mokPU.
 *
 * @author Daniel Łondka
 */
@Local
public interface AccessLevelFacadeLocal extends AbstractFacadeInterface<AccessLevel> {

    /**
     * Metoda wyszukująca encje typu {@link AccessLevel}, w których znajduje się dowiązanie do encji typu {@link Account} o przekazanym loginie.
     *
     * @param login Login encji.
     * @return Lista obiektów typu {@link AccessLevel} o przekazanym loginie.
     */
    List<AccessLevel> findByLogin(String login);

    /**
     * Metoda wyszukująca encje typu {@link AccessLevel}, w których znajduje się dowiązanie do przekazanej encji typu {@link Account}
     *
     * @param account Konto którego poziomy dostępu wyszukujemy
     * @return Lista obiektów typu {@link AccessLevel}, z dowiązaniem do przekazanego konta
     */
    List<AccessLevel> findAllByAccount(Account account);

     /**
     * Metoda wyszukująca encje typu {@link AccessLevel}, reprezentujące aktywne poziomy dostępu,
     * w których znajduje się dowiązanie do przekazanej encji typu {@link Account}
     *
     * @param account Konto którego poziomy dostępu wyszukujemy
     * @return Lista obiektów typu {@link AccessLevel}, z dowiązaniem do przekazanego konta
     */
    List<AccessLevel> findAllActiveByAccount(Account account);
}
