package com.senla.service.impl;

import com.senla.controller.customexception.EntityNotFoundByIdException;
import com.senla.dao.RideDao;
import com.senla.dao.ScooterDao;
import com.senla.dao.UserDao;
import com.senla.model.entity.RentalPoint;
import com.senla.model.entity.Ride;
import com.senla.model.entity.Scooter;
import com.senla.model.entity.ScooterModel;
import com.senla.model.entity.Tariff;
import com.senla.model.entity.User;
import com.senla.model.entity.User2Subscription;
import com.senla.model.entityenum.RideStatus;
import com.senla.model.entityenum.ScooterConditionStatus;
import com.senla.service.RideService;
import com.senla.service.TariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class RideServiceImpl extends AbstractServiceImpl<Ride, RideDao> implements RideService {

    private final TariffService tariffService;
    private final RideDao rideDao;
    private final UserDao userDao;
    private final ScooterDao scooterDao;

    @Override
    public List<Ride> getRidesOfTheUser(Long userId, RideStatus rideStatus, Integer limit) {
        User user = userDao.getById(userId).orElseThrow(() -> new EntityNotFoundByIdException(userId, User.class));
        List<Ride> rides = getDefaultDao().getRidesOfTheUser(user, rideStatus, limit);

        if (rideStatus == RideStatus.ACTIVE) {
            rides.forEach(ride -> ride.setPriceTotal(
                    ride.getPricePerMinute() * Duration.between(ride.getStartTimestamp(), LocalDateTime.now()).toMinutes()));
        }
        return rides;
    }

    @Override
    public List<Ride> getRidesOfTheScooter(Long scooterId, Integer limit) {
        Scooter scooter = scooterDao.getById(scooterId).orElseThrow(() -> new EntityNotFoundByIdException(scooterId, Scooter.class));
        return getDefaultDao().getRidesOfTheScooter(scooter, limit);
    }

    @Override
    public List<Ride> getRidesOfTheScooter(Long scooterId, LocalDateTime firstRideStartTimestamp, LocalDateTime lastRideStartTimestamp) {
        Scooter scooter = scooterDao.getById(scooterId).orElseThrow(() -> new EntityNotFoundByIdException(scooterId, Scooter.class));
        return getDefaultDao().getRidesOfTheScooter(scooter, firstRideStartTimestamp, lastRideStartTimestamp);
    }

    @Transactional
    @Override
    public Ride createRideWithTariff(Scooter scooter, User user) {
        Tariff tariff = user.getTariff();
        Double pricePerMinute = tariffService.calculateRidePricePerMinute(scooter.getRentalPoint(), tariff, scooter);

        List<Long> models = tariff.getModels().stream().map(ScooterModel::getId).collect(toList());
        if (!models.contains(scooter.getModel().getId())) {
            throw new IllegalArgumentException("Выбранный самокат не поддерживается вашим тарифом");
        }
        if (scooter.getStatus() == ScooterConditionStatus.UNAVAILABLE || scooter.getStatus() == ScooterConditionStatus.IN_USE) {
            throw new IllegalArgumentException("Выбранный самокат не доступен");
        }

        Ride ride = Ride.builder()
                .user(user)
                .scooter(scooter)
                .creationTimestamp(LocalDateTime.now())
                .status(RideStatus.PENDING)
                .startRentalPoint(scooter.getRentalPoint())
                .pricePerMinute(pricePerMinute)
                .build();

        getDefaultDao().create(ride);
        return ride;
    }

    @Transactional
    @Override
    public Ride createRideWithSubscription(Scooter scooter, User user) {
        User2Subscription user2Subscription = user.getUser2Subscription();

        if (Objects.isNull(user2Subscription)) {
            throw new IllegalArgumentException("Подписка отсутствует");
        }
        if (!user2Subscription.isValid()) {
            throw new IllegalArgumentException("Срок действия подписки истёк");
        }
        List<Long> models = user2Subscription.getSubscription().getModels().stream().map(ScooterModel::getId).collect(toList());
        if (!models.contains(scooter.getModel().getId())) {
            throw new IllegalArgumentException("Выбранный самокат не поддерживается вашей подпиской");
        }
        if (scooter.getStatus() == ScooterConditionStatus.UNAVAILABLE || scooter.getStatus() == ScooterConditionStatus.IN_USE) {
            throw new IllegalArgumentException("Выбранный самокат не доступен");
        }

        Ride ride = Ride.builder()
                .user(user)
                .scooter(scooter)
                .creationTimestamp(LocalDateTime.now())
                .status(RideStatus.PENDING)
                .startRentalPoint(scooter.getRentalPoint())
                .pricePerMinute(0d)
                .build();

        getDefaultDao().create(ride);
        return ride;
    }

    @Transactional
    @Override
    public void startRide(Ride ride) {
        ride.setStartTimestamp(LocalDateTime.now());
        ride.setStatus(RideStatus.ACTIVE);
        ride.getScooter().setStatus(ScooterConditionStatus.IN_USE);
        ride.getScooter().setRentalPoint(null);
        getDefaultDao().update(ride);
    }

    @Transactional
    @Override
    public void endRide(Ride ride, RentalPoint rentalPoint, Double mileage, Double charge) {
        //assuming mileage is fair
        Scooter scooter = ride.getScooter();
        LocalDateTime startTime = ride.getStartTimestamp();
        LocalDateTime endTime = LocalDateTime.now();

        ride.setEndTimestamp(endTime);
        ride.setEndRentalPoint(rentalPoint);
        ride.setRideMileage(mileage);
        ride.setPriceTotal(ride.getPricePerMinute() * Duration.between(startTime, endTime).toMinutes());
        ride.setStatus(RideStatus.FINISHED);

        scooter.setMileage(scooter.getMileage() + mileage);
        scooter.setCharge(charge);
        scooter.setRentalPoint(rentalPoint);
        if (charge < 15) {
            scooter.setStatus(ScooterConditionStatus.DISCHARGED);
        } else {
            scooter.setStatus(ScooterConditionStatus.OK);
        }

        getDefaultDao().update(ride);
    }

    @Transactional
    @Override
    public void deletePendingRides(Duration minPendingTime) {
        getDefaultDao().deletePendingRides(minPendingTime);
    }

    @Override
    protected RideDao getDefaultDao() {
        return rideDao;
    }

    @Override
    protected Class<Ride> getDefaultEntityClass() {
        return Ride.class;
    }
}