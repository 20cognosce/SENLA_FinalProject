package com.senla.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

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
    private Long id;

    @Column(name = "manufacturer")
    private String manufacturer;
    @Column(name = "model")
    private String name;
    @Column(name = "scooter_weight")
    private Integer scooterWeight;
    @Column(name = "max_weight_limit")
    private Integer maxWeightLimit;
    @Column(name = "max_speed")
    private Integer maxSpeed;
    @Column(name = "max_range")
    private Integer maxRange;
    @Column(name = "price")
    private Double price;

    @JsonIgnore
    @OneToMany(mappedBy = "model", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    List<Scooter> scooters;
}
