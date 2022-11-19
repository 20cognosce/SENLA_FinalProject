package com.senla.service;

import com.senla.domain.model.entity.Subscription;
import com.senla.domain.model.entity.User;

public interface SubscriptionService extends AbstractService<Subscription> {

    void setSubscriptionToUser(User user, Subscription subscription);
}
