package com.senla.controller.dto.creation;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SubscriptionCreationDto {

    private String name;
    private Double price;
    private Integer durationInDays = 30;
    private String description = "";
    private List<Long> models;
}
