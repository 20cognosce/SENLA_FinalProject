package com.senla.service;

import com.senla.model.entity.User;

public interface UserService extends AbstractService<User> {

    boolean isRootCreated();
    User getUserByLogin(String login);

    boolean isLoginIncorrect(String login);
    boolean isPasswordIncorrect(String password);
    boolean isPhoneIncorrect(String phone);

    boolean isLoginUnavailable(String login);
    boolean isPhoneUnavailable(String phone);
}
