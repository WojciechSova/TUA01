package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacadeInterface;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Route;

import javax.ejb.Local;

/**
 * Interfejs encji modułu obsługi promów.
 * Używa konkretnego typu {@link Route} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji do ssbd02mopPU.
 *
 * @author Daniel Łondka
 */
@Local
public interface RouteFacadeLocal extends AbstractFacadeInterface<Route> {

    /**
     * Metoda wyszukująca encje typu {@link Route} o przekazanym kodzie trasy.
     *
     * @param code Kod trasy.
     * @return Obiekt typu {@link Route} o przekazanym kodzie trasy.
     */
    Route findByCode(String code);
}
