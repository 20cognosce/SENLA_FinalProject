package com.senla.controller.dto.selection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class GeolocationSelectionDto implements SelectionDto {

    private String countryCode;
    private String countryName;
    private String county;
    private String city;
    private String district;
    private String street;
    private String houseNumber;
    private String description;
}
