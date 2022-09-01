package com.senla.controller.dto;

import com.senla.model.entityenum.Role;
import com.senla.model.entityenum.UserAccountStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Setter
@Getter
public class UserDto {

    private Long id;
    private Role role;

    private UserAccountStatus status;
    private String name;
    private String phone;
    private LocalDate dateOfBirth;

    private TariffDto tariff;
    private User2SubscriptionDto user2Subscription;
}
