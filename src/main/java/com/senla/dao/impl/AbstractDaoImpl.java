package com.senla.dao.impl;

import com.senla.dao.AbstractDao;
import org.hibernate.query.criteria.internal.OrderImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractDaoImpl<T> implements AbstractDao<T> {

    @PersistenceContext
    protected EntityManager entityManager;

    protected abstract Class<T> daoEntityClass();

    @Override
    public void create(T element) {
        entityManager.persist(element);
    }

    @Override
    public Optional<T> getById(long id) {
        return Optional.ofNullable(entityManager.find(daoEntityClass(), id));
    }

    @Override
    public void update(T element) {
        entityManager.merge(element);
    }

    @Override
    public void delete(T element) {
        entityManager.remove(element);
    }

    @Override
    public List<T> getAll(Map<String, Object> mapOfFieldNamesAndValuesToSelectBy,
                          String orderBy,
                          boolean asc,
                          int limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(daoEntityClass());
        Root<T> tRoot = cq.from(daoEntityClass());

        Order order = new OrderImpl(tRoot.get(orderBy), asc);
        List<Predicate> predicates = new ArrayList<>();
        mapOfFieldNamesAndValuesToSelectBy.forEach((key, value) -> predicates.add(cb.equal(tRoot.get(key), value)));

        TypedQuery<T> query = entityManager.createQuery(cq
                .select(tRoot)
                .where(predicates.toArray(new Predicate[]{}))
                .orderBy(order)
        );

        return query
                .setMaxResults(limit)
                .getResultList();
    }
}
