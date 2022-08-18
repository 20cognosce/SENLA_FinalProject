package com.senla.dao.impl;

import com.senla.dao.RideDao;
import com.senla.model.entity.Ride;
import org.springframework.stereotype.Repository;

@Repository
public class RideDaoImpl extends AbstractDaoImpl<Ride> implements RideDao {

    @Override
    protected Class<Ride> daoEntityClass() {
        return Ride.class;
    }
}
