package com.senla.controller.dto;

import com.senla.model.entity.Geolocation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class RentalPointDto {

    private Long id;
    private Integer currentNumberOfScooters;

    private Geolocation geolocation;
    private Double distanceToClientInKm;

    private List<ScooterDto> scooters;
}
