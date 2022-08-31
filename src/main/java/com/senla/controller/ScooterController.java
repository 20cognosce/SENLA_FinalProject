package com.senla.controller;

import com.senla.controller.customexception.EntityNotFoundByIdException;
import com.senla.controller.dto.ScooterDto;
import com.senla.controller.dto.creation.ScooterCreationDto;
import com.senla.controller.dto.update.ScooterUpdateDto;
import com.senla.controller.mapper.ScooterMapper;
import com.senla.model.entity.Scooter;
import com.senla.service.ScooterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/v1/scooters", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class ScooterController {

    private final ScooterService scooterService;
    private final ScooterMapper scooterMapper;

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

    @DeleteMapping(value = "/{id}")
    public void deleteScooter(@PathVariable("id") Long id) {
        scooterService.deleteById(id);
    }

    @PatchMapping(value = "/{id}")
    public void updateScooter(@PathVariable("id") Long id, @RequestBody ScooterUpdateDto updateModel) {
        Scooter scooter = scooterService.getById(id);
        scooterService.updateEntityFromDto(scooter, updateModel, Scooter.class);
        scooterService.update(scooter);
    }

    @PatchMapping(value = "/{id}", params = {"model-id"})
    public void updateScooterModel(@PathVariable("id") Long id, @RequestParam(value = "model-id") Long modelId) {
        try {
            scooterService.updateScooterModel(id, modelId);
        } catch (EntityNotFoundByIdException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @PatchMapping(value = "/{id}", params = {"rental-point-id"})
    public void updateScooterRentalPoint(@PathVariable("id") Long id,
                                         @RequestParam(value = "rental-point-id") Long rentalPointId) {
        //TODO: in other methods
        try {
            scooterService.updateScooterRentalPoint(id, rentalPointId);
        } catch (EntityNotFoundByIdException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
