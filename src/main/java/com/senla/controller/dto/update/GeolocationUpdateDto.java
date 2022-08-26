package com.senla.controller.dto.update;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class GeolocationUpdateDto {

    private String countryCode;
    private String countryName;
    private String county;
    private String city;
    private String district;
    private String street;
    private String houseNumber;
    private String description;
}
