package pl.lodz.p.it.ssbd2021.ssbd02.facades;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.List;

public abstract class AbstractFacade<T> {

    private final Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public T find(Object id){
        return getEntityManager().find(entityClass, id);
    }

    public void edit(T entity){
        getEntityManager().merge(entity);
    }

    public void remove(T entity){
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public List<T> findAll() {
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        TypedQuery<T> q = getEntityManager().createQuery(cq);
        q.setFirstResult(range[0]).setMaxResults(range[1] - range[0] + 1);
        return q.getResultList();
    }

    public int count() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(entityClass)));
        return (getEntityManager().createQuery(cq).getSingleResult()).intValue();
    }

    public List<T> findWithNamedQuery(String namedQuery) {
        return getEntityManager().createNamedQuery(namedQuery, entityClass).getResultList();
    }

    public List<T> findWithQuery(String query) {
        return getEntityManager().createQuery(query, entityClass).getResultList();
    }
}
