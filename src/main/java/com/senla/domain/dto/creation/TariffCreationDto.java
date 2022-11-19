package com.senla.domain.dto.creation;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TariffCreationDto {

    private String name;
    private Double pricePerMinute;
    private String description = "";
    private List<Long> models;
}
