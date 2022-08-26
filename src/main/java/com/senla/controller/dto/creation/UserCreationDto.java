package com.senla.controller.dto.creation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserCreationDto {

    private String login;
    private String password;

    private String name;
    private String phone;
    private LocalDate dateOfBirth;
}
