package com.senla.controller.mapper;

import com.senla.controller.dto.RentalPointDto;
import com.senla.controller.dto.RideDto;
import com.senla.controller.dto.ScooterDto;
import com.senla.controller.dto.UserDto;
import com.senla.model.entity.RentalPoint;
import com.senla.model.entity.Ride;
import com.senla.model.entity.Scooter;
import com.senla.model.entity.User;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RideMapper {

    private final ModelMapper modelMapper;

    public RideMapper(ModelMapper modelMapper, UserMapper userMapper,
                      ScooterMapper scooterMapper, RentalPointMapper rentalPointMapper) {
        this.modelMapper = modelMapper;

        Converter<User, UserDto> userToDtoConverter = (src) -> userMapper.convertToDto(src.getSource());
        Converter<Scooter, ScooterDto> scooterToDtoConverter = (src) -> scooterMapper.convertToDto(src.getSource());
        Converter<RentalPoint, RentalPointDto> rentalPointToDtoConverter = (src) -> rentalPointMapper.convertToDto(src.getSource());

        modelMapper.createTypeMap(Ride.class, RideDto.class)
                .addMappings(mapper -> mapper
                        .using(userToDtoConverter)
                        .map(Ride::getUser, RideDto::setUserDto))
                .addMappings(mapper -> mapper
                        .using(scooterToDtoConverter)
                        .map(Ride::getScooter, RideDto::setScooterDto))
                .addMappings(mapper -> mapper
                        .using(rentalPointToDtoConverter)
                        .map(Ride::getStartRentalPoint, RideDto::setStartRentalPointDto))
                .addMappings(mapper -> mapper
                        .using(rentalPointToDtoConverter)
                        .map(Ride::getEndRentalPoint, RideDto::setEndRentalPointDto))
                .addMapping(Ride::getRideDuration, RideDto::setRideDuration);
    }

    public RideDto convertToDto(Ride entity) {
        return modelMapper.map(entity, RideDto.class);
    }
}
