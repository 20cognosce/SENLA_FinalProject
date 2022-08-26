package com.senla.controller.dto.creation;

import com.senla.model.entityenum.ScooterConditionStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ScooterCreationDto {

    private Long modelId;
    private ScooterConditionStatus status = ScooterConditionStatus.UNAVAILABLE;
    private Double charge = 0d;
    private Double mileage = 0d;
}
