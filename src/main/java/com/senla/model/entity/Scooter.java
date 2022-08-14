package com.senla.model.entity;

import com.senla.model.enums.ScooterConditionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "scooter")
public class Scooter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "manufacturer")
    String manufacturer;
    @Column(name = "model")
    String model;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    ScooterConditionStatus status;

    @Column(name = "charge")
    Float chargePercentage;
    @Column(name = "mileage")
    Integer mileage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rental_point_id")
    RentalPoint rentalPoint;
}
