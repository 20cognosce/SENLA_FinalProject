package com.senla.controller.dto.update;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class SubscriptionUpdateDto {

    private String name;
    private Double price;
    private Integer durationInDays;
    private String description;
    private List<Long> models;
}
