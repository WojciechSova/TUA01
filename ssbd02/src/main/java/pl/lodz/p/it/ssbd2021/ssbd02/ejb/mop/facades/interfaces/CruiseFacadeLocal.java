package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.AbstractFacadeInterface;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Route;

import javax.ejb.Local;
import java.sql.Timestamp;
import java.util.List;

/**
 * Interfejs encji modułu obsługi promów.
 * Używa konkretnego typu {@link Cruise} zamiast typu generycznego.
 * Jednostka składowania używana do wstrzyknięcia zarządcy encji do ssbd02mopPU.
 *
 * @author Daniel Łondka
 */
@Local
public interface CruiseFacadeLocal extends AbstractFacadeInterface<Cruise> {

    /**
     * Metoda wyszukująca encje typu {@link Cruise} o przekazanym biznesowym numerze identyfikacyjnym.
     *
     * @param number Biznesowy numer identyfikacyjny.
     * @return Obiekt typu {@link Cruise} o przekazanym biznesowym numerze identyfikacyjnym.
     */
    Cruise findByNumber(String number);

    /**
     * Metoda wyszukująca listę encji typu {@link Cruise}, które są na danej trasie.
     *
     * @param route Encja typu {@link Route}.
     * @return Listę obiektów typu {@link Cruise}, które są na danej trasie.
     */
    List<Cruise> findAllByRoute(Route route);

    /**
     * Metoda wyszukująca listę encji typu {@link Cruise}, które się nie zakończyły.
     *
     * @return Listę obiektów typu {@link Cruise}, które się nie zakończyły.
     */
    List<Cruise> findAllFutureDate();

    /**
     * Metoda wyszukująca listę encji typu {@link Cruise}, które zawierają encje typu {@link Ferry}, a ich czas przeprawy
     * odbywa się w określonym terminie.
     *
     * @param ferry     Encja typu {@link Ferry}
     * @param startDate Czas rozpoczęcia rejsu
     * @param endDate   Czas zakończenia rejsu
     * @return Listę obiektów typu {@link Cruise}, które zawierają encję typu {@link Ferry}
     * i odbywają się w określonym czasie.
     */
    List<Cruise> findAllUsingFerryInTime(Ferry ferry, Timestamp startDate, Timestamp endDate);
}
