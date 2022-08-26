package com.senla.controller.dto.selection;

import com.senla.model.entityenum.Role;
import com.senla.model.entityenum.UserAccountStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Setter
@Getter
public class UserSelectionDto implements SelectionDto {

    private Long id;
    private String login;
    private Role role;

    private UserAccountStatus status;
    private String name;
    private String phone;
    private LocalDate dateOfBirth;
}
