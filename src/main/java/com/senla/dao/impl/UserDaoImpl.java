package com.senla.dao.impl;

import com.senla.dao.UserDao;
import com.senla.model.entity.User;
import com.senla.model.entityenum.Role;
import com.senla.model.entityenum.UserAccountStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public List<User> getAll(Map<String, Object> mapOfFieldNamesAndValuesToSelectBy,
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

        return Optional.ofNullable(q.getSingleResult());
    }

    @Override
    public Optional<User> getUserByPhone(String phone) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> user = cq.from(User.class);
        TypedQuery<User> q = entityManager.createQuery(cq
                .select(user)
                .where(cb.equal(user.get("phone"), phone)));

        return Optional.ofNullable(q.getSingleResult());
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
