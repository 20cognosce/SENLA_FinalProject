package com.senla.service;

import com.senla.model.entity.RentalPoint;
import com.senla.model.entity.Scooter;
import com.senla.model.entity.Tariff;

public interface TariffService extends AbstractService<Tariff>  {

    Double calculateRidePricePerMinute(RentalPoint rentalPoint, Tariff userTariff, Scooter scooter);
}
