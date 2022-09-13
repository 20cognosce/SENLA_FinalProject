package com.senla.controller.dto;

import com.senla.model.entityenum.RideStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Setter
@Getter
public class RideDto {

    private Long id;
    private UserDto userDto;
    private ScooterDto scooterDto;

    private RideStatus status;
    private Double priceTotal;
    private RentalPointDto startRentalPointDto;
    private RentalPointDto endRentalPointDto;
    private LocalDateTime startTimestamp;
    private LocalDateTime endTimestamp;

    private Double rideMileage;
    private Duration rideDuration;
}
