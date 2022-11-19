package com.senla.domain.dto.update;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class UserUpdateDto {

    private String name;
    private String phone;
    private LocalDate dateOfBirth;
}