package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacadeInterface;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;

import javax.ejb.Local;

/**
 * Interfejs encji modułu obsługi promów.
 * Używa konkretnego typu {@link Ferry} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji do ssbd02mopPU.
 *
 * @author Daniel Łondka
 */
@Local
public interface FerryFacadeLocal extends AbstractFacadeInterface<Ferry> {

    /**
     * Metoda wyszukująca encje typu {@link Ferry} o przekazanej nazwie promu.
     *
     * @param name Nazwa promu.
     * @return Obiekt typu {@link Ferry} o przekazanej nazwie promu.
     */
    Ferry findByName(String name);
}
