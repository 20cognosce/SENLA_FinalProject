package com.senla.service;

import com.senla.model.entity.RentalPoint;
import com.senla.model.entity.Scooter;
import com.senla.model.entity.Tariff;
import com.senla.model.entity.User;

public interface PaymentService {

    Tariff calculateTariff(RentalPoint rentalPoint, User user, Scooter scooter);

    void setSubscriptionDiscount(User user, float discount);
    void setTariffDiscountForAllUser();
    void setTariffDiscountForAllSpecificUser();
}
