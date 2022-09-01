package com.senla.controller.mapper;

import com.senla.controller.dto.TariffDto;
import com.senla.controller.dto.User2SubscriptionDto;
import com.senla.controller.dto.UserDto;
import com.senla.controller.dto.creation.UserCreationDto;
import com.senla.model.entity.Tariff;
import com.senla.model.entity.User;
import com.senla.model.entity.User2Subscription;
import com.senla.model.entityenum.Role;
import com.senla.model.entityenum.UserAccountStatus;
import com.senla.service.TariffService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;
    private final Converter<String, String> passwordToHashcodeConverter;
    private final Converter<Tariff, TariffDto> tariffToDtoConverter;
    private final Converter<User2Subscription, User2SubscriptionDto> user2SubscriptionToDtoConverter;

    public UserMapper(ModelMapper modelMapper, PasswordEncoder passwordEncoder, TariffService tariffService,
                      TariffMapper tariffMapper, SubscriptionMapper subscriptionMapper) {
        this.modelMapper = modelMapper;

        passwordToHashcodeConverter = (src) -> passwordEncoder.encode(src.getSource());
        tariffToDtoConverter = (src) -> tariffMapper.convertToDto(src.getSource());
        user2SubscriptionToDtoConverter = (src) -> subscriptionMapper.convertToUser2SubscriptionDto(src.getSource());

        modelMapper.createTypeMap(UserCreationDto.class, User.class)
                .addMappings(mapper -> mapper
                        .using(passwordToHashcodeConverter)
                        .map(UserCreationDto::getPassword, User::setHashPassword))
                .addMappings(mapper -> mapper
                        .map((userCreationDto) -> tariffService.getById(1L), User::setTariff))
                .addMappings(mapper -> mapper
                        .map((userCreationDto) -> Role.USER, User::setRole))
                .addMappings(mapper -> mapper
                        .map((userCreationDto) -> UserAccountStatus.ACTIVE, User::setStatus));

        modelMapper.createTypeMap(User.class, UserDto.class)
                .addMappings(mapper -> mapper
                        .using(tariffToDtoConverter)
                        .map(User::getTariff, UserDto::setTariff))
                .addMappings(mapper -> mapper
                        .using(user2SubscriptionToDtoConverter)
                        .map(User::getUser2Subscription, UserDto::setUser2Subscription));
    }

    public UserDto convertToDto(User entity) {
        return modelMapper.map(entity, UserDto.class);
    }

    public User convertToUser(UserCreationDto dto) {
        return modelMapper.map(dto, User.class);
    }
}
