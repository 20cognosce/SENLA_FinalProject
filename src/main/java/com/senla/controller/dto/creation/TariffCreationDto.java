package com.senla.controller.dto.creation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class TariffCreationDto {

    private String name;
    private Double pricePerMinute;
    private String description;
    private List<Long> models;
}
