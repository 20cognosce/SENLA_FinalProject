package com.senla.controller;

import com.senla.controller.customexception.EntityNotFoundByIdException;
import com.senla.controller.dto.RideDto;
import com.senla.controller.mapper.RideMapper;
import com.senla.model.entity.RentalPoint;
import com.senla.model.entity.Ride;
import com.senla.model.entity.Scooter;
import com.senla.model.entity.User;
import com.senla.model.entityenum.RideStatus;
import com.senla.security.UserDetailsImpl;
import com.senla.service.RentalPointService;
import com.senla.service.RideService;
import com.senla.service.ScooterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@RequestMapping(value = "/v1/rides", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class RideController {

    private final ScooterService scooterService;
    private final RideService rideService;
    private final RentalPointService rentalPointService;
    private final RideMapper rideMapper;

    @GetMapping(params = {"user-id", "status"})
    public List<RideDto> getUserRidesById(@RequestParam("user-id") Long userId,
                                          @RequestParam("status") String status,
                                          @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit) {
        RideStatus rideStatus = RideStatus.valueOf(status.toUpperCase(Locale.ROOT));
        List<Ride> rides = rideService.getRidesOfTheUser(userId, rideStatus, limit);
        return rides.stream().map(rideMapper::convertToDto).collect(toList());
    }

    @GetMapping(params = {"scooter-id"})
    public List<RideDto> getScooterRidesById(@RequestParam("scooter-id") Long scooterId,
                                             @RequestParam(value = "firstStartTime", defaultValue = "first", required = false) LocalDateTime firstStartTime,
                                             @RequestParam(value = "lastStartTime", defaultValue = "last", required = false) LocalDateTime lastStartTime) {
        List<Ride> rides = rideService.getRidesOfTheScooter(scooterId, firstStartTime, lastStartTime);
        return rides.stream().map(rideMapper::convertToDto).collect(toList());
    }

    @GetMapping("my/{status}")
    public List<RideDto> getRidesByAuth(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @PathVariable("status") String status,
                                        @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit) {
        return getUserRidesById(userDetails.getId(), status, limit);
    }

    //Scheduler cleans pending more than 30 seconds rides once in 30 seconds
    @PostMapping(value = "/my", params = {"scooter-id", "payment"})
    public RideDto createRide(@AuthenticationPrincipal UserDetailsImpl userDetails,
                              @RequestParam("scooter-id") Long scooterId,
                              @RequestParam("payment") String payment) {

        if (rideService.getRidesOfTheUser(userDetails.getId(), RideStatus.PENDING, 5).size() == 5) {
            throw new RuntimeException("Достигнут лимит количества ожидающих начала поездок (5). " +
                    "Повторите попытку через 30 секунд");
        }

        User user = userDetails.getUser();
        Scooter scooter = scooterService.getById(scooterId);
        if ("tariff".equals(payment)) {
            return rideMapper.convertToDto(rideService.createRideWithTariff(scooter, user));
        }
        if ("subscription".equals(payment)) {
            return rideMapper.convertToDto(rideService.createRideWithSubscription(scooter, user));
        }

        throw new RuntimeException("Не выбрана опция оплаты поездки");
    }

    @PutMapping(value = "/my/pending/{ride-id}")
    public void startRide(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("ride-id") Long rideId) {
        if (rideService.getRidesOfTheUser(userDetails.getId(), RideStatus.ACTIVE, 5).size() == 5) {
            throw new RuntimeException("Достигнут лимит количества активных поездок. Нельзя брать в прокат больше 5-ти"
                    + " самокатов одновременно");
        }

        List<Ride> rides = rideService.getRidesOfTheUser(userDetails.getId(), RideStatus.PENDING, 5);
        Ride ride = rides.stream()
                .filter(element -> Objects.equals(element.getId(), rideId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundByIdException(rideId, Ride.class));

        rideService.startRide(ride);
    }

    @PutMapping(value = "/my/active/{ride-id}", params = {"rental-point-id", "mileage", "charge"})
    public void endRide(@AuthenticationPrincipal UserDetailsImpl userDetails,
                        @PathVariable("ride-id") Long rideId,
                        @RequestParam("rental-point-id") Long rentalPointId,
                        @RequestParam("mileage") Double mileage,
                        @RequestParam("charge") Double charge) {

        List<Ride> rides = rideService.getRidesOfTheUser(userDetails.getId(), RideStatus.ACTIVE, 10);
        Ride ride = rides.stream()
                .filter(element -> Objects.equals(element.getId(), rideId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundByIdException(rideId, Ride.class));

        RentalPoint rentalPoint = rentalPointService.getById(rentalPointId);
        rideService.endRide(ride, rentalPoint, mileage, charge);
    }


    @InitBinder
    public void initBinder(WebDataBinder binder) throws Exception {
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        final CustomDateEditor dateEditor = new CustomDateEditor(df, true) {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if ("first".equals(text)) {
                    setValue(LocalDateTime.of(1970, 1, 1, 0, 0));
                } else if ("last".equals(text)) {
                    setValue(LocalDateTime.now());
                } else {
                    super.setAsText(text);
                }
            }
        };
        binder.registerCustomEditor(LocalDateTime.class, dateEditor);
    }

    //TODO: potential performance issues
    @Scheduled(initialDelay = 30_000, fixedDelay = 30_000)
    public void cleanPendingRides() {
        rideService.deletePendingRides(Duration.ofSeconds(30));
    }
}
