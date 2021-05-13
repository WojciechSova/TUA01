package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacadeInterface;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.OneTimeUrl;

import javax.ejb.Local;

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
}
