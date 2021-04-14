package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacadeInterface;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.ClientData;

import javax.ejb.Local;

/**
 * Interfejs encji modułu obsługi promów.
 * Używa konkretnego typu {@link ClientData} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji to ssbd02mopPU.
 *
 * @author Daniel Łondka
 */
@Local
public interface ClientDataMopFacadeLocal extends AbstractFacadeInterface<ClientData> {

    /**
     * Metoda wyszukująca encję typu {@link ClientData} o przekazanym loginie.
     *
     * @param login Login konta.
     * @return Obiekt typu {@link ClientData} o przekazanym loginie.
     */
    ClientData findByLogin(String login);
}
