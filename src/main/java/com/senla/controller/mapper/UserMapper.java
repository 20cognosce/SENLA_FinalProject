package com.senla.controller.mapper;

import com.senla.controller.dto.UserDto;
import com.senla.model.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDto convertToDto(User entity) {
        return modelMapper.map(entity, UserDto.class);
    }

    public User convertToUser(UserDto dto) {
        return modelMapper.map(dto, User.class);
    }
}
