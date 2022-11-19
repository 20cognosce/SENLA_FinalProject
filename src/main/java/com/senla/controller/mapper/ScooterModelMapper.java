package com.senla.controller.mapper;

import com.senla.domain.dto.ScooterModelDto;
import com.senla.domain.model.entity.ScooterModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ScooterModelMapper {

    private final ModelMapper modelMapper;

    public ScooterModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ScooterModelDto convertToDto(ScooterModel entity) {
        return modelMapper.map(entity, ScooterModelDto.class);
    }
}
