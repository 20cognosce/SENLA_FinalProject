package com.senla.dao.impl;

import com.senla.dao.AbstractDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractDaoImpl<T> implements AbstractDao<T> {

	@PersistenceContext
	protected EntityManager entityManager;

	protected abstract Class<T> daoEntityClass();

	@Override
	public void create(T element) {
		entityManager.persist(element);
	}

	@Override
	public T getById(long id) {
		return entityManager.find(daoEntityClass(), id);
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
	public List<T> getAll(String fieldToSortBy, String ascDesc) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(daoEntityClass());
		Root<T> tRoot = cq.from(daoEntityClass());
		List<Order> orderList = getOrderList(ascDesc, fieldToSortBy, cb, tRoot);
		TypedQuery<T> query = entityManager.createQuery(cq
				.select(tRoot)
				.orderBy(orderList));

		return query.getResultList();
	}

	@Override
	public List<Order> getOrderList(String ascDesc, String fieldToSortBy, CriteriaBuilder cb, Root<?> root) {
		List<Order> orderList = new ArrayList<>();
		if (Objects.equals(ascDesc, "asc")) {
			orderList.add(cb.asc(root.get(fieldToSortBy)));
		}
		if (Objects.equals(ascDesc, "desc")) {
			orderList.add(cb.desc(root.get(fieldToSortBy)));
		}
		return orderList;
	}
}
