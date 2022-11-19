package com.senla.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "geolocation")
public class Geolocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "latitude")
    private Double latitude = 0d;
    @Column(name = "longitude")
    private Double longitude = 0d;

    @Column(name = "country_code")
    private String countryCode = "";

    @Column(name = "country_name")
    private String countryName = "";

    @Column(name = "county")
    private String county = "";

    @Column(name = "city")
    private String city = "";

    @Column(name = "district")
    private String district = "";

    @Column(name = "street")
    private String street = "";

    @Column(name = "house_number")
    private String houseNumber = "";

    @Column(name = "description")
    private String description = "";

    @JsonIgnore
    @OneToOne(mappedBy = "geolocation", cascade = CascadeType.ALL)
    private RentalPoint rentalPoint;
}
