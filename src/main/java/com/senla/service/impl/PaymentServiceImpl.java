package com.senla.service.impl;

import com.senla.model.entity.RentalPoint;
import com.senla.model.entity.Scooter;
import com.senla.model.entity.Tariff;
import com.senla.model.entity.User;
import com.senla.service.PaymentService;

public class PaymentServiceImpl implements PaymentService {

    @Override
    public Tariff calculateTariff(RentalPoint rentalPoint, User user, Scooter scooter) {
        return null;
    }

    @Override
    public void setSubscriptionDiscount(User user, float discount) {

    }

    @Override
    public void setTariffDiscountForAllUser() {

    }

    @Override
    public void setTariffDiscountForAllSpecificUser() {

    }
}
