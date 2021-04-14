package pl.lodz.p.it.ssbd2021.ssbd02.ejb;

import javax.ejb.Local;
import java.util.List;

/**
 * Interfejs oferujący podstawowe operacje na encjach.
 *
 * @param <T>
 * @author Daniel Łondka
 */
@Local
public interface AbstractFacadeInterface<T> {

    /**
     * Metoda odpowiadająca ze dołączenie encji do kontekstu utrwalania i przejście encji w stan zarządzania
     *
     * @param entity Encja typu T
     */
    void create(T entity);

    /**
     * Metoda wyszukująca encję danej klasy o danej wartości klucza głównego
     *
     * @param id Klucz główny
     * @return Encja typu T w przypadku zgodności wartości klucza głównego,
     * null gdy brak encji o podanej wartości klucza głównego
     */
    T find(Object id);

    /**
     * Metoda synchronizująca stan obiektu w kontekście utrwalania, opowiada za utworzenie kopii przekazanej encji,
     * i dołączenie jej do kontektu utrwalania
     *
     * @param entity Encja typu T
     */
    void edit(T entity);

    /**
     * Metoda usuwająca encję
     *
     * @param entity Encja typu T
     */
    void remove(T entity);

    /**
     * Metoda wyszukująca wszystkie encje danego typu
     *
     * @return Kolekcja wszystkich encji typu T
     */
    List<T> findAll();

    /**
     * Metoda pobierająca encje danego typu z podanego przedziału
     *
     * @param start Początek przedziału
     * @param end Koniec przedziału (włącznie)
     * @return Kolekcja encji typu T z przekazanego przedziału
     */
    List<T> findInRange(int start, int end);

    /**
     * Metoda zwracająca liczbę wszystkich encji danego typu
     *
     * @return Liczba wszystkich encji typu T
     */
    int count();

    /**
     * Metoda wyszukująca encje na podstawie przekazanego zapytania nazwanego
     *
     * @param namedQuery Zapytanie nazwane
     * @return Kolekcja encji typu T
     */
    List<T> findWithNamedQuery(String namedQuery);

    /**
     * Metoda wyszukująca encje na podstawie przekazanego zapytania
     *
     * @param query Zapytanie
     * @return Kolekcja encji typu T
     */
    List<T> findWithQuery(String query);
}
