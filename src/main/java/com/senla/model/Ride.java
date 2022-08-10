package com.senla.model;

import lombok.Builder;

import javax.persistence.Transient;
import java.time.Duration;
import java.time.LocalDateTime;

@Builder
public class Ride {

    User user;

    RentalPoint departureRentalPoint;
    RentalPoint destinationRentalPoint;

    LocalDateTime departureTimestamp;
    LocalDateTime destinationTimestamp;

    @Transient
    Duration rideDuration;

}
