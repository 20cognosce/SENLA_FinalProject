package com.senla.controller.mapper;

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

    public TariffMapper(ModelMapper modelMapper, ScooterService scooterService) {
        this.modelMapper = modelMapper;

        Converter<List<Long>, List<ScooterModel>> modelsIdToModelsConverter = (src) -> src
                .getSource()
                .stream()
                .map(scooterService::getScooterModelById)
                .collect(toList());

        modelMapper.createTypeMap(TariffCreationDto.class, Tariff.class)
                .addMappings(mapper -> mapper
                        .using(modelsIdToModelsConverter)
                        .map(TariffCreationDto::getModels, Tariff::setModels));

        modelMapper.createTypeMap(TariffUpdateDto.class, Tariff.class)
                .addMappings(mapper -> mapper
                        .using(modelsIdToModelsConverter)
                        .map(TariffUpdateDto::getModels, Tariff::setModels));
    }

    public Tariff convertToTariff(TariffCreationDto creationDto) {
        return modelMapper.map(creationDto, Tariff.class);
    }

    public Tariff convertToTariff(TariffUpdateDto updateDto) {
        return modelMapper.map(updateDto, Tariff.class);
    }
}
