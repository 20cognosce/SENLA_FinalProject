package com.senla.domain.dto.update;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TariffUpdateDto {

    private String name;
    private Double pricePerMinute;
    private String description;
    private List<Long> models;
}
