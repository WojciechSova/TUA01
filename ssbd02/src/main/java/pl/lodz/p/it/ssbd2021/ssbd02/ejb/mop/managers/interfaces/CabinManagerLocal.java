package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;

import javax.ejb.Local;
import java.util.List;

/**
 * Lokalny interfejs managera kajut
 *
 * @author Wojciech Sowa
 */
@Local
public interface CabinManagerLocal {

    /**
     * Metoda wyszukująca wszystkie kajuty.
     *
     * @return Lista kajut {@link Cabin}
     */
    List<Cabin> getAllCabins();

    /**
     * Metoda wyszukująca kajutę o podanym kodzie
     *
     * @param code Kod kajuty, którą chcemy wyszukać
     * @return Encja typu {@link Cabin}
     */
    Cabin getCabinByCode(String code);

    /**
     * Metoda tworząca kajutę
     *
     * @param cabin Encja typu {@link Cabin}
     */
    void createCabin(Cabin cabin);

    /**
     * Metoda aktualizuje kajutę o kodzie zawartym w encji {@link Cabin} oraz ustawia konto w polu modifiedBy na konto
     * użytkownika dokonującego zmiany
     *
     * @param cabin      Encja typu {@link Cabin}
     * @param modifiedBy Login użytkownika, który edytuje encje
     */
    void updateCabin(Cabin cabin, String modifiedBy);

    /**
     * Metoda usuwa kajutę o kodzie zawartym w encji {@link Cabin}
     *
     * @param cabin Encja typu {@link Cabin}
     */
    void removeCabin(Cabin cabin);
}
