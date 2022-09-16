package com.senla.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TariffDto {

    private Long id;
    private String name;
    private Double pricePerMinute;
    private String description;
    private List<ScooterModelDto> models;
}
