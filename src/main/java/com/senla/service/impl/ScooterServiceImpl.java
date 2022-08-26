package com.senla.service.impl;

import com.senla.controller.customexception.EntityNotFoundByIdException;
import com.senla.dao.RentalPointDao;
import com.senla.dao.ScooterDao;
import com.senla.dao.ScooterModelDao;
import com.senla.model.entity.RentalPoint;
import com.senla.model.entity.Scooter;
import com.senla.model.entity.ScooterModel;
import com.senla.service.ScooterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ScooterServiceImpl extends AbstractServiceImpl<Scooter, ScooterDao> implements ScooterService {

    private final ScooterDao scooterDao;
    private final RentalPointDao rentalPointDao;
    private final ScooterModelDao scooterModelDao;

    @Override
    protected ScooterDao getDefaultDao() {
        return scooterDao;
    }

    @Transactional
    @Override
    public void updateScooterModel(Long scooterId, Long modelId) {
        Scooter scooter = getById(scooterId).orElseThrow(() -> new EntityNotFoundByIdException(scooterId, Scooter.class));
        ScooterModel model = scooterModelDao.getById(modelId).orElseThrow(() -> new EntityNotFoundByIdException(modelId, ScooterModel.class));
        scooter.setModel(model);
        scooterDao.update(scooter);
    }

    @Transactional
    @Override
    public void updateScooterRentalPoint(Long scooterId, Long rentalPointId) {
        Scooter scooter = getById(scooterId).orElseThrow(() -> new EntityNotFoundByIdException(scooterId, Scooter.class));
        RentalPoint rentalPoint = rentalPointDao.getById(rentalPointId).orElseThrow(() -> new EntityNotFoundByIdException(rentalPointId, RentalPoint.class));
        scooter.setRentalPoint(rentalPoint);
        scooterDao.update(scooter);
    }

    @Override
    public Optional<ScooterModel> getScooterModelById(Long id) {
        return scooterModelDao.getById(id);
    }
}
