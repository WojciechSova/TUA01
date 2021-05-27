package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacadeInterface;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.CabinType;

import javax.ejb.Local;

/**
 * Interfejs encji modułu obsługi promów.
 * Używa konkretnego typu {@link CabinType} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji do ssbd02mopPU.
 *
 * @author Daniel Łondka
 */
@Local
public interface CabinTypeFacadeLocal extends AbstractFacadeInterface<CabinType> {

    /**
     * Metoda wyszukująca encje typu {@link CabinType} o przekazanej biznesowej nazwie identyfikacyjnej.
     *
     * @param name Biznesowa nazwa identyfikacyjna.
     * @return Obiekt typu {@link CabinType} o przekazanej biznesowej nazwie identyfikacyjnej.
     */
    CabinType findByName(String name);
}
