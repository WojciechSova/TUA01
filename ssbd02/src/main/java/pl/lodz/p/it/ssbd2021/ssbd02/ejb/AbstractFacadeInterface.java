package pl.lodz.p.it.ssbd2021.ssbd02.ejb;

import javax.ejb.Local;
import java.util.List;

@Local
public interface AbstractFacadeInterface<T> {

    void create(T entity);

    T find(Object id);

    void edit(T entity);

    void remove(T entity);

    List<T> findAll();

    List<T> findInRange(int start, int end);

    int count();

    List<T> findWithNamedQuery(String namedQuery);

    List<T> findWithQuery(String query);
}
