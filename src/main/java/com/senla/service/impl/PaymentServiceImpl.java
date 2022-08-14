package com.senla.service.impl;

import com.senla.model.entity.RentalPoint;
import com.senla.model.entity.Scooter;
import com.senla.model.entity.Tariff;
import com.senla.model.entity.User;
import com.senla.service.PaymentService;

public class PaymentServiceImpl implements PaymentService {

    @Override
    public Float calculateRidePricePerMinute(RentalPoint rentalPoint, Tariff userTariff, Scooter scooter) {
        //analyze the peak hour factor
        //analyze the demand for current rental point
        //analyze user tariff
        //analyze scooter condition

        return 5.5f;
    }

    @Override
    public void setSubscriptionDiscount(User user, float discount) {

    }

    @Override
    public void setTariffDiscountForAllUser() {

    }

    @Override
    public void setTariffDiscountForSpecificUser() {

    }
}
