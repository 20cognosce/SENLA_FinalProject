package com.senla.dao.impl;

import com.senla.dao.UserDao;
import com.senla.domain.model.entity.User;
import com.senla.domain.model.entityenum.Role;
import com.senla.domain.model.entityenum.UserAccountStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class UserDaoImpl extends AbstractDaoImpl<User> implements UserDao {

    @Override
    protected Class<User> daoEntityClass() {
        return User.class;
    }

    @Override
    public Optional<User> getById(Long id) {
        return Optional.ofNullable(entityManager.find(daoEntityClass(), id));
    }

    @Override
    public List<User> getAll(@NonNull Map<String, Object> mapOfFieldNamesAndValuesToSelectBy,
                             String orderBy,
                             boolean asc,
                             int limit) {
        mapOfFieldNamesAndValuesToSelectBy.put("status", UserAccountStatus.ACTIVE);
        return super.getAll(mapOfFieldNamesAndValuesToSelectBy, orderBy, asc, limit);
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> user = cq.from(User.class);
        TypedQuery<User> q = entityManager.createQuery(cq
                .select(user)
                .where(cb.equal(user.get("login"), login)));

        User result = null;
        try {
            result = q.getSingleResult();
        } catch (NoResultException e) {
            log.info("User with login " + login + " not found, empty optional returned", e);
        }

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<User> getUserByPhone(String phone) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> user = cq.from(User.class);
        TypedQuery<User> q = entityManager.createQuery(cq
                .select(user)
                .where(cb.equal(user.get("phone"), phone)));

        User result = null;
        try {
            result = q.getSingleResult();
        } catch (NoResultException e) {
            log.info("User with phone " + phone + " not found, empty optional returned", e);
        }

        return Optional.ofNullable(result);
    }

    @Override
    public boolean isRootCreated() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<User> user = cq.from(User.class);
        TypedQuery<Long> q = entityManager.createQuery(cq
                .select(cb.count(user))
                .where(cb.equal(user.get("role"), Role.ROOT)));

        return q.getSingleResult() > 0;
    }
}
