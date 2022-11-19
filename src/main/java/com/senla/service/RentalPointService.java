package com.senla.service;

import com.senla.domain.model.entity.Geolocation;
import com.senla.domain.model.entity.RentalPoint;

import java.util.List;
import java.util.Map;

public interface RentalPointService extends AbstractService<RentalPoint> {

    RentalPoint getByIdWithScooters(Long id);

    List<Geolocation> getAllGeo(Map<String, Object> mapOfFieldNamesAndValuesToSelectBy,
                                String orderBy,
                                boolean ascending,
                                int limit);

    List<RentalPoint> getAllTheClosest(double latitude, double longitude, int limit);

    Double getDistanceToClientInKm(RentalPoint rentalPoint, Double clientLatitude, Double clientLongitude);
}
