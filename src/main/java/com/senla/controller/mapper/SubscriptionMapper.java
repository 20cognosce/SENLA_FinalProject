package com.senla.controller.mapper;

import com.senla.controller.dto.creation.SubscriptionCreationDto;
import com.senla.controller.dto.update.SubscriptionUpdateDto;
import com.senla.model.entity.ScooterModel;
import com.senla.model.entity.Subscription;
import com.senla.service.ScooterService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class SubscriptionMapper {

    private final ModelMapper modelMapper;

    public SubscriptionMapper(ModelMapper modelMapper, ScooterService scooterService) {
        this.modelMapper = modelMapper;

        Converter<List<Long>, List<ScooterModel>> modelsIdToModelsConverter = (src) -> src
                .getSource()
                .stream()
                .map(scooterService::getScooterModelById)
                .collect(toList());

        modelMapper.createTypeMap(SubscriptionCreationDto.class, Subscription.class)
                .addMappings(mapper -> mapper
                        .using(modelsIdToModelsConverter)
                        .map(SubscriptionCreationDto::getModels, Subscription::setModels));

        modelMapper.createTypeMap(SubscriptionUpdateDto.class, Subscription.class)
                .addMappings(mapper -> mapper
                        .using(modelsIdToModelsConverter)
                        .map(SubscriptionUpdateDto::getModels, Subscription::setModels));
    }

    public Subscription convertToSubscription(SubscriptionCreationDto creationDto) {
        return modelMapper.map(creationDto, Subscription.class);
    }

    public Subscription convertToSubscription(SubscriptionUpdateDto updateDto) {
        return modelMapper.map(updateDto, Subscription.class);
    }
}
