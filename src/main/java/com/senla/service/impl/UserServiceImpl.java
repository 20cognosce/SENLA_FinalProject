package com.senla.service.impl;

import com.senla.controller.customexception.EntityNotFoundByIdException;
import com.senla.dao.UserDao;
import com.senla.model.entity.RentalPoint;
import com.senla.model.entity.User;
import com.senla.model.entityenum.UserAccountStatus;
import com.senla.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class UserServiceImpl extends AbstractServiceImpl<User, UserDao> implements UserService {

    private final UserDao userDao;

    @Transactional
    @Override
    public void deleteById(Long userId) {
        User user = getDefaultDao().getById(userId)
                .orElseThrow(() -> new EntityNotFoundByIdException(userId, RentalPoint.class));
        user.setStatus(UserAccountStatus.DELETED);
        getDefaultDao().update(user);
    }

    @Override
    protected UserDao getDefaultDao() {
        return userDao;
    }

    @Override
    public boolean isRootCreated() {
        return getDefaultDao().isRootCreated();
    }

    @Override
    public User getUserByLogin(String login) throws UsernameNotFoundException{
        return getDefaultDao().getUserByLogin(login).orElseThrow(() -> new UsernameNotFoundException(login));
    }

    @Override
    public boolean isLoginIncorrect(String login) {
        Pattern loginPattern = Pattern.compile("^([a-zA-Z])+([.\\-_]?[a-zA-Z\\d]+)+@([a-z]+)\\.(com|ru|by)$");
        Matcher matcher = loginPattern.matcher(login);
        return !matcher.find();
    }

    @Override
    public boolean isPasswordIncorrect(String password) {
        Pattern passwordPattern = Pattern.compile("^([a-zA-Z\\d#?!@$%^&*-]){8,}$");
        Matcher matcher = passwordPattern.matcher(password);
        return !matcher.find();
    }

    @Override
    public boolean isPhoneIncorrect(String phone) {
        Pattern phonePattern = Pattern.compile("^(8|\\+7|\\+375)(\\(\\d{3}\\))(\\d{7})$");
        Matcher matcher = phonePattern.matcher(phone);
        return !matcher.find();
    }

    @Override
    public boolean isLoginUnavailable(String login) {
        return getDefaultDao().getUserByLogin(login).isPresent();
    }

    @Override
    public boolean isPhoneUnavailable(String phone) {
        return getDefaultDao().getUserByPhone(phone).isPresent();
    }
}
