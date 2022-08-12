package com.senla.model.entity;

import com.senla.model.enums.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @OneToOne
    User user;
    @OneToOne
    Scooter scooter;

    @Enumerated(EnumType.STRING)
    RideStatus rideStatus;

    @OneToOne
    RentalPoint startRentalPoint;
    @OneToOne
    RentalPoint endRentalPoint;

    LocalDateTime startTimestamp;
    LocalDateTime endTimestamp;

    Integer rideMileage;

    public Duration getRideDuration() {
        if (Objects.isNull(endTimestamp)) {
            return Duration.between(startTimestamp, LocalDateTime.now());
        } else {
            return Duration.between(startTimestamp, endTimestamp);
        }
    }
}
