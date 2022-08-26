package com.senla.controller.mapper;

import com.senla.controller.customexception.EntityNotFoundByIdException;
import com.senla.controller.dto.ScooterDto;
import com.senla.controller.dto.ScooterModelDto;
import com.senla.controller.dto.creation.ScooterCreationDto;
import com.senla.model.entity.Scooter;
import com.senla.model.entity.ScooterModel;
import com.senla.service.ScooterService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ScooterMapper {

    private final ModelMapper modelMapper;
    private final Converter<ScooterModel, ScooterModelDto> scooterModelDtoConverter;
    private final Converter<Long, ScooterModel> scooterIdToScooterModelConverter;

    public ScooterMapper(ModelMapper modelMapper,
                         ScooterModelMapper scooterModelMapper,
                         ScooterService scooterService) {

        this.modelMapper = modelMapper;

        scooterModelDtoConverter = (src) -> scooterModelMapper.convertToDto(src.getSource());

        scooterIdToScooterModelConverter = (src) -> {
            Long id = src.getSource();
            return scooterService.getScooterModelById(id)
                    .orElseThrow(() -> new EntityNotFoundByIdException(id, ScooterModel.class));
        };

        modelMapper.createTypeMap(Scooter.class, ScooterDto.class)
                .addMapping(scooter -> scooter.getRentalPoint().getId(), ScooterDto::setRentalPointId)
                .addMappings(mapper -> mapper
                        .using(scooterModelDtoConverter)
                        .map(Scooter::getModel, ScooterDto::setModel));

        modelMapper.createTypeMap(ScooterCreationDto.class, Scooter.class)
                .addMappings(mapper -> mapper
                        .using(scooterIdToScooterModelConverter)
                        .map(ScooterCreationDto::getModelId, Scooter::setModel))
                .addMappings(mapper -> mapper
                        .map((scooterCreationDto) -> null, Scooter::setId));
    }

    public ScooterDto convertToDto(Scooter entity) {
        return modelMapper.map(entity, ScooterDto.class);
    }

    public Scooter convertToScooter(ScooterCreationDto dto) {
        return modelMapper.map(dto, Scooter.class);
    }
}
