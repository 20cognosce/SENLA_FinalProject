package com.senla.controller.dto.selection;

import com.senla.model.entityenum.Role;
import com.senla.model.entityenum.UserAccountStatus;

import java.time.LocalDate;

public class UserSelectionDto {

    Long id;
    String login;
    Role role;

    UserAccountStatus status;
    String name;
    String phone;
    LocalDate dateOfBirth;
}
