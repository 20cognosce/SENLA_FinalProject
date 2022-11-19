package com.senla.service.impl;

import com.senla.dao.TariffDao;
import com.senla.domain.model.entity.RentalPoint;
import com.senla.domain.model.entity.Scooter;
import com.senla.domain.model.entity.Tariff;
import com.senla.service.TariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TariffServiceImpl extends AbstractServiceImpl<Tariff, TariffDao> implements TariffService {

    private final TariffDao tariffDao;

    @Override
    public Double calculateRidePricePerMinute(RentalPoint rentalPoint, Tariff tariff, Scooter scooter) {
        //analyze the peak hour factor
        //analyze the demand for current rental point
        //analyze user tariff
        //analyze scooter condition

        return tariff.getPricePerMinute();
    }

    @Override
    protected TariffDao getDefaultDao() {
        return tariffDao;
    }

    @Override
    protected Class<Tariff> getDefaultEntityClass() {
        return Tariff.class;
    }
}
