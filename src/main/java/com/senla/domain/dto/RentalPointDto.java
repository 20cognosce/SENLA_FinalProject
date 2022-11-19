package com.senla.domain.dto;

import com.senla.domain.model.entity.Geolocation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RentalPointDto {

    private Long id;
    private Integer currentNumberOfScooters;

    private Geolocation geolocation;
    private Double distanceToClientInKm;

    private List<ScooterDto> scooters;
}
