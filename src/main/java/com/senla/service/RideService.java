package com.senla.service;

import com.senla.model.entity.Ride;

import java.util.List;

public interface RideService {

    List<Ride> getRidesOfTheScooter(Long scooterId);

    List<Ride> getRidesOfTheUser(Long userId);
}
