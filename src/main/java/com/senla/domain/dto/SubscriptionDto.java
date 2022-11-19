package com.senla.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SubscriptionDto {

    private Long id;
    private String name;
    private Double price;
    private Integer durationInDays;
    private String description;

    private List<ScooterModelDto> models;
}
