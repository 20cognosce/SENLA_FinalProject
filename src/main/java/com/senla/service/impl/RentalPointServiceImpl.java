package com.senla.service.impl;

import com.senla.controller.customexception.EntityNotFoundByIdException;
import com.senla.dao.GeolocationDao;
import com.senla.dao.RentalPointDao;
import com.senla.domain.model.entity.Geolocation;
import com.senla.domain.model.entity.RentalPoint;
import com.senla.service.RentalPointService;
import com.senla.utils.Distance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class RentalPointServiceImpl extends AbstractServiceImpl<RentalPoint, RentalPointDao> implements RentalPointService {

    private final RentalPointDao rentalPointDao;
    private final GeolocationDao geolocationDao;

    @Override
    public RentalPoint getByIdWithScooters(Long id) throws EntityNotFoundByIdException {
        return getDefaultDao().getByIdWithScooters(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(id, RentalPoint.class));
    }

    @Override
    public List<Geolocation> getAllGeo(Map<String, Object> fieldNamesAndValuesToSelectBy,
                                                String orderBy, boolean ascending, int limit) {
        fieldNamesAndValuesToSelectBy.entrySet().removeIf(entry -> Objects.isNull(entry.getValue()));
        return geolocationDao.getAll(fieldNamesAndValuesToSelectBy, orderBy, ascending, limit);
    }

    @Override
    public List<RentalPoint> getAllTheClosest(double latitude, double longitude, int limit) {
        return getDefaultDao().getAllTheClosest(latitude, longitude, limit);
    }

    @Override
    public Double getDistanceToClientInKm(RentalPoint rentalPoint, Double clientLatitude, Double clientLongitude) {
        return Distance.getDistanceToClientInKm(rentalPoint, clientLatitude, clientLongitude);
    }

    @Override
    protected RentalPointDao getDefaultDao() {
        return rentalPointDao;
    }

    @Override
    protected Class<RentalPoint> getDefaultEntityClass() {
        return RentalPoint.class;
    }
}
