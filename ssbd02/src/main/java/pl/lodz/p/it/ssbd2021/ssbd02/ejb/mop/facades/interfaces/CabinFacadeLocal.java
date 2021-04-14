package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacadeInterface;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;

import javax.ejb.Local;

/**
 * Interfejs encji modułu obsługi promów.
 * Używa konkretnego typu {@link Cabin} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji do ssbd02mopPU.
 *
 * @author Daniel Łondka
 */
@Local
public interface CabinFacadeLocal extends AbstractFacadeInterface<Cabin> {

    /**
     * Metoda wyszukująca encje typu {@link Cabin} o przekazanym biznesowym numerze identyfikacyjnym.
     *
     * @param number Biznesowy numer identyfikacyjny.
     * @return Obiekt typu {@link Cabin} o przekazanym biznesowym numerze identyfikacyjnym.
     */
    Cabin findByNumber(String number);
}
