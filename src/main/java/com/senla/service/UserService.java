package com.senla.service;

import com.senla.domain.model.entity.User;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

public interface UserService extends AbstractService<User> {

    boolean isRootCreated();
    User getUserByLogin(@NonNull String login);

    boolean isLoginIncorrect(@NonNull String login);
    boolean isPasswordIncorrect(@NonNull String password);
    boolean isPhoneIncorrect(@NonNull String phone);
    boolean isAgeUnder18(@NonNull LocalDate dateOfBirth);

    boolean isLoginUnavailable(String login);
    boolean isPhoneUnavailable(String phone);
}
