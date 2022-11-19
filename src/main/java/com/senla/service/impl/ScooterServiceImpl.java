package com.senla.service.impl;

import com.senla.controller.customexception.EntityNotFoundByIdException;
import com.senla.dao.ScooterDao;
import com.senla.dao.ScooterModelDao;
import com.senla.domain.model.entity.RentalPoint;
import com.senla.domain.model.entity.Scooter;
import com.senla.domain.model.entity.ScooterModel;
import com.senla.service.ScooterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ScooterServiceImpl extends AbstractServiceImpl<Scooter, ScooterDao> implements ScooterService {

    private final ScooterDao scooterDao;
    private final ScooterModelDao scooterModelDao;

    @Transactional
    @Override
    public void updateScooterModel(Scooter scooter, ScooterModel model) {
        scooter.setModel(model);
        scooterDao.update(scooter);
    }

    @Transactional
    @Override
    public void updateScooterRentalPoint(Scooter scooter, RentalPoint rentalPoint) {
        scooter.setRentalPoint(rentalPoint);
        scooterDao.update(scooter);
    }

    @Override
    public ScooterModel getScooterModelById(Long id) {
        return scooterModelDao.getById(id).orElseThrow(() -> new EntityNotFoundByIdException(id, ScooterModel.class));
    }

    @Override
    protected ScooterDao getDefaultDao() {
        return scooterDao;
    }

    @Override
    protected Class<Scooter> getDefaultEntityClass() {
        return Scooter.class;
    }
}
