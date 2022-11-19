package com.senla.domain.dto;

import com.senla.domain.model.entityenum.ScooterConditionStatus;
import lombok.Getter;
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
