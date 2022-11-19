package com.senla.dao;

import com.senla.domain.model.entity.User;

import java.util.Optional;

public interface UserDao extends AbstractDao<User> {

    Optional<User> getUserByLogin(String login);
    Optional<User> getUserByPhone(String phone);

    boolean isRootCreated();
}
