package com.senla.service;

import com.senla.domain.model.entity.RentalPoint;
import com.senla.domain.model.entity.Scooter;
import com.senla.domain.model.entity.Tariff;

public interface TariffService extends AbstractService<Tariff>  {

    Double calculateRidePricePerMinute(RentalPoint rentalPoint, Tariff userTariff, Scooter scooter);
}
