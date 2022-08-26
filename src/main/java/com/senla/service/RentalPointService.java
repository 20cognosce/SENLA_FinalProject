package com.senla.service;

import com.senla.model.entity.Geolocation;
import com.senla.model.entity.RentalPoint;
import com.senla.model.entity.Scooter;
import com.senla.model.entity.Subscription;
import com.senla.model.entity.Tariff;
import com.senla.model.entity.User;

import javax.naming.ServiceUnavailableException;
import java.util.List;
import java.util.Map;

public interface RentalPointService extends AbstractService<RentalPoint> {

    List<Scooter> getAllAvailableScooters(RentalPoint rentalPoint);

    void rentTheScooter(Scooter scooter, User user, Tariff userTariff) throws ServiceUnavailableException;

    void rentTheScooter(Scooter scooter, User user, Subscription userSubscription) throws ServiceUnavailableException;

    RentalPoint getByIdWithScooters(Long id);

    List<Geolocation> getAllGeo(Map<String, Object> mapOfFieldNamesAndValuesToSelectBy,
                                String orderBy,
                                boolean ascending,
                                int limit);

    List<RentalPoint> getAllTheClosest(double latitude, double longitude, int limit);

    Double getDistanceToClientInKm(RentalPoint rentalPoint, Double clientLatitude, Double clientLongitude);
}
