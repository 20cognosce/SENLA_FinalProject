package com.senla.domain.dto.update;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserCredentialsUpdateDto {

    String login;
    String password;
}
