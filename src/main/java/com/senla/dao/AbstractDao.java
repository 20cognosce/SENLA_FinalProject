package com.senla.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.List;

public interface AbstractDao<T> {

	void create(T element);
	T getById(long id);
	void delete(T element);
	void update(T element);
	List<T> getAll(String fieldToSortBy, String ascDesc);
	List<Order> getOrderList(String ascDesc, String fieldToSortBy, CriteriaBuilder cb, Root<?> root);
}
