package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;

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
     * Metoda wyszukująca wszystkie wolne kajuty dla danego rejsu
     *
     * @param number Numer rejsu
     * @return Lista wolnych kajut {@link Cabin} na danym rejsie
     */
    List<Cabin> getFreeCabinsOnCruise(String number);

    /**
     * Metoda wyszukująca kajutę o podanym numerze, znajdującej się na podanym promie.
     *
     * @param ferryName   Nazwa promu, na którym znajduje się kajuta
     * @param cabinNumber Numer kajuty, którą chcemy wyszukać
     * @return Encja typu {@link Cabin}
     */
    Cabin getCabinByFerryAndNumber(String ferryName, String cabinNumber);

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
     * @param ferryName Nazwa promu, do którego przypisana jest modyfikowana kajuta
     */
    void updateCabin(Cabin cabin, String modifiedBy, String ferryName);

    /**
     * Metoda usuwa kajutę o numerze zawartym w encji {@link Cabin}.
     *
     * @param number    Numer biznesowy usuwanej kajuty
     * @param removedBy Login użytkownika, który usunął encję
     */
    void removeCabin(String number, String removedBy);

    /**
     * Metoda obliczająca popularność rejsu
     *
     * @param cruise Rejs, dla którego liczony jest agregat
     * @return Procentowa wartość popularności rejsu
     */
    double calculatePopularity(Cruise cruise);
}
