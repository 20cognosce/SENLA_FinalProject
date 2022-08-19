package com.senla.service.impl;

import com.senla.dao.GeolocationDao;
import com.senla.dao.RentalPointDao;
import com.senla.model.entity.Geolocation;
import com.senla.model.entity.RentalPoint;
import com.senla.model.entity.Scooter;
import com.senla.model.entity.Subscription;
import com.senla.model.entity.Tariff;
import com.senla.model.entity.User;
import com.senla.service.RentalPointService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.ServiceUnavailableException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RentalPointServiceImpl extends AbstractServiceImpl<RentalPoint, RentalPointDao> implements RentalPointService {

    private final RentalPointDao rentalPointDao;
    private final GeolocationDao geolocationDao;

    @Override
    public List<Scooter> getAllAvailableScooters(RentalPoint rentalPoint) {
        return null;
    }

    @Override
    public void rentTheScooter(Scooter scooter, User user, Tariff userTariff) throws ServiceUnavailableException {

    }

    @Override
    public void rentTheScooter(Scooter scooter, User user, Subscription userSubscription) throws ServiceUnavailableException {

    }

    @Transactional
    @Override
    public Optional<RentalPoint> getByIdWithScooters(long id) {
        Optional<RentalPoint> optionalRentalPoint = getById(id);
        optionalRentalPoint.ifPresent(rentalPoint -> Hibernate.initialize(rentalPoint.getScooters()));
        return optionalRentalPoint;
    }

    @Override
    public List<Geolocation> getAllGeo(Map<String, Object> mapOfFieldNamesAndValuesToSelectBy,
                                                String orderBy,
                                                boolean ascending,
                                                int limit) {
        return geolocationDao.getAll(mapOfFieldNamesAndValuesToSelectBy, orderBy, ascending, limit);
    }

    @Override
    protected RentalPointDao getDefaultDao() {
        return rentalPointDao;
    }
}
