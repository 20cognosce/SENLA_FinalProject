package com.senla.controller.mapper;

import com.senla.domain.dto.RentalPointDto;
import com.senla.domain.dto.ScooterDto;
import com.senla.domain.dto.creation.RentalPointCreationDto;
import com.senla.domain.model.entity.RentalPoint;
import com.senla.domain.model.entity.Scooter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class RentalPointMapper {

    private final ModelMapper modelMapper;
    private final Converter<List<Scooter>, List<ScooterDto>> scootersToScootersDtoConverter;
    private final Converter<List<Scooter>, Integer> scootersToNumberOfScootersConverter;

    public RentalPointMapper(ModelMapper modelMapper, ScooterMapper scooterMapper) {
        this.modelMapper = modelMapper;

        scootersToScootersDtoConverter = (src) -> src
                .getSource()
                .stream()
                .map(scooterMapper::convertToDto)
                .collect(toList());

        scootersToNumberOfScootersConverter = (src) -> src
                .getSource()
                .size();

        modelMapper.createTypeMap(RentalPoint.class, RentalPointDto.class)
                .addMappings(mapper -> mapper
                        .using(scootersToScootersDtoConverter)
                        .map(RentalPoint::getScooters, RentalPointDto::setScooters))
                .addMappings(mapper -> mapper
                        .using(scootersToNumberOfScootersConverter)
                        .map(RentalPoint::getScooters, RentalPointDto::setCurrentNumberOfScooters));
    }

    public RentalPointDto convertToDto(RentalPoint entity) {
        return modelMapper.map(entity, RentalPointDto.class);
    }

    public RentalPoint convertToRentalPoint(RentalPointCreationDto dto) {
        return modelMapper.map(dto, RentalPoint.class);
    }
}
