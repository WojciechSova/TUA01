package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Route;

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
     * Metoda wyszukująca wszystkie aktualne rejsy.
     *
     * @return Lista aktualnych rejsów {@link Cruise}
     */
    List<Cruise> getAllCurrentCruises();

    /**
     * Metoda wyszukująca rejs o podanym numerze.
     *
     * @param number Numer rejsu, który chcemy wyszukać
     * @return Encja typu {@link Cruise}
     */
    Cruise getCruiseByNumber(String number);

    /**
     * Metoda tworząca rejs.
     *
     * @param cruise Encja typu {@link Cruise}
     * @param name Identyfikator encji typu {@link Ferry}
     * @param code Identyfikator encji typu {@link Route}
     * @param login Login użytkownika, który tworzy encje
     */
    void createCruise(Cruise cruise, String name, String code, String login);

    /**
     * Metoda aktualizuje rejs o numerze zawartym w encji {@link Cruise} oraz ustawia konto w polu modifiedBy na konto
     * użytkownika dokonującego zmiany.
     *
     * @param cruise     Encja typu {@link Cruise}
     * @param modifiedBy Login użytkownika, który edytuje encje
     */
    void updateCruise(Cruise cruise, String modifiedBy);

    /**
     * Metoda usuwa rejs o numerze cruiseNumber.
     *
     * @param cruiseNumber Numer identyfikujący rejs typu {@link String}
     * @param userLogin Login użytkownika, który usuwa encje {@link String}
     */
    void removeCruise(String cruiseNumber, String userLogin);

    /**
     * Metoda obliczająca popularność rejsu
     *
     * @param cruise Rejs, dla którego liczony jest agregat
     * @return Procentowa wartość popularności rejsu
     */
    double calculatePopularity(Cruise cruise);
}
