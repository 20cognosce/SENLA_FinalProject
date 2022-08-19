package com.senla.controller;

import com.senla.controller.customexception.EntityNotFoundByIdException;
import com.senla.controller.dto.RentalPointDto;
import com.senla.controller.dto.selection.GeolocationSelectionDto;
import com.senla.controller.mapper.RentalPointMapper;
import com.senla.model.entity.Geolocation;
import com.senla.model.entity.RentalPoint;
import com.senla.service.RentalPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;


@RequiredArgsConstructor
@RequestMapping(value = "/v1/rental-points", produces = {"application/json; charset=UTF-8"})
@RestController
public class RentalPointController {

    private final RentalPointMapper rentalPointMapper;
    private final RentalPointService rentalPointService;

    @GetMapping
    public List<RentalPointDto> getAll(@RequestBody(required = false) GeolocationSelectionDto selectionModel,
                                       @RequestParam(value = "orderBy", defaultValue = "id", required = false) String orderBy,
                                       @RequestParam(value = "asc", defaultValue = "true", required = false) boolean asc,
                                       @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit) {

        Map<String, Object> selectParameters = rentalPointService.getMapOfObjectFieldsAndValues(selectionModel);
        selectParameters.entrySet().removeIf(entry -> Objects.isNull(entry.getValue()));

        List<Geolocation> selectedGeolocations = rentalPointService.getAllGeo(selectParameters, orderBy, asc, limit);
        /*return selectedGeolocations
                .stream()
                .map(geolocation -> {
                    Long id = geolocation.getRentalPoint().getId();
                    return rentalPointMapper.convertToDto(rentalPointService.getByIdWithScooters(id)
                                    .orElseThrow(() -> new EntityNotFoundByIdException(RentalPoint.class, id)));
                })
                .collect(toList());  //1000+ result lines if select Russia, so there is no reason to fetch scooters */
        return selectedGeolocations
                .stream()
                .map(geolocation -> rentalPointMapper.convertToDto(geolocation.getRentalPoint()))
                .collect(toList());
    }

    @GetMapping(value = "/{id}")
    public RentalPointDto getById(@PathVariable("id") Long id) {

        RentalPoint rentalPoint = rentalPointService.getByIdWithScooters(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(RentalPoint.class, id));
        return rentalPointMapper.convertToDto(rentalPoint);
    }
}
