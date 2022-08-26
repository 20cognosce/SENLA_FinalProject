package com.senla.service.impl;

import com.senla.controller.customexception.EntityNotFoundByIdException;
import com.senla.dao.SubscriptionDao;
import com.senla.dao.TariffDao;
import com.senla.dao.User2SubscriptionDao;
import com.senla.model.entity.RentalPoint;
import com.senla.model.entity.Scooter;
import com.senla.model.entity.Subscription;
import com.senla.model.entity.Tariff;
import com.senla.model.entity.User;
import com.senla.model.entity.User2Subscription;
import com.senla.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final TariffDao tariffDao;
    private final SubscriptionDao subscriptionDao;
    private final User2SubscriptionDao user2SubscriptionDao;

    @Override
    public Double calculateRidePricePerMinute(RentalPoint rentalPoint, Tariff userTariff, Scooter scooter) {
        //analyze the peak hour factor
        //analyze the demand for current rental point
        //analyze user tariff
        //analyze scooter condition

        return 5.5d;
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

    @Override
    public Tariff getTariffById(Long id) {
        return tariffDao.getById(id).orElseThrow(() -> new EntityNotFoundByIdException(id, Tariff.class));
    }

    @Override
    public Subscription getSubscriptionById(Long id) {
        return subscriptionDao.getById(id).orElseThrow(() -> new EntityNotFoundByIdException(id, Subscription.class));
    }

    @Override
    public User2Subscription getUser2SubscriptionByUserId(Long userId) {
        return null;
    }
}
