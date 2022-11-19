package com.senla.domain.dto;

import com.senla.domain.model.entityenum.Role;
import com.senla.domain.model.entityenum.UserAccountStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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
