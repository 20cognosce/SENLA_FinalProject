package com.senla.dao.impl;

import com.senla.dao.ScooterDao;
import com.senla.model.entity.Scooter;
import org.springframework.stereotype.Repository;

@Repository
public class ScooterDaoImpl extends AbstractDaoImpl<Scooter> implements ScooterDao {

    @Override
    protected Class<Scooter> daoEntityClass() {
        return Scooter.class;
    }
}
