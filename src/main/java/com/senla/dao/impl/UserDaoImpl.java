package com.senla.dao.impl;

import lombok.Getter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Getter
@Repository
public class UserDaoImpl {

	@PersistenceContext
	private EntityManager entityManager;
}
