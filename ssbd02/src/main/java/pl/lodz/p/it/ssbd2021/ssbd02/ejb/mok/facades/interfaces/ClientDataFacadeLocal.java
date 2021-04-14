package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacadeInterface;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.ClientData;

import javax.ejb.Local;

/**
 * Interfejs encji modułu obsługi kont.
 * Używa konkretnego typu {@link ClientData} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji to ssbd02mokPU.
 *
 * @author Daniel Łondka
 */
@Local
public interface ClientDataFacadeLocal extends AbstractFacadeInterface<ClientData> {

}
