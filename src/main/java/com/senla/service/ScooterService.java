package com.senla.service;

import com.senla.domain.model.entity.RentalPoint;
import com.senla.domain.model.entity.Scooter;
import com.senla.domain.model.entity.ScooterModel;

public interface ScooterService extends AbstractService<Scooter> {

    void updateScooterModel(Scooter scooter, ScooterModel scooterModel);

    void updateScooterRentalPoint(Scooter scooter, RentalPoint rentalPoint);

    ScooterModel getScooterModelById(Long id);
}
