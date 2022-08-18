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
    Long id;

    @Column(name = "latitude")
    Double latitude;
    @Column(name = "longitude")
    Double longitude;

    @Column(name = "country_code")
    String countryCode;
    @Column(name = "country_name")
    String countryName;
    @Column(name = "county")
    String county;
    @Column(name = "city")
    String city;
    @Column(name = "district")
    String district;
    @Column(name = "street")
    String street;
    @Column(name = "house_number")
    String houseNumber;
    @Column(name = "description")
    String description;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "geolocation", cascade = CascadeType.ALL)
    RentalPoint rentalPoint;
}
