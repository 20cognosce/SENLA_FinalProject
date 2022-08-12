package com.senla.model.entity;

import com.senla.model.enums.ScooterConditionStatus;
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

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Scooter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String manufacturer;
    String model;

    @Enumerated(EnumType.STRING)
    ScooterConditionStatus status;

    Double chargePercentage;
    Integer mileage;

    @OneToOne
    RentalPoint rentalPoint;
}
