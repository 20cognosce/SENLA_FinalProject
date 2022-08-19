package com.senla.controller.dto;

import com.senla.model.entity.ScooterModel;
import com.senla.model.entityenum.ScooterConditionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScooterDto {

    Long id;
    ScooterModel model;
    ScooterConditionStatus status;
    Double charge;
    Double mileage;

    Long rentalPointId;
}
