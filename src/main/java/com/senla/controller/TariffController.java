package com.senla.controller;

import com.senla.controller.dto.update.TariffUpdateDto;
import com.senla.controller.mapper.TariffMapper;
import com.senla.model.entity.Tariff;
import com.senla.service.TariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/v1/tariffs", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class TariffController {

    private final TariffService tariffService;
    private final TariffMapper tariffMapper;

    @GetMapping("/{id}")
    public Tariff getTariffById(@PathVariable Long id) {
        return tariffService.getById(id);
    }

    @GetMapping
    public List<Tariff> getAllTariffs() {
        return tariffService.getAll(new HashMap<>(), "id", true, 10);
    }

    @PatchMapping("/{id}")
    public void updateTariff(@PathVariable Long id, @RequestBody TariffUpdateDto updateDto) {
        Tariff tariff = tariffService.getById(id);
        Tariff updatedTariff = tariffMapper.convertToTariff(updateDto);
        tariffService.updateEntityFromDto(tariff, updatedTariff, Tariff.class);
        tariffService.update(tariff);
    }
}
