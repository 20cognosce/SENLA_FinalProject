package com.senla.service.impl;

import com.senla.controller.customexception.EntityNotFoundByIdException;
import com.senla.dao.GeolocationDao;
import com.senla.dao.RentalPointDao;
import com.senla.model.entity.Geolocation;
import com.senla.model.entity.RentalPoint;
import com.senla.service.RentalPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    public List<Geolocation> getAllGeo(Map<String, Object> mapOfFieldNamesAndValuesToSelectBy,
                                                String orderBy, boolean ascending, int limit) {
        return geolocationDao.getAll(mapOfFieldNamesAndValuesToSelectBy, orderBy, ascending, limit);
    }

    @Override
    public List<RentalPoint> getAllTheClosest(double latitude, double longitude, int limit) {
        return getDefaultDao().getAllTheClosest(latitude, longitude, limit);
    }

    @Override
    public Double getDistanceToClientInKm(RentalPoint rentalPoint, Double clientLatitude, Double clientLongitude) {
        double equatorialEarthRadius = 6371d;
        double lat1 = Math.abs(rentalPoint.getGeolocation().getLatitude());
        double lng1 = Math.abs(rentalPoint.getGeolocation().getLongitude());
        double lat2 = clientLatitude;
        double lng2 = clientLongitude;

        double dLat = (lat2 - lat1) * Math.PI / 180d;
        double dLng = (lng2 - lng1) * Math.PI / 180d;
        double a = Math.sin(dLat / 2d) * Math.sin(dLat / 2d) + Math.sin(dLng / 2d) * Math.sin(dLng / 2d) *
                Math.cos(lat1 * Math.PI / 180d) * Math.cos(lat2 * Math.PI / 180d);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distanceInKm = equatorialEarthRadius * c;

        DecimalFormat df = new DecimalFormat("#.###");
        df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setRoundingMode(RoundingMode.HALF_UP);
        return Double.parseDouble(df.format(distanceInKm));
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
