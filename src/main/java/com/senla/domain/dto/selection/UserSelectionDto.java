package com.senla.domain.dto.selection;

import com.senla.domain.model.entityenum.Role;
import com.senla.domain.model.entityenum.UserAccountStatus;
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
public class UserSelectionDto implements SelectionDto {

    private Long id;
    private String login;
    private Role role;

    private UserAccountStatus status;
    private String name;
    private String phone;
    private LocalDate dateOfBirth;
}
