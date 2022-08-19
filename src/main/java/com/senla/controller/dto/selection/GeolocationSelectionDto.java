package com.senla.controller.dto.selection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GeolocationSelectionDto {

    String countryCode;
    String countryName;
    String county;
    String city;
    String district;
    String street;
    String houseNumber;
    String description;
}
