package com.senla.dao;

import com.senla.domain.model.entity.RentalPoint;

import java.util.List;
import java.util.Optional;

public interface RentalPointDao extends AbstractDao<RentalPoint> {

    List<RentalPoint> getAllTheClosest(double latitude, double longitude, int limit);

    Optional<RentalPoint> getByIdWithScooters(long id);
}
