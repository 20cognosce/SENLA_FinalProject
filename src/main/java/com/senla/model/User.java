package com.senla.model;


import com.senla.model.enums.Role;
import com.senla.model.enums.UserAccountStatus;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public class User {

    Long id;
    String login;
    String hashPassword;
    Role role;

    UserAccountStatus accountStatus;
    String name;
    String phoneNumber; // regex, validation
    LocalDate dateOfBirth; // >14

}
