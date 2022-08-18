package com.senla.dao.impl;

import com.senla.dao.RentalPointDao;
import com.senla.model.entity.RentalPoint;
import org.springframework.stereotype.Repository;

@Repository
public class RentalPointDaoImpl extends AbstractDaoImpl<RentalPoint> implements RentalPointDao {

    @Override
    protected Class<RentalPoint> daoEntityClass() {
        return RentalPoint.class;
    }
}
