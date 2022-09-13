package com.senla.controller.dto.update;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class UserCredentialsUpdateDto {

    String login;
    String password;
}
