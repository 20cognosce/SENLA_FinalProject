package com.senla.dao.impl;

import com.senla.dao.ScooterModelDao;
import com.senla.domain.model.entity.ScooterModel;
import org.springframework.stereotype.Repository;

@Repository
public class ScooterModelDaoImpl extends AbstractDaoImpl<ScooterModel> implements ScooterModelDao {

    @Override
    protected Class<ScooterModel> daoEntityClass() {
        return ScooterModel.class;
    }
}
