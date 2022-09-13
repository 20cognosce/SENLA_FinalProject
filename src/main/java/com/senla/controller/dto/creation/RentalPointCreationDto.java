package com.senla.controller.dto.creation;

import com.senla.model.entity.Geolocation;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RentalPointCreationDto {

    private Geolocation geolocation;
}
