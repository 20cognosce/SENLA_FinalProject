package com.senla.controller.dto.update;

import com.senla.model.entityenum.ScooterConditionStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ScooterUpdateDto {

    private ScooterConditionStatus status;
    private Double charge;
}
