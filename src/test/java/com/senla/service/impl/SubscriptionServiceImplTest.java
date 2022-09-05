package com.senla.service.impl;

import com.senla.dao.SubscriptionDao;
import com.senla.dao.User2SubscriptionDao;
import com.senla.model.entity.Subscription;
import com.senla.model.entity.User;
import com.senla.model.entity.User2Subscription;
import com.senla.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceImplTest {

    @Mock
    private UserService userService;
    @Mock
    private SubscriptionDao subscriptionDao;
    @Mock
    private User2SubscriptionDao user2SubscriptionDao;
    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    @Test
    void setSubscriptionToUser_UserAlreadyHasSubscription_OldSubscriptionDeletedNewSubscriptionSet() {
        User user = spy(User.class);
        User2Subscription oldUser2Subscription = new User2Subscription();
        User2Subscription newUser2Subscription = new User2Subscription();
        user.setUser2Subscription(oldUser2Subscription); //mock ignores that
        Subscription subscription = Subscription.builder().durationInDays(14).build();

        doReturn(user).when(userService).update(user);
        doAnswer((invocationOnMock) -> {
            user.setUser2Subscription(newUser2Subscription);
            return null;
        }).when(user2SubscriptionDao).create(any(User2Subscription.class));

        subscriptionService.setSubscriptionToUser(user, subscription);

        verify(user).setUser2Subscription(null);
        assertEquals(newUser2Subscription, user.getUser2Subscription());
    }

    @Test
    void setSubscriptionToUser_UserDoesNotHaveSubscription_NewSubscriptionSet() {
        User user = new User();
        User2Subscription newUser2Subscription = new User2Subscription();
        Subscription subscription = Subscription.builder().durationInDays(14).build();

        doReturn(user).when(userService).update(user);
        doAnswer((invocationOnMock) -> {
            user.setUser2Subscription(newUser2Subscription);
            return null;
        }).when(user2SubscriptionDao).create(any(User2Subscription.class));

        subscriptionService.setSubscriptionToUser(user, subscription);

        assertEquals(newUser2Subscription, user.getUser2Subscription());
    }

    @Test
    void getDefaultDao() {
        assertEquals(subscriptionDao.getClass(), subscriptionService.getDefaultDao().getClass());
    }

    @Test
    void getDefaultEntityClass() {
        assertEquals(Subscription.class, subscriptionService.getDefaultEntityClass());
    }
}