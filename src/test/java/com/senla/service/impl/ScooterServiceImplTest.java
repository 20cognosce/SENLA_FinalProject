package com.senla.service.impl;

import com.senla.controller.customexception.EntityNotFoundByIdException;
import com.senla.dao.RentalPointDao;
import com.senla.dao.ScooterDao;
import com.senla.dao.ScooterModelDao;
import com.senla.model.entity.RentalPoint;
import com.senla.model.entity.Scooter;
import com.senla.model.entity.ScooterModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class ScooterServiceImplTest {

    @Mock
    private ScooterDao scooterDao;
    @Mock
    private ScooterModelDao scooterModelDao;
    @InjectMocks
    private ScooterServiceImpl scooterService;

    @Test
    void updateScooterModel() {
        Scooter scooter = new Scooter();
        ScooterModel scooterModel = new ScooterModel();

        scooterService.updateScooterModel(scooter, scooterModel);
        assertEquals(scooterModel, scooter.getModel());
    }

    @Test
    void updateScooterRentalPoint() {
        Scooter scooter = new Scooter();
        RentalPoint rentalPoint = new RentalPoint();

        scooterService.updateScooterRentalPoint(scooter, rentalPoint);
        assertEquals(rentalPoint, scooter.getRentalPoint());
    }

    @Test
    void getScooterModelById_EmptyOptionalReturnedFromDao_EntityNotFoundByIdExceptionThrown() {
        doReturn(Optional.empty()).when(scooterModelDao).getById(1L);
        assertThrows(EntityNotFoundByIdException.class, () -> scooterService.getScooterModelById(1L));
    }

    @Test
    void getScooterModelById_PresentOptionalReturnedFromDao_ScooterModelReturned() {
        ScooterModel scooterModel = new ScooterModel();
        doReturn(Optional.of(scooterModel)).when(scooterModelDao).getById(1L);
        assertEquals(scooterModel, scooterService.getScooterModelById(1L));
    }

    @Test
    void getDefaultDao() {
        assertEquals(scooterDao.getClass(), scooterService.getDefaultDao().getClass());
    }

    @Test
    void getDefaultEntityClass() {
        assertEquals(Scooter.class, scooterService.getDefaultEntityClass());
    }
}
