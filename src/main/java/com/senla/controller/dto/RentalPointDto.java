package com.senla.controller.dto;

import com.senla.model.entity.Geolocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RentalPointDto {

    Long id;
    Integer currentNumberOfScooters;

    Geolocation geolocation;

    List<ScooterDto> scooters;
}
