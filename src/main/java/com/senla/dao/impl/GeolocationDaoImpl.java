package com.senla.dao.impl;

import com.senla.dao.GeolocationDao;
import com.senla.domain.model.entity.Geolocation;
import org.springframework.stereotype.Repository;

@Repository
public class GeolocationDaoImpl extends AbstractDaoImpl<Geolocation> implements GeolocationDao {

    @Override
    protected Class<Geolocation> daoEntityClass() {
        return Geolocation.class;
    }
}
