package com.senla.controller.dto.update;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class UserUpdateDto {

    private String name;
    private String phone;
    private LocalDate dateOfBirth;

    private String creditCard; //TODO: encryption
}
