package com.senla.dao.impl;

import com.senla.dao.UserDao;
import com.senla.model.entity.User;
import com.senla.model.entityenum.UserAccountStatus;
import org.springframework.stereotype.Repository;

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
    public Optional<User> getById(long id) {
        Optional<User> optionalUser = Optional.ofNullable(entityManager.find(daoEntityClass(), id));

        if (optionalUser.isPresent()) {
            if (optionalUser.get().getStatus() == UserAccountStatus.DELETED) {
                return Optional.empty();
            }
        }

        return optionalUser;
    }

    @Override
    public List<User> getAll(Map<String, Object> mapOfFieldNamesAndValuesToSelectBy,
                          String orderBy,
                          boolean asc,
                          int limit) {
        mapOfFieldNamesAndValuesToSelectBy.put("status", UserAccountStatus.ACTIVE);
        return super.getAll(mapOfFieldNamesAndValuesToSelectBy, orderBy, asc, limit);
    }
}
