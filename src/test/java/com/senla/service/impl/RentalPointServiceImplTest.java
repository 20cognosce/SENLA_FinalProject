package com.senla.service.impl;

import com.senla.controller.customexception.EntityNotFoundByIdException;
import com.senla.dao.GeolocationDao;
import com.senla.dao.RentalPointDao;
import com.senla.model.entity.Geolocation;
import com.senla.model.entity.RentalPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RentalPointServiceImplTest {

    @Mock
    private RentalPointDao rentalPointDao;
    @Mock
    private GeolocationDao geolocationDao;
    @InjectMocks
    private RentalPointServiceImpl rentalPointService;

    @Test
    void getByIdWithScooters_EmptyOptionalReturnedFromDao_EntityNotFoundByIdExceptionThrown() {
        doReturn(Optional.empty()).when(rentalPointDao).getByIdWithScooters(1L);
        assertThrows(EntityNotFoundByIdException.class, () -> rentalPointService.getByIdWithScooters(1L));
    }

    @Test
    void getByIdWithScooters_PresentOptionalReturnedFromDao_GetByIdWithScootersDaoMethodInvoked() {
        doReturn(Optional.of(new RentalPoint())).when(rentalPointDao).getByIdWithScooters(1L);
        rentalPointService.getByIdWithScooters(1L);
        verify(rentalPointDao).getByIdWithScooters(1L);
    }

    @Test
    void getAllGeo() {
        rentalPointService.getAllGeo(new HashMap<>(), "id", true, 15);
        verify(geolocationDao).getAll(new HashMap<>(), "id", true, 15);
    }

    @Test
    void getAllTheClosest() {
        rentalPointService.getAllTheClosest(133.33435, 47.51634, 15);
        verify(rentalPointDao).getAllTheClosest(133.33435, 47.51634, 15);
    }


    @Test
    void getDistanceToClientInKm() {
        Geolocation geolocation = Geolocation.builder()
                .latitude(59.93991)
                .longitude(30.31451)
                .build();
        RentalPoint rentalPoint = new RentalPoint();
        rentalPoint.setGeolocation(geolocation);

        //Distance between Red Square and Hermitage, i.e. there is about 600km between Moscow and Saint-Petersburg
        assertEquals(634.682, rentalPointService.getDistanceToClientInKm(rentalPoint, 55.75174, 37.61872));
    }

    @Test
    void getDefaultDao() {
        assertEquals(rentalPointDao.getClass(), rentalPointService.getDefaultDao().getClass());
    }

    @Test
    void getDefaultEntityClass() {
        assertEquals(RentalPoint.class, rentalPointService.getDefaultEntityClass());
    }
}