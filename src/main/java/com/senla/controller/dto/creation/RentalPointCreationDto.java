package com.senla.controller.dto.creation;

import com.senla.model.entity.Geolocation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class RentalPointCreationDto {

    private Geolocation geolocation;
}
