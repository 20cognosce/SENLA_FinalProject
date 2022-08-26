package com.senla.controller.mapper;

import com.senla.controller.dto.UserDto;
import com.senla.controller.dto.creation.UserCreationDto;
import com.senla.model.entity.User;
import com.senla.model.entityenum.Role;
import com.senla.model.entityenum.UserAccountStatus;
import com.senla.service.PaymentService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;
    private final Converter<String, String> passwordToHashcodeConverter;

    public UserMapper(ModelMapper modelMapper, PasswordEncoder passwordEncoder, PaymentService paymentService) {
        this.modelMapper = modelMapper;

        passwordToHashcodeConverter = (src) -> passwordEncoder.encode(src.getSource());

        modelMapper.createTypeMap(UserCreationDto.class, User.class)
                .addMappings(mapper -> mapper
                        .using(passwordToHashcodeConverter)
                        .map(UserCreationDto::getPassword, User::setHashPassword))
                .addMappings(mapper -> mapper
                        .map((userCreationDto) -> paymentService.getTariffById(1L), User::setTariff))
                .addMappings(mapper -> mapper
                        .map((userCreationDto) -> Role.USER, User::setRole))
                .addMappings(mapper -> mapper
                        .map((userCreationDto) -> UserAccountStatus.ACTIVE, User::setStatus));
    }

    public UserDto convertToDto(User entity) {
        return modelMapper.map(entity, UserDto.class);
    }

    public User convertToUser(UserCreationDto dto) {
        return modelMapper.map(dto, User.class);
    }
}
