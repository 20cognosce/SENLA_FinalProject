package com.senla.service;

import com.senla.model.entity.RentalPoint;
import com.senla.model.entity.Scooter;
import com.senla.model.entity.ScooterModel;

public interface ScooterService extends AbstractService<Scooter> {

    void updateScooterModel(Scooter scooter, ScooterModel scooterModel);

    void updateScooterRentalPoint(Scooter scooter, RentalPoint rentalPoint);

    ScooterModel getScooterModelById(Long id);
}
