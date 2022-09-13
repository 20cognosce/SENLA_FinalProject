package com.senla.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenDto {

    private String login;
    private String value;
}