package com.senla.domain.dto.creation;

import com.senla.domain.model.entityenum.ScooterConditionStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ScooterCreationDto {

    private Long modelId;
    private ScooterConditionStatus status = ScooterConditionStatus.UNAVAILABLE;
    private Double charge = 0d;
    private Double mileage = 0d;
}
