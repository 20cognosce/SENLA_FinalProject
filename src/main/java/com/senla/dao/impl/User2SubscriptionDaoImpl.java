package com.senla.dao.impl;

import com.senla.dao.User2SubscriptionDao;
import com.senla.model.entity.User2Subscription;
import org.springframework.stereotype.Repository;

@Repository
public class User2SubscriptionDaoImpl extends AbstractDaoImpl<User2Subscription> implements User2SubscriptionDao {

    @Override
    protected Class<User2Subscription> daoEntityClass() {
        return User2Subscription.class;
    }
}
