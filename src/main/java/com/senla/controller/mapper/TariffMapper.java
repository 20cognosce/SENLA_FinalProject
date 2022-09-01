package com.senla.controller.mapper;

import com.senla.controller.dto.ScooterModelDto;
import com.senla.controller.dto.TariffDto;
import com.senla.controller.dto.creation.TariffCreationDto;
import com.senla.controller.dto.update.TariffUpdateDto;
import com.senla.model.entity.ScooterModel;
import com.senla.model.entity.Tariff;
import com.senla.service.ScooterService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class TariffMapper {

    private final ModelMapper modelMapper;

    public TariffMapper(ModelMapper modelMapper, ScooterService scooterService, ScooterModelMapper scooterModelMapper) {
        this.modelMapper = modelMapper;

        Converter<List<Long>, List<ScooterModel>> modelsIdToModelsConverter = (src) -> src
                .getSource()
                .stream()
                .map(scooterService::getScooterModelById)
                .collect(toList());

        Converter<List<ScooterModel>, List<ScooterModelDto>> modelsToDtoConverter = (src) -> src
                .getSource()
                .stream()
                .map(scooterModelMapper::convertToDto)
                .collect(toList());

        modelMapper.createTypeMap(TariffCreationDto.class, Tariff.class)
                .addMappings(mapper -> mapper
                        .using(modelsIdToModelsConverter)
                        .map(TariffCreationDto::getModels, Tariff::setModels));

        modelMapper.createTypeMap(TariffUpdateDto.class, Tariff.class)
                .addMappings(mapper -> mapper
                        .using(modelsIdToModelsConverter)
                        .map(TariffUpdateDto::getModels, Tariff::setModels));

        modelMapper.createTypeMap(Tariff.class, TariffDto.class)
                .addMappings(mapper -> mapper
                        .using(modelsToDtoConverter)
                        .map(Tariff::getModels, TariffDto::setModels));
    }

    public Tariff convertToTariff(TariffCreationDto creationDto) {
        return modelMapper.map(creationDto, Tariff.class);
    }

    public Tariff convertToTariff(TariffUpdateDto updateDto) {
        return modelMapper.map(updateDto, Tariff.class);
    }

    public TariffDto convertToDto(Tariff tariff) {
        return modelMapper.map(tariff, TariffDto.class);
    }
}
