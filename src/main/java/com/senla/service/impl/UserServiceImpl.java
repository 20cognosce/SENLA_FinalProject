package com.senla.service.impl;

import com.senla.dao.UserDao;
import com.senla.model.entity.User;
import com.senla.model.entity_enum.UserAccountStatus;
import com.senla.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl extends AbstractServiceImpl<User, UserDao> implements UserService {

    UserDao userDao;

    @Override
    public void createUser(User user) {

    }

    @Override
    public void delete(User user) {
        user.setStatus(UserAccountStatus.DELETED);
    }

    @Override
    protected UserDao getDefaultDao() {
        return userDao;
    }
}
