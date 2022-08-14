package com.senla.service;

import com.senla.model.entity.RentalPoint;
import com.senla.model.entity.Scooter;
import com.senla.model.entity.Subscription;
import com.senla.model.entity.Tariff;
import com.senla.model.entity.User;

import java.util.List;

public interface RentalPointService {

	List<Scooter> getAvailableScooters(RentalPoint rentalPoint, User user, Tariff userTariff);
	List<Scooter> getAvailableScooters(RentalPoint rentalPoint, User user, Subscription userSubscription);
}
