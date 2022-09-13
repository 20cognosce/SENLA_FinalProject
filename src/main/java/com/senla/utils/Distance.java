package com.senla.utils;

import com.senla.model.entity.RentalPoint;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Distance {

    public static Double getDistanceToClientInKm(RentalPoint rentalPoint, Double clientLatitude, Double clientLongitude) {
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
}
