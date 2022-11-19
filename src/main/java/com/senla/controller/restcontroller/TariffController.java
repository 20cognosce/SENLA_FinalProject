package com.senla.controller.restcontroller;

import com.senla.domain.dto.TariffDto;
import com.senla.domain.dto.creation.TariffCreationDto;
import com.senla.domain.dto.update.TariffUpdateDto;
import com.senla.controller.mapper.TariffMapper;
import com.senla.domain.model.entity.Tariff;
import com.senla.service.TariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@RequestMapping(value = "/v1/tariffs", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class TariffController {

    private final TariffService tariffService;
    private final TariffMapper tariffMapper;

    @GetMapping("/{id}")
    public TariffDto getTariffById(@PathVariable Long id) {
        return tariffMapper.convertToDto(tariffService.getById(id));
    }

    @GetMapping
    public List<TariffDto> getAllTariffs() {
        return tariffService.getAll(new HashMap<>(), "id", true, 100)
                .stream().map(tariffMapper::convertToDto).collect(toList());
    }

    @PostMapping
    public void createTariff(@RequestBody TariffCreationDto creationDto) {
        Tariff tariff = tariffMapper.convertToTariff(creationDto);
        tariffService.create(tariff);
    }

    @PatchMapping("/{id}")
    public void updateTariff(@PathVariable Long id, @RequestBody TariffUpdateDto updateDto) {
        Tariff tariff = tariffService.getById(id);
        Tariff updatedTariff = tariffMapper.convertToTariff(updateDto);
        tariffService.updateEntityFromDto(tariff, updatedTariff, Tariff.class);
        tariffService.update(tariff);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteTariff(@PathVariable("id") Long id) {
        tariffService.deleteById(id);
    }
}
