package com.senla.controller.restcontroller;

import com.senla.domain.dto.ScooterDto;
import com.senla.domain.dto.creation.ScooterCreationDto;
import com.senla.domain.dto.update.ScooterUpdateDto;
import com.senla.controller.mapper.ScooterMapper;
import com.senla.domain.model.entity.RentalPoint;
import com.senla.domain.model.entity.Scooter;
import com.senla.domain.model.entity.ScooterModel;
import com.senla.service.RentalPointService;
import com.senla.service.ScooterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/v1/scooters", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class ScooterController {

    private final ScooterService scooterService;
    private final RentalPointService rentalPointService;
    private final ScooterMapper scooterMapper;

    @GetMapping
    public List<ScooterDto> getAll(@RequestParam(value = "orderBy", defaultValue = "id", required = false) String orderBy,
                                @RequestParam(value = "asc", defaultValue = BooleanUtils.TRUE, required = false) boolean asc,
                                @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit) {

        List<Scooter> scooters = scooterService.getAll(new HashMap<>(), orderBy, asc, limit);
        return scooters.stream().map(scooterMapper::convertToDto).collect(toList());
    }

    @GetMapping(value = "/{id}")
    public ScooterDto getById(@PathVariable("id") Long id) {
        Scooter scooter = scooterService.getById(id);
        return scooterMapper.convertToDto(scooter);
    }

    @PostMapping
    public void createScooter(@RequestBody ScooterCreationDto scooterCreationDto) {
        Scooter scooter = scooterMapper.convertToScooter(scooterCreationDto);
        scooterService.create(scooter);
    }

    @PatchMapping(value = "/{id}")
    public void updateScooter(@PathVariable("id") Long id, @RequestBody ScooterUpdateDto updateDto) {
        Scooter scooter = scooterService.getById(id);
        scooterService.updateEntityFromDto(scooter, updateDto, Scooter.class);
        scooterService.update(scooter);
    }

    @PatchMapping(value = "/{id}", params = {"model-id"})
    public void updateScooterModel(@PathVariable("id") Long id, @RequestParam(value = "model-id") Long modelId) {
        Scooter scooter = scooterService.getById(id);
        ScooterModel scooterModel = scooterService.getScooterModelById(modelId);
        scooterService.updateScooterModel(scooter, scooterModel);
    }

    @PatchMapping(value = "/{id}", params = {"rental-point-id"})
    public void updateScooterRentalPoint(@PathVariable("id") Long id,
                                         @RequestParam(value = "rental-point-id") Long rentalPointId) {
        Scooter scooter = scooterService.getById(id);
        RentalPoint rentalPoint = rentalPointService.getById(rentalPointId);
        scooterService.updateScooterRentalPoint(scooter, rentalPoint);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteScooter(@PathVariable("id") Long id) {
        scooterService.deleteById(id);
    }
}
