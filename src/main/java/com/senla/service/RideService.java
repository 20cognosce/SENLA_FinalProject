package com.senla.service;

import com.senla.model.entity.RentalPoint;
import com.senla.model.entity.Ride;
import com.senla.model.entity.Scooter;
import com.senla.model.entity.User;
import com.senla.model.entityenum.RideStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public interface RideService extends AbstractService<Ride> {

    List<Ride> getRidesOfTheUser(User user, RideStatus rideStatus, Integer limit);
    List<Ride> getRidesOfTheScooter(Scooter scooter, Integer limit);
    List<Ride> getRidesOfTheScooter(Scooter scooter, LocalDateTime firstRideStartTimestamp, LocalDateTime lastRideStartTimestamp);

    Ride createRideWithTariff(Scooter scooter, User user);
    Ride createRideWithSubscription(Scooter scooter, User user);
    void startCountdownForDeletionOfPendingRidesOfTheUser(Duration minPendingRideLifetime, User user, long delayInSeconds);

    void startRide(Ride ride);
    void endRide(Ride ride, RentalPoint endRentalPoint, Double mileage, Double charge);
}
