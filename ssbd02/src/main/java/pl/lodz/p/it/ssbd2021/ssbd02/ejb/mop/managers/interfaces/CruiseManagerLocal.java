package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;

import javax.ejb.Local;
import java.util.List;

/**
 * Lokalny interfejs managera rejsów
 *
 * @author Wojciech Sowa
 */
@Local
public interface CruiseManagerLocal {

    /**
     * Metoda wyszukująca wszystkie rejsy.
     *
     * @return Lista rejsów {@link Cruise}
     */
    List<Cruise> getAllCruises();

    /**
     * Metoda wyszukująca rejs o podanym kodzie
     *
     * @param code Kod rejsu, który chcemy wyszukać
     * @return Encja typu {@link Cruise}
     */
    Cruise getCruiseByCode(String code);

    /**
     * Metoda tworząca rejs
     *
     * @param cruise Encja typu {@link Cruise}
     */
    void createCruise(Cruise cruise);

    /**
     * Metoda aktualizuje rejs o kodzie zawartym w encji {@link Cruise} oraz ustawia konto w polu modifiedBy na konto
     * użytkownika dokonującego zmiany
     *
     * @param cruise      Encja typu {@link Cruise}
     * @param modifiedBy Login użytkownika, który edytuje encje
     */
    void updateCruise(Cruise cruise, String modifiedBy);

    /**
     * Metoda usuwa rejs o kodzie zawartym w encji {@link Cruise}
     *
     * @param cruise Encja typu {@link Cruise}
     */
    void removeCruise(Cruise cruise);
}
