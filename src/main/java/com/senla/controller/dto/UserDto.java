package com.senla.controller.dto;

import com.senla.model.entity.Tariff;
import com.senla.model.entity.User2Subscription;
import com.senla.model.entity_enum.Role;
import com.senla.model.entity_enum.UserAccountStatus;

import java.time.LocalDate;

public class UserDto {

    Long id;
    String login;
    String password; //TODO: receive but do not send
    Role role;

    UserAccountStatus status;
    String name;
    String phone;
    LocalDate dateOfBirth;

    Tariff tariff;
    User2Subscription user2Subscription;
}
