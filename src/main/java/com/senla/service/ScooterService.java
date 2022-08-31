package com.senla.service;

import com.senla.model.entity.Scooter;
import com.senla.model.entity.ScooterModel;

public interface ScooterService extends AbstractService<Scooter> {

    void updateScooterModel(Long scooterId, Long modelId);

    void updateScooterRentalPoint(Long scooterId, Long rentalPointId);

    ScooterModel getScooterModelById(Long id);
}
