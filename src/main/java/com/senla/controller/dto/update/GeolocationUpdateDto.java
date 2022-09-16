package com.senla.controller.dto.update;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GeolocationUpdateDto {

    private Double latitude;
    private Double longitude;
    private String countryCode;
    private String countryName;
    private String county;
    private String city;
    private String district;
    private String street;
    private String houseNumber;
    private String description;
}
