package com.senla.service.impl;

import com.senla.dao.SubscriptionDao;
import com.senla.dao.User2SubscriptionDao;
import com.senla.dao.UserDao;
import com.senla.model.entity.Subscription;
import com.senla.model.entity.User;
import com.senla.model.entity.User2Subscription;
import com.senla.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class SubscriptionServiceImpl extends AbstractServiceImpl<Subscription, SubscriptionDao> implements SubscriptionService {

    private final SubscriptionDao subscriptionDao;
    private final User2SubscriptionDao user2SubscriptionDao;
    private final UserDao userDao;

    @Transactional
    @Override
    public void setSubscriptionToUser(User user, Subscription subscription) {
        User2Subscription currentUser2Subscription = user.getUser2Subscription();
        if (!Objects.isNull(currentUser2Subscription)) {
            user2SubscriptionDao.delete(currentUser2Subscription);
        }

        User2Subscription user2Subscription = User2Subscription.builder()
                .user(user)
                .subscription(subscription)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusDays(30))
                .build();

        if ("Вечная".equals(subscription.getName())) {
            user2Subscription.setEndTime(LocalDateTime.now().plusYears(100));
        }

        user2SubscriptionDao.create(user2Subscription);
        userDao.refresh(user);
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
