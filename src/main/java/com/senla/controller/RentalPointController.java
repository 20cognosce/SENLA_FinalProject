package com.senla.controller;

import com.senla.controller.custom_exceptions.EntityNotFoundByIdException;
import com.senla.controller.dto.RentalPointDto;
import com.senla.controller.mapper.RentalPointMapper;
import com.senla.model.entity.RentalPoint;
import com.senla.service.RentalPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping(value = "/v1/rental-points", produces = {"application/json; charset=UTF-8"})
@RestController
public class RentalPointController {

    private final RentalPointMapper rentalPointMapper;
    private final RentalPointService rentalPointService;

    /*@GetMapping(params = {"fieldToOrderBy", "asc", "limit"})
    public List<RentalPointDto> getAll(@RequestBody RentalPointSelectionDto selectionModel,
                                       @RequestParam(value = "fieldToOrderBy", defaultValue = "id", required = false) String fieldToOrderBy,
                                       @RequestParam(value = "asc", defaultValue = "true", required = false) boolean asc,
                                       @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit) {

        Map<String, Object> selectParameters = rentalPointService.getMapOfObjectFieldsAndValues(selectionModel);
        selectParameters.entrySet().removeIf(entry -> Objects.isNull(entry.getValue()));

        List<RentalPoint> rentalPoints = rentalPointService.getAll(selectParameters, fieldToOrderBy, asc, limit);
        return rentalPoints.stream().map(rentalPointMapper::convertToDto).collect(toList());
    }*/

    @GetMapping(value = "/{id}")
    public RentalPointDto getById(@PathVariable("id") Long id) {

        RentalPoint rentalPoint = rentalPointService.getByIdWithScooters(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(RentalPoint.class, id));
        return rentalPointMapper.convertToDto(rentalPoint);
    }

    @GetMapping
    public Integer greeting(@RequestParam("id") Long id) {
        return id.intValue();
    }
}
