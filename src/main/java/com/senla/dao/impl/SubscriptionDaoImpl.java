package com.senla.dao.impl;

import com.senla.dao.SubscriptionDao;
import com.senla.model.entity.Subscription;
import org.springframework.stereotype.Repository;

@Repository
public class SubscriptionDaoImpl extends AbstractDaoImpl<Subscription> implements SubscriptionDao {

    @Override
    protected Class<Subscription> daoEntityClass() {
        return Subscription.class;
    }
}
