package com.senla.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenDto {

    private String login;
    private String value;

    public TokenDto(String login, String value) {
        this.login = login;
        this.value = value;
    }
}