package com.senla.service;

import com.senla.model.entity.User;

public interface UserService extends AbstractService<User> {

    void createUser(User user);
}
