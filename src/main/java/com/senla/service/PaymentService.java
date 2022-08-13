package com.senla.service;

import com.senla.model.entity.RentalPoint;
import com.senla.model.entity.Scooter;
import com.senla.model.entity.User;

public interface PaymentService {

    Float calculatePricePerMinute(RentalPoint rentalPoint, User user, Scooter scooter);

    void setSubscriptionDiscount(User user, float discount);

    void setTariffDiscountForAllUser();

    void setTariffDiscountForSpecificUser();
}
