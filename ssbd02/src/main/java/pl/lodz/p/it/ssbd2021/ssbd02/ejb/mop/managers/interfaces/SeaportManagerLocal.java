package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Seaport;

import javax.ejb.Local;
import java.util.List;

/**
 * Lokalny interfejs managera portów
 *
 * @author Wojciech Sowa
 */
@Local
public interface SeaportManagerLocal {

    /**
     * Metoda wyszukująca wszystkie porty.
     *
     * @return Lista portów {@link Seaport}
     */
    List<Seaport> getAllSeaports();

    /**
     * Metoda wyszukująca port o podanym kodzie.
     *
     * @param code Kod portu, który chcemy wyszukać
     * @return Encja typu {@link Seaport}
     */
    Seaport getSeaportByCode(String code);

    /**
     * Metoda tworząca port.
     *
     * @param login   Login użytkownika, który stworzył port
     * @param seaport Encja typu {@link Seaport}
     */
    void createSeaport(String login, Seaport seaport);

    /**
     * Metoda aktualizuje port o kodzie zawartym w encji {@link Seaport} oraz ustawia konto w polu modifiedBy na konto
     * użytkownika dokonującego zmiany.
     *
     * @param seaport    Encja typu {@link Seaport}
     * @param modifiedBy Login użytkownika, który edytuje encje
     */
    void updateSeaport(Seaport seaport, String modifiedBy);

    /**
     * Metoda usuwa port o kodzie zawartym w encji {@link Seaport}.
     *
     * @param seaportCode Encja typu {@link Seaport}
     * @param userLogin Login użytkownika usuwającego port
     */
    void removeSeaport(String seaportCode, String userLogin);
}
