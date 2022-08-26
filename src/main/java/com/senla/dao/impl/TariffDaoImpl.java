package com.senla.dao.impl;

import com.senla.dao.TariffDao;
import com.senla.model.entity.Tariff;
import org.springframework.stereotype.Repository;

@Repository
public class TariffDaoImpl extends AbstractDaoImpl<Tariff> implements TariffDao {
    @Override
    protected Class<Tariff> daoEntityClass() {
        return Tariff.class;
    }
}
