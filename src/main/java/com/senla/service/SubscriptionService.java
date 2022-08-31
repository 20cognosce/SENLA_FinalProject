package com.senla.service;

import com.senla.model.entity.Subscription;
import com.senla.model.entity.User;

public interface SubscriptionService extends AbstractService<Subscription> {

    void setSubscriptionToUser(User user, Subscription subscription);
}
