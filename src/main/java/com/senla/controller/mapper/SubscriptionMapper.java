package com.senla.controller.mapper;

import com.senla.domain.dto.ScooterModelDto;
import com.senla.domain.dto.SubscriptionDto;
import com.senla.domain.dto.User2SubscriptionDto;
import com.senla.domain.dto.creation.SubscriptionCreationDto;
import com.senla.domain.dto.update.SubscriptionUpdateDto;
import com.senla.domain.model.entity.ScooterModel;
import com.senla.domain.model.entity.Subscription;
import com.senla.domain.model.entity.User2Subscription;
import com.senla.service.ScooterService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class SubscriptionMapper {

    private final ModelMapper modelMapper;
    private final Converter<List<Long>, List<ScooterModel>> modelsIdToModelsConverter;
    private final Converter<List<ScooterModel>, List<ScooterModelDto>> scooterModelsToDtoConverter;
    private final Converter<Subscription, SubscriptionDto> subscriptionToDtoConverter;

    public SubscriptionMapper(ModelMapper modelMapper, ScooterService scooterService, ScooterModelMapper scooterModelMapper) {
        this.modelMapper = modelMapper;

        modelsIdToModelsConverter = (src) -> src
                .getSource()
                .stream()
                .map(scooterService::getScooterModelById)
                .collect(toList());

        scooterModelsToDtoConverter = (src) -> src
                .getSource()
                .stream()
                .map(scooterModelMapper::convertToDto)
                .collect(toList());

        subscriptionToDtoConverter = (src) -> this.convertToSubscriptionDto(src.getSource());

        modelMapper.createTypeMap(SubscriptionCreationDto.class, Subscription.class)
                .addMappings(mapper -> mapper
                        .using(modelsIdToModelsConverter)
                        .map(SubscriptionCreationDto::getModels, Subscription::setModels));

        modelMapper.createTypeMap(SubscriptionUpdateDto.class, Subscription.class)
                .addMappings(mapper -> mapper
                        .using(modelsIdToModelsConverter)
                        .map(SubscriptionUpdateDto::getModels, Subscription::setModels));

        modelMapper.createTypeMap(Subscription.class, SubscriptionDto.class)
                .addMappings(mapper -> mapper
                        .using(scooterModelsToDtoConverter)
                        .map(Subscription::getModels, SubscriptionDto::setModels));

        modelMapper.createTypeMap(User2Subscription.class, User2SubscriptionDto.class)
                .addMappings(mapper -> mapper
                        .using(subscriptionToDtoConverter)
                        .map(User2Subscription::getSubscription, User2SubscriptionDto::setSubscription));
    }

    public Subscription convertToSubscription(SubscriptionCreationDto creationDto) {
        return modelMapper.map(creationDto, Subscription.class);
    }

    public Subscription convertToSubscription(SubscriptionUpdateDto updateDto) {
        return modelMapper.map(updateDto, Subscription.class);
    }

    public SubscriptionDto convertToSubscriptionDto(Subscription subscription) {
        return modelMapper.map(subscription, SubscriptionDto.class);
    }

    public User2SubscriptionDto convertToUser2SubscriptionDto(User2Subscription user2Subscription) {
        return modelMapper.map(user2Subscription, User2SubscriptionDto.class);
    }
}
