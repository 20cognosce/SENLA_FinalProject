package com.senla.service;

import com.senla.model.entity.RentalPoint;
import com.senla.model.entity.Scooter;
import com.senla.model.entity.Subscription;
import com.senla.model.entity.Tariff;
import com.senla.model.entity.User;
import com.senla.model.entity.User2Subscription;

public interface PaymentService  {

    Double calculateRidePricePerMinute(RentalPoint rentalPoint, Tariff userTariff, Scooter scooter);

    void setSubscriptionDiscount(User user, float discount);

    void setTariffDiscountForAllUser();

    void setTariffDiscountForSpecificUser();

    Tariff getTariffById(Long id);
    Subscription getSubscriptionById(Long id);
    User2Subscription getUser2SubscriptionByUserId(Long userId);
}
