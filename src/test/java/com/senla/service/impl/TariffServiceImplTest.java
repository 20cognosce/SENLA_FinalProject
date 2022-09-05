package com.senla.service.impl;

import com.senla.dao.TariffDao;
import com.senla.model.entity.RentalPoint;
import com.senla.model.entity.Scooter;
import com.senla.model.entity.Tariff;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TariffServiceImplTest {

    @Mock
    private TariffDao tariffDao;
    @InjectMocks
    private TariffServiceImpl tariffService;

    @Test
    void calculateRidePricePerMinute() {
        Tariff tariff = new Tariff();
        tariff.setPricePerMinute(13.13);

        Double calculatedPrice = tariffService.calculateRidePricePerMinute(new RentalPoint(), tariff, new Scooter());

        assertTrue(calculatedPrice >= tariff.getPricePerMinute());
    }

    @Test
    void getDefaultDao() {
        assertEquals(tariffDao.getClass(), tariffService.getDefaultDao().getClass());
    }

    @Test
    void getDefaultEntityClass() {
        assertEquals(Tariff.class, tariffService.getDefaultEntityClass());
    }
}