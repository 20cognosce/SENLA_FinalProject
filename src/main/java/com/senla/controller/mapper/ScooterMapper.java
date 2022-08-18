package com.senla.controller.mapper;

import com.senla.controller.dto.ScooterDto;
import com.senla.model.entity.Scooter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ScooterMapper {

    private final ModelMapper modelMapper;

    public ScooterMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        modelMapper.createTypeMap(Scooter.class, ScooterDto.class)
                .addMapping(scooter -> scooter.getRentalPoint().getId(), ScooterDto::setRentalPointId);
    }

    public ScooterDto convertToDto(Scooter entity) {
        return modelMapper.map(entity, ScooterDto.class);
    }

    public Scooter convertToUser(ScooterDto dto) {
        return modelMapper.map(dto, Scooter.class);
    }
}
