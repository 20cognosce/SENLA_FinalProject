package com.senla.domain.dto.creation;

import com.senla.domain.model.entity.Geolocation;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RentalPointCreationDto {

    private Geolocation geolocation;
}
