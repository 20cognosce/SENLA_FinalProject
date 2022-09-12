package com.senla.dao;

import com.senla.model.entity.Ride;
import com.senla.model.entity.Scooter;
import com.senla.model.entity.User;
import com.senla.model.entityenum.RideStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public interface RideDao extends AbstractDao<Ride> {

    List<Ride> getRidesOfTheUser(User user, RideStatus rideStatus, Integer limit);
    List<Ride> getRidesOfTheScooter(Scooter scooter, Integer limit);
    List<Ride> getRidesOfTheScooter(Scooter scooter, LocalDateTime firstRideStartTimestamp, LocalDateTime lastRideEndTimestamp);
    void deletePendingRidesOfTheUser(Duration minPendingRideLifetime, User user);
}
