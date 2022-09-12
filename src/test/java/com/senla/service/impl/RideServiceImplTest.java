package com.senla.service.impl;

import com.senla.dao.RideDao;
import com.senla.model.entity.RentalPoint;
import com.senla.model.entity.Ride;
import com.senla.model.entity.Scooter;
import com.senla.model.entity.ScooterModel;
import com.senla.model.entity.Subscription;
import com.senla.model.entity.Tariff;
import com.senla.model.entity.User;
import com.senla.model.entity.User2Subscription;
import com.senla.model.entityenum.RideStatus;
import com.senla.model.entityenum.ScooterConditionStatus;
import com.senla.service.TariffService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RideServiceImplTest {

    @Mock
    private TariffService tariffService;
    @Mock
    private RideDao rideDao;
    @InjectMocks
    private RideServiceImpl rideService;

    @Test
    void getRidesOfTheUser_NotActiveRidesRequested_ListOfRidesReturned() {
        User user = new User();
        Ride ride1 = Ride.builder()
                .status(RideStatus.FINISHED)
                .build();
        Ride ride2 = Ride.builder()
                .status(RideStatus.FINISHED)
                .build();
        List<Ride> rides = Arrays.asList(ride1, ride2);

        doReturn(rides).when(rideDao).getRidesOfTheUser(user, RideStatus.FINISHED, 10);

        assertEquals(rides, rideService.getRidesOfTheUser(user, RideStatus.FINISHED, 10));
    }

    @Test
    void getRidesOfTheUser_ActiveRidesRequested_ActiveRidesWithCalculatedPriceReturned() {
        User user = new User();
        Ride ride1 = Ride.builder()
                .status(RideStatus.ACTIVE)
                .pricePerMinute(5d)
                .startTimestamp(LocalDateTime.now().minusMinutes(10))
                .build();
        Ride ride2 = Ride.builder()
                .status(RideStatus.ACTIVE)
                .pricePerMinute(6d)
                .startTimestamp(LocalDateTime.now().minusMinutes(10))
                .build();
        List<Ride> rides = Arrays.asList(ride1, ride2);

        doReturn(rides).when(rideDao).getRidesOfTheUser(user, RideStatus.ACTIVE, 10);

        rideService.getRidesOfTheUser(user, RideStatus.ACTIVE, 10);

        assertEquals(ride1.getPriceTotal(), 50d);
        assertEquals(ride2.getPriceTotal(), 60d);
    }

    @Test
    void getRidesOfTheScooter() {
        Scooter scooter = new Scooter();
        Ride ride1 = Ride.builder()
                .status(RideStatus.FINISHED)
                .build();
        Ride ride2 = Ride.builder()
                .status(RideStatus.FINISHED)
                .build();
        List<Ride> rides = Arrays.asList(ride1, ride2);

        LocalDateTime firstRideStartTime = LocalDateTime.now().minusHours(6);
        LocalDateTime lastRideStartTime = LocalDateTime.now();

        doReturn(rides).when(rideDao).getRidesOfTheScooter(scooter,10);
        doReturn(rides).when(rideDao).getRidesOfTheScooter(scooter, firstRideStartTime, lastRideStartTime);

        assertEquals(rides, rideService.getRidesOfTheScooter(scooter, 10));
        assertEquals(rides, rideService.getRidesOfTheScooter(scooter, firstRideStartTime, lastRideStartTime));
    }

    @Test
    void createRideWithTariff_TariffDoesNotSupportPassedScooterModel_IllegalArgumentExceptionThrown() {
        ScooterModel scooterModel1 = new ScooterModel();
        scooterModel1.setId(1L);
        ScooterModel scooterModel2 = new ScooterModel();
        scooterModel2.setId(2L);

        Scooter scooter = new Scooter();
        scooter.setModel(scooterModel2);

        Tariff tariff = new Tariff();
        tariff.setModels(List.of(scooterModel1));

        User user = new User();
        user.setTariff(tariff);

        assertThrows(IllegalArgumentException.class, () -> rideService.createRideWithTariff(scooter, user));
    }

    @Test
    void createRideWithTariff_ScooterIsUnavailable_IllegalArgumentExceptionThrown() {
        ScooterModel scooterModel1 = new ScooterModel();
        scooterModel1.setId(1L);

        Scooter scooter = new Scooter();
        scooter.setModel(scooterModel1);
        scooter.setStatus(ScooterConditionStatus.UNAVAILABLE);

        Tariff tariff = new Tariff();
        tariff.setModels(List.of(scooterModel1));

        User user = new User();
        user.setTariff(tariff);

        assertThrows(IllegalArgumentException.class, () -> rideService.createRideWithTariff(scooter, user));
    }

    @Test
    void createRideWithTariff_ScooterIsInUse_IllegalArgumentExceptionThrown() {
        ScooterModel scooterModel1 = new ScooterModel();
        scooterModel1.setId(1L);

        Scooter scooter = new Scooter();
        scooter.setModel(scooterModel1);
        scooter.setStatus(ScooterConditionStatus.UNAVAILABLE);

        Tariff tariff = new Tariff();
        tariff.setModels(List.of(scooterModel1));

        User user = new User();
        user.setTariff(tariff);

        assertThrows(IllegalArgumentException.class, () -> rideService.createRideWithTariff(scooter, user));
    }

    @Test
    void createRideWithTariff_PendingRideCreatedSuccessfully_MethodToStartCountdownForDeletionPendingRideInvoked() {
        RentalPoint rentalPoint = new RentalPoint();

        ScooterModel scooterModel1 = new ScooterModel();
        scooterModel1.setId(1L);

        Scooter scooter = new Scooter();
        scooter.setRentalPoint(rentalPoint);
        scooter.setModel(scooterModel1);

        Tariff tariff = new Tariff();
        tariff.setPricePerMinute(5d);
        tariff.setModels(List.of(scooterModel1));

        User user = new User();
        user.setTariff(tariff);

        doReturn(tariff.getPricePerMinute()).when(tariffService).calculateRidePricePerMinute(rentalPoint, tariff, scooter);

        RideServiceImpl spy = Mockito.spy(rideService);
        spy.createRideWithTariff(scooter, user);
        verify(rideDao).create(argThat(ride ->
                (ride.getStatus() == RideStatus.PENDING) &&
                (ride.getUser() == user) &&
                (ride.getScooter() == scooter) &&
                (Objects.equals(ride.getPricePerMinute(), 5d)) &&
                (ride.getStartRentalPoint() == rentalPoint)));
        verify(spy).startCountdownForDeletionOfPendingRidesOfTheUser(Duration.ofSeconds(30), user, 30);
    }

    @Test
    void createRideWithSubscription_UserDoesNotHaveSubscription_IllegalArgumentExceptionThrown() {
        Scooter scooter = new Scooter();
        User user = new User();

        assertThrows(IllegalArgumentException.class, () -> rideService.createRideWithSubscription(scooter, user));
    }

    @Test
    void createRideWithSubscription_UserHasExpiredSubscription_IllegalArgumentExceptionThrown() {
        Scooter scooter = new Scooter();

        User user = new User();
        user.setUser2Subscription(User2Subscription.builder()
                .user(user)
                .startTime(LocalDateTime.now().minusDays(30))
                .endTime(LocalDateTime.now())
                .build());

        assertThrows(IllegalArgumentException.class, () -> rideService.createRideWithSubscription(scooter, user));
    }

    @Test
    void createRideWithSubscription_SubscriptionDoesNotSupportPassedScooterModel_IllegalArgumentExceptionThrown() {
        ScooterModel scooterModel1 = new ScooterModel();
        scooterModel1.setId(1L);
        ScooterModel scooterModel2 = new ScooterModel();
        scooterModel1.setId(2L);

        Scooter scooter = new Scooter();
        scooter.setModel(scooterModel2);
        scooter.setStatus(ScooterConditionStatus.OK);

        Subscription subscription = new Subscription();
        subscription.setModels(List.of(scooterModel1));

        User user = new User();
        user.setUser2Subscription(User2Subscription.builder()
                .subscription(subscription)
                .user(user)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusDays(30))
                .build());

        assertThrows(IllegalArgumentException.class, () -> rideService.createRideWithSubscription(scooter, user));
    }

    @Test
    void createRideWithSubscription_ScooterIsUnavailableToRent_IllegalArgumentExceptionThrown() {
        ScooterModel scooterModel1 = new ScooterModel();
        scooterModel1.setId(1L);

        Scooter scooter = new Scooter();
        scooter.setModel(scooterModel1);
        scooter.setStatus(ScooterConditionStatus.UNAVAILABLE);

        Subscription subscription = new Subscription();
        subscription.setModels(List.of(scooterModel1));

        User user = new User();
        user.setUser2Subscription(User2Subscription.builder()
                .subscription(subscription)
                .user(user)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusDays(30))
                .build());

        assertThrows(IllegalArgumentException.class, () -> rideService.createRideWithSubscription(scooter, user));
    }

    @Test
    void createRideWithSubscription_PendingRideCreatedSuccessfully_MethodToStartCountdownForDeletionPendingRideInvoked() {
        RentalPoint rentalPoint = new RentalPoint();

        ScooterModel scooterModel1 = new ScooterModel();
        scooterModel1.setId(1L);

        Scooter scooter = new Scooter();
        scooter.setRentalPoint(rentalPoint);
        scooter.setModel(scooterModel1);
        scooter.setStatus(ScooterConditionStatus.OK);

        Subscription subscription = new Subscription();
        subscription.setModels(List.of(scooterModel1));

        User user = new User();
        user.setUser2Subscription(User2Subscription.builder()
                .subscription(subscription)
                .user(user)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusDays(30))
                .build());

        RideServiceImpl spy = Mockito.spy(rideService);
        spy.createRideWithSubscription(scooter, user);
        verify(rideDao).create(argThat(ride ->
                (ride.getStatus() == RideStatus.PENDING) &&
                        (ride.getUser() == user) &&
                        (ride.getScooter() == scooter) &&
                        (Objects.equals(ride.getPricePerMinute(), 0d)) &&
                        (ride.getStartRentalPoint() == rentalPoint)));
        verify(spy).startCountdownForDeletionOfPendingRidesOfTheUser(Duration.ofSeconds(30), user, 30);
    }

    @Test
    void startRide_scooterStatusIsUnavailable_IllegalArgumentExceptionThrown() {
        Scooter scooter = Scooter.builder()
                .status(ScooterConditionStatus.UNAVAILABLE)
                .rentalPoint(new RentalPoint())
                .build();
        Ride ride = Ride.builder()
                .scooter(scooter)
                .build();

        assertThrows(IllegalArgumentException.class, () -> rideService.startRide(ride));
    }

    @Test
    void startRide_scooterStatusIsIN_USE_IllegalArgumentExceptionThrown() {
        Scooter scooter = Scooter.builder()
                .status(ScooterConditionStatus.IN_USE)
                .rentalPoint(new RentalPoint())
                .build();
        Ride ride = Ride.builder()
                .scooter(scooter)
                .build();

        assertThrows(IllegalArgumentException.class, () -> rideService.startRide(ride));
    }

    @Test
    void startRide_scooterStatusIsOK_rideStarted() {
        Scooter scooter = Scooter.builder()
                .status(ScooterConditionStatus.OK)
                .rentalPoint(new RentalPoint())
                .build();
        Ride ride = Ride.builder()
                .scooter(scooter)
                .build();

        rideService.startRide(ride);

        assertTrue(ride.getStartTimestamp().isBefore(LocalDateTime.now().plusNanos(1)));
        assertEquals(ride.getStatus(), RideStatus.ACTIVE);
        assertEquals(ride.getScooter().getStatus(), ScooterConditionStatus.IN_USE);
        assertNull(ride.getScooter().getRentalPoint());
    }

    @Test
    void startRide_scooterStatusIsDischarged_rideStarted() {
        Scooter scooter = Scooter.builder()
                .status(ScooterConditionStatus.DISCHARGED)
                .rentalPoint(new RentalPoint())
                .build();
        Ride ride = Ride.builder()
                .scooter(scooter)
                .build();

        rideService.startRide(ride);

        assertTrue(ride.getStartTimestamp().isBefore(LocalDateTime.now().plusNanos(1)));
        assertEquals(ride.getStatus(), RideStatus.ACTIVE);
        assertEquals(ride.getScooter().getStatus(), ScooterConditionStatus.IN_USE);
        assertNull(ride.getScooter().getRentalPoint());
    }

    @Test
    void endRide_ScooterChargeIsAbove1Percent_ScooterStatusSetToOk() {
        Scooter scooter = Scooter.builder()
                .status(ScooterConditionStatus.IN_USE)
                .mileage(2.5)
                .build();
        Ride ride = Ride.builder()
                .startTimestamp(LocalDateTime.now().minusMinutes(10))
                .status(RideStatus.ACTIVE)
                .pricePerMinute(6.5)
                .scooter(scooter)
                .build();
        RentalPoint rentalPoint = new RentalPoint();

        rideService.endRide(ride, rentalPoint, 2.5, 35d);

        //now() inside tested method and in the test itself returns just the same time somehow
        assertTrue(ride.getEndTimestamp().isBefore(LocalDateTime.now().plusNanos(1)));
        assertEquals(ride.getEndRentalPoint(), rentalPoint);
        assertEquals(ride.getRideMileage(), 2.5);
        assertEquals(ride.getPriceTotal(), 65);
        assertEquals(ride.getStatus(), RideStatus.FINISHED);

        assertEquals(scooter.getMileage(), 5.0);
        assertEquals(scooter.getCharge(), 35.0);
        assertEquals(scooter.getRentalPoint(), rentalPoint);
        assertEquals(scooter.getStatus(), ScooterConditionStatus.OK);
    }

    @Test
    void endRide_ScooterChargeIsBelow1Percent_ScooterStatusSetToDischarged() {
        Scooter scooter = Scooter.builder()
                .status(ScooterConditionStatus.IN_USE)
                .mileage(2.5)
                .build();
        Ride ride = Ride.builder()
                .startTimestamp(LocalDateTime.now().minusMinutes(10))
                .status(RideStatus.ACTIVE)
                .pricePerMinute(6.5)
                .scooter(scooter)
                .build();
        RentalPoint rentalPoint = new RentalPoint();

        rideService.endRide(ride, rentalPoint, 2.5, 1.0);

        assertTrue(ride.getEndTimestamp().isBefore(LocalDateTime.now().plusNanos(1)));
        assertEquals(ride.getEndRentalPoint(), rentalPoint);
        assertEquals(ride.getRideMileage(), 2.5);
        assertEquals(ride.getPriceTotal(), 65);
        assertEquals(ride.getStatus(), RideStatus.FINISHED);

        assertEquals(scooter.getMileage(), 5.0);
        assertEquals(scooter.getCharge(), 1.0);
        assertEquals(scooter.getRentalPoint(), rentalPoint);
        assertEquals(scooter.getStatus(), ScooterConditionStatus.DISCHARGED);
    }

    @Test
    void startCountdownForDeletionOfPendingRidesOfTheUser() {
        User user = new User();
        Ride ride = Ride.builder().status(RideStatus.PENDING).build();

        doAnswer(invocationOnMock -> {
            ride.setStatus(RideStatus.FINISHED); //actual method deletes ride from db
            return null;
        }).when(rideDao).deletePendingRidesOfTheUser(Duration.ofSeconds(2), user);

        rideService.startCountdownForDeletionOfPendingRidesOfTheUser(Duration.ofSeconds(2), user, 2);

        assertEquals(RideStatus.PENDING, ride.getStatus());
        try {
            Thread.sleep(3000); //allocating a bit more time for doAnswer to complete setting new status
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(RideStatus.FINISHED, ride.getStatus());
    }

    @Test
    void getDefaultDao() {
        assertEquals(rideDao.getClass(), rideService.getDefaultDao().getClass());
    }

    @Test
    void getDefaultEntityClass() {
        assertEquals(Ride.class, rideService.getDefaultEntityClass());
    }
}