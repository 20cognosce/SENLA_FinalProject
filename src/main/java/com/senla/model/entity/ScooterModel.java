package com.senla.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "scooter_model")
public class ScooterModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "manufacturer")
    String manufacturer;
    @Column(name = "model")
    String name;
    @Column(name = "scooter_weight")
    Integer scooterWeight;
    @Column(name = "max_weight_limit")
    Integer maxWeightLimit;
    @Column(name = "max_speed")
    Integer maxSpeed;
    @Column(name = "max_range")
    Integer maxRange;
    @Column(name = "price")
    Double price;
}
