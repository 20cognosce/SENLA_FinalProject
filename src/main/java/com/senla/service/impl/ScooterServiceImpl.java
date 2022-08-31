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

    @Override
    protected Class<Scooter> getDefaultEntityClass() {
        return Scooter.class;
    }

    @Transactional
    @Override
    public void updateScooterModel(Long scooterId, Long modelId) {
        Scooter scooter = getById(scooterId);
        ScooterModel model = scooterModelDao.getById(modelId).orElseThrow(() -> new EntityNotFoundByIdException(modelId, ScooterModel.class));
        scooter.setModel(model);
        scooterDao.update(scooter);
    }

    @Transactional
    @Override
    public void updateScooterRentalPoint(Long scooterId, Long rentalPointId) {
        Scooter scooter = getById(scooterId);
        RentalPoint rentalPoint = rentalPointDao.getById(rentalPointId).orElseThrow(() -> new EntityNotFoundByIdException(rentalPointId, RentalPoint.class));
        scooter.setRentalPoint(rentalPoint);
        scooterDao.update(scooter);
    }

    @Override
    public ScooterModel getScooterModelById(Long id) {
        return scooterModelDao.getById(id).orElseThrow(() -> new EntityNotFoundByIdException(id, ScooterModel.class));
    }
}
