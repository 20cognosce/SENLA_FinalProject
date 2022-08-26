package com.senla.model.entity;

import com.senla.model.entityenum.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ride")
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "user_id")
    @OneToOne
    private User user;
    @JoinColumn(name = "scooter_id")
    @OneToOne
    private Scooter scooter;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RideStatus status;
    @Column(name = "price")
    private Double price;

    @JoinColumn(name = "start_rental_point_id")
    @OneToOne
    RentalPoint startRentalPoint;

    @JoinColumn(name = "end_rental_point_id")
    @OneToOne
    private RentalPoint endRentalPoint;

    @Column(name = "start_timestamp")
    private LocalDateTime startTimestamp;
    @Column(name = "end_timestamp")
    private LocalDateTime endTimestamp;

    @Column(name = "ride_mileage")
    private Integer rideMileage;

    public Duration getRideDuration() {
        if (Objects.isNull(endTimestamp)) {
            return Duration.between(startTimestamp, LocalDateTime.now());
        } else {
            return Duration.between(startTimestamp, endTimestamp);
        }
    }
}
