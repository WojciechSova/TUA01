package pl.lodz.p.it.ssbd2021.ssbd02.facades;

import lombok.AllArgsConstructor;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.List;

/**
 * Klasa abstrakcyjna implementująca podstawowe operacje na encjach
 *
 * @param <T>
 * @author Julia Kołodziej
 */

@AllArgsConstructor
public abstract class AbstractFacade<T> {

    private final Class<T> entityClass;

    protected abstract EntityManager getEntityManager();

    /**
     * Metoda odpowiadająca ze dołączenie encji do kontekstu utrwalania i przejście encji w stan zarządzania
     *
     * @param entity Encja typu T
     */
    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    /**
     * Metoda wyszukująca encję danej klasy o danej wartości klucza głównego
     *
     * @param id Klucz główny
     * @return Encja typu T w przypadku zgodności wartości klucza głównego,
     * null gdy brak encji o podanej wartości klucza głównego
     */
    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    /**
     * Metoda synchronizująca stan obiektu w kontekście utrwalania, opowiada za utworzenie kopii przekazanej encji,
     * i dołączenie jej do kontektu utrwalania
     *
     * @param entity Encja typu T
     */
    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    /**
     * Metoda usuwająca encję
     *
     * @param entity Encja typu T
     */
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    /**
     * Metoda wyszukująca wszystkie encje danego typu
     *
     * @return Kolekcja wszystkich encji typu T
     */
    public List<T> findAll() {
        CriteriaQuery<T> criteriaQuery = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);
        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }

    /**
     * Metoda pobierająca encje danego typu z podanego zakresu
     *
     * @param range Zakres
     * @return Kolekcja encji typu T z przekazanego zakresu
     */
    public List<T> findInRange(int[] range) {
        CriteriaQuery<T> criteriaQuery = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);
        TypedQuery<T> query = getEntityManager().createQuery(criteriaQuery);
        query.setFirstResult(range[0]).setMaxResults(range[1] - range[0] + 1);
        return query.getResultList();
    }

    /**
     * Metoda zwracająca liczbę wszystkich encji danego typu
     *
     * @return Liczba wszystkich encji typu T
     */
    public int count() {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(criteriaBuilder.count(root));
        return (getEntityManager().createQuery(criteriaQuery).getSingleResult()).intValue();
    }

    /**
     * Metoda wyszukująca encje na podstawie przekazanego zapytania nazwanego
     *
     * @param namedQuery Zapytanie nazwane
     * @return Kolekcja encji typu T
     */
    public List<T> findWithNamedQuery(String namedQuery) {
        return getEntityManager().createNamedQuery(namedQuery, entityClass).getResultList();
    }

    /**
     * Metoda wyszukująca encje na podstawie przekazanego zapytania
     *
     * @param query Zapytanie
     * @return Kolekcja encji typu T
     */
    public List<T> findWithQuery(String query) {
        return getEntityManager().createQuery(query, entityClass).getResultList();
    }
}
