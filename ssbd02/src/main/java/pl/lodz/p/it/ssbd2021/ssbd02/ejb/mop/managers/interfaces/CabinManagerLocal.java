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
     * Metoda wyszukująca wszystkie kajuty, które zawierają się na promie o podanym kodzie.
     *
     * @param code Kod promu, po którym chcemy wyszukać
     * @return Lista kajut {@link Cabin}, które znajdują się na promie o podanym kodzie
     */
    List<Cabin> getAllCabinsByFerryCode(String code);

    /**
     * Metoda wyszukująca wszystkie kajuty, które znajdują się na promie o podanym kodzie i są wybranego typu.
     *
     * @param code      Kod promu, po którym chcemy wyszukać
     * @param cabinType Nazwa typu kajuty
     * @return Lista kajut {@link Cabin}, które znajdują się na promie o podanym kodzie i są wybranego typu
     */
    List<Cabin> getAllCabinsByFerryCodeAndCabinType(String code, String cabinType);

    /**
     * Metoda wyszukująca kajutę o podanym numerze.
     *
     * @param number Numer kajuty, którą chcemy wyszukać
     * @return Encja typu {@link Cabin}
     */
    Cabin getCabinByNumber(String number);

    /**
     * Metoda tworząca kajutę.
     *
     * @param cabin     Encja typu {@link Cabin}
     * @param createdBy Login użytkownika, który utworzył encję
     * @param ferryName Nazwa promu, do którego zostanie przypisana kajuta
     */
    void createCabin(Cabin cabin, String createdBy, String ferryName);

    /**
     * Metoda aktualizuje kajutę o numerze zawartym w encji {@link Cabin} oraz ustawia konto w polu modifiedBy na konto
     * użytkownika dokonującego zmiany.
     *
     * @param cabin      Encja typu {@link Cabin}
     * @param modifiedBy Login użytkownika, który edytuje encje
     */
    void updateCabin(Cabin cabin, String modifiedBy);

    /**
     * Metoda usuwa kajutę o numerze zawartym w encji {@link Cabin}.
     *
     * @param cabin Encja typu {@link Cabin}
     */
    void removeCabin(Cabin cabin);
}
