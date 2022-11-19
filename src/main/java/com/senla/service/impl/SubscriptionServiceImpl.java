package com.senla.service.impl;

import com.senla.dao.SubscriptionDao;
import com.senla.dao.User2SubscriptionDao;
import com.senla.domain.model.entity.Subscription;
import com.senla.domain.model.entity.User;
import com.senla.domain.model.entity.User2Subscription;
import com.senla.service.SubscriptionService;
import com.senla.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class SubscriptionServiceImpl extends AbstractServiceImpl<Subscription, SubscriptionDao> implements SubscriptionService {

    private final UserService userService;
    private final SubscriptionDao subscriptionDao;
    private final User2SubscriptionDao user2SubscriptionDao;

    @Transactional
    @Override
    public void setSubscriptionToUser(User user, Subscription subscription) {
        user = userService.update(user); //to make user managed by hibernate

        if (!Objects.isNull(user.getUser2Subscription())) {
            user.setUser2Subscription(null); //deleting previous subscription
        }

        User2Subscription user2Subscription = User2Subscription.builder()
                .user(user)
                .subscription(subscription)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusDays(subscription.getDurationInDays()))
                .build();

        user2SubscriptionDao.create(user2Subscription);
    }

    @Override
    protected SubscriptionDao getDefaultDao() {
        return subscriptionDao;
    }

    @Override
    protected Class<Subscription> getDefaultEntityClass() {
        return Subscription.class;
    }
}
