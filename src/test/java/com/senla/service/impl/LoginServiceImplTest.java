package com.senla.service.impl;

import com.senla.controller.dto.LoginDto;
import com.senla.dao.UserDao;
import com.senla.model.entity.User;
import com.senla.model.entityenum.UserAccountStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceImplTest {

    @Spy
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(16);
    @Mock
    private UserDao userDao;
    @InjectMocks
    private LoginServiceImpl loginService;

    @Test
    void tryToLoginReturnUserIfSuccess_UserStatusDeleted_IllegalArgumentExceptionThrown() {
        LoginDto loginDto = new LoginDto();
        loginDto.setLogin("user");
        loginDto.setPassword("user");
        User user = new User();
        user.setStatus(UserAccountStatus.DELETED);
        when(userDao.getUserByLogin("user")).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> loginService.tryToLoginReturnUserIfSuccess(loginDto));
    }

    @Test
    void tryToLoginReturnUserIfSuccess_UserStatusSuspended_IllegalArgumentExceptionThrown() {
        LoginDto loginDto = new LoginDto();
        loginDto.setLogin("user");
        loginDto.setPassword("user");
        User user = new User();
        user.setStatus(UserAccountStatus.SUSPENDED);
        when(userDao.getUserByLogin("user")).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> loginService.tryToLoginReturnUserIfSuccess(loginDto));
    }

    @Test
    void tryToLoginReturnUserIfSuccess_IncorrectLogin_IllegalArgumentExceptionThrown() {
        LoginDto loginDto = new LoginDto();
        loginDto.setLogin("user");
        loginDto.setPassword("user");
        when(userDao.getUserByLogin("user")).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> loginService.tryToLoginReturnUserIfSuccess(loginDto));
    }

    @Test
    void tryToLoginReturnUserIfSuccess_IncorrectPassword_IllegalArgumentExceptionThrown() {
        LoginDto loginDto = new LoginDto();
        loginDto.setLogin("user");
        loginDto.setPassword("user");
        User user = new User();
        user.setHashPassword(passwordEncoder.encode("qwerty"));
        when(userDao.getUserByLogin("user")).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> loginService.tryToLoginReturnUserIfSuccess(loginDto));
    }

    @Test
    void tryToLoginReturnUserIfSuccess_CorrectCredentials_UserReturned() {
        LoginDto loginDto = new LoginDto();
        loginDto.setLogin("user");
        loginDto.setPassword("user");
        User user = new User();
        user.setHashPassword(passwordEncoder.encode("user"));
        when(userDao.getUserByLogin("user")).thenReturn(Optional.of(user));

        assertEquals(user, loginService.tryToLoginReturnUserIfSuccess(loginDto));
    }
}