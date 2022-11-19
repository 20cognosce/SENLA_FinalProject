package com.senla.domain.dto.update;

import com.senla.domain.model.entityenum.ScooterConditionStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ScooterUpdateDto {

    private ScooterConditionStatus status;
    private Double charge;
}
