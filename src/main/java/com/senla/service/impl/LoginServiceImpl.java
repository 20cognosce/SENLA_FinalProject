package com.senla.service.impl;

import com.senla.dao.UserDao;
import com.senla.domain.dto.LoginDto;
import com.senla.domain.model.entity.User;
import com.senla.domain.model.entityenum.UserAccountStatus;
import com.senla.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginService {

    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;

    @Override
    public User tryToLoginReturnUserIfSuccess(LoginDto loginDto) throws IllegalArgumentException {
        User user = userDao.getUserByLogin(loginDto.getLogin())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с логином " + loginDto.getLogin() + " не найден"));

        if (user.getStatus() == UserAccountStatus.DELETED) {
            throw new IllegalArgumentException("Аккаунт удален");
        }
        if (user.getStatus() == UserAccountStatus.SUSPENDED) {
            throw new IllegalArgumentException("Аккаунт заморожен");
        }
        if (passwordEncoder.matches(loginDto.getPassword(), user.getHashPassword())) {
            return user;
        } else {
            throw new IllegalArgumentException("Неправильный пароль");
        }
    }
}
