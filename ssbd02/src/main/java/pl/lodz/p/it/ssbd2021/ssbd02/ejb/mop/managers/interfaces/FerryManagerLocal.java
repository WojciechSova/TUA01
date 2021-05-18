package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;

import javax.ejb.Local;
import java.util.List;

/**
 * Lokalny interfejs managera promów
 *
 * @author Wojciech Sowa
 */
@Local
public interface FerryManagerLocal {

    /**
     * Metoda wyszukująca wszystkie promy.
     *
     * @return Lista promów {@link Ferry}
     */
    List<Ferry> getAllFerries();

    /**
     * Metoda wyszukująca prom o podanym kodzie
     *
     * @param code Kod promu, który chcemy wyszukać
     * @return Encja typu {@link Ferry}
     */
    Ferry getFerryByCode(String code);

    /**
     * Metoda tworząca prom
     *
     * @param ferry Encja typu {@link Ferry}
     */
    void createFerry(Ferry ferry);

    /**
     * Metoda aktualizuje prom o kodzie zawartym w encji {@link Ferry} oraz ustawia konto w polu modifiedBy na konto
     * użytkownika dokonującego zmiany
     *
     * @param ferry    Encja typu {@link Ferry}
     * @param modifiedBy Login użytkownika, który edytuje encje
     */
    void updateFerry(Ferry ferry, String modifiedBy);

    /**
     * Metoda usuwa prom o kodzie zawartym w encji {@link Ferry}
     *
     * @param ferry Encja typu {@link Ferry}
     */
    void removeFerry(Ferry ferry);
}
