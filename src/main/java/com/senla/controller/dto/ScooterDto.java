package com.senla.controller.dto;

import com.senla.model.entityenum.ScooterConditionStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class ScooterDto {

    private Long id;
    private ScooterModelDto model;
    private ScooterConditionStatus status;
    private Double charge;
    private Double mileage;

    private Long rentalPointId;
}
