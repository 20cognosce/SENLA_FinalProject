package com.senla.service.impl;

import com.senla.dao.UserDao;
import com.senla.model.entity.User;
import com.senla.model.entityenum.UserAccountStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void deleteById() {
        User user = new User();
        user.setStatus(UserAccountStatus.ACTIVE);
        when(userDao.getById(1L)).thenReturn(Optional.of(user));

        userService.deleteById(1L);

        assertEquals(user.getStatus(), UserAccountStatus.DELETED);
    }

    @Test
    void isRootCreated() {
        userService.isRootCreated();
        verify(userDao).isRootCreated();
    }

    @Test
    void getUserByLogin_EmptyOptionalReturnedFromDao_EntityNotFoundByIdExceptionThrown() {
        doReturn(Optional.empty()).when(userDao).getUserByLogin("login");

        assertThrows(UsernameNotFoundException.class, () -> userService.getUserByLogin("login"));
    }

    @Test
    void getUserByLogin_PresentOptionalReturnedFromDao_EntityNotFoundByIdExceptionThrown() {
        User user = new User();
        doReturn(Optional.of(user)).when(userDao).getUserByLogin("login");

        assertEquals(user, userService.getUserByLogin("login"));
    }

    @Test
    void isLoginIncorrect_WrongPattern_TrueReturned() {
        assertTrue(userService.isLoginIncorrect("20user@yandex.ru"));
        assertTrue(userService.isLoginIncorrect("login.@yandex.ru"));
        assertTrue(userService.isLoginIncorrect("user@yandex.io"));
        assertTrue(userService.isLoginIncorrect("a_-2@yandex.com"));
    }

    @Test
    void isLoginIncorrect_CorrectPattern_FalseReturned() {
        assertFalse(userService.isLoginIncorrect("user20user20@yandex.ru"));
        assertFalse(userService.isLoginIncorrect("user.login@yandex.ru"));
        assertFalse(userService.isLoginIncorrect("user.02@yandex.ru"));
        assertFalse(userService.isLoginIncorrect("user_02_02@yandex.ru"));
        assertFalse(userService.isLoginIncorrect("user-02@yandex.ru"));
    }

    @Test
    void isPasswordIncorrect_WrongPattern_TrueReturned() {
        assertTrue(userService.isPasswordIncorrect("user2$"));
        assertTrue(userService.isPasswordIncorrect("use-use"));
    }
    @Test
    void isPasswordIncorrect_CorrectPattern_FalseReturned() {
        assertFalse(userService.isPasswordIncorrect("user^user"));
        assertFalse(userService.isPasswordIncorrect("12345678"));
        assertFalse(userService.isPasswordIncorrect("user2002$"));
    }

    @Test
    void isPhoneIncorrect_WrongPattern_TrueReturned() {
        assertTrue(userService.isPhoneIncorrect("+7901401320"));
        assertTrue(userService.isPhoneIncorrect("+7(3901)1401320"));
        assertTrue(userService.isPhoneIncorrect("+7(901)701320"));
        assertTrue(userService.isPhoneIncorrect("89014013207"));
    }

    @Test
    void isPhoneIncorrect_CorrectPattern_FalseReturned() {
        assertFalse(userService.isPhoneIncorrect("+7(901)4013207"));
        assertFalse(userService.isPhoneIncorrect("8(000)0000000"));
        assertFalse(userService.isPhoneIncorrect("+375(999)1234567"));
    }

    @Test
    void isAgeUnder18_AgeIsUnder18_TrueReturned() {
        assertTrue(userService.isAgeUnder18(LocalDate.now().minusYears(17)));
    }

    @Test
    void isAgeUnder18_AgeIsOver18_FalseReturned() {
        assertFalse(userService.isAgeUnder18(LocalDate.now().minusYears(19)));
    }

    @Test
    void isLoginUnavailable_PresentOptionalReturnedFromDao_TrueReturned() {
        doReturn(Optional.of(new User())).when(userDao).getUserByLogin("login");
        assertTrue(userService.isLoginUnavailable("login"));
    }

    @Test
    void isLoginUnavailable_EmptyOptionalReturnedFromDao_FalseReturned() {
        doReturn(Optional.empty()).when(userDao).getUserByLogin("login");
        assertFalse(userService.isLoginUnavailable("login"));
    }

    @Test
    void isPhoneUnavailable_PresentOptionalReturnedFromDao_TrueReturned() {
        doReturn(Optional.of(new User())).when(userDao).getUserByPhone("phone");
        assertTrue(userService.isPhoneUnavailable("phone"));
    }

    @Test
    void isPhoneUnavailable_EmptyOptionalReturnedFromDao_FalseReturned() {
        doReturn(Optional.empty()).when(userDao).getUserByPhone("phone");
        assertFalse(userService.isPhoneUnavailable("phone"));
    }

    @Test
    void getDefaultDao() {
        assertEquals(userDao.getClass(), userService.getDefaultDao().getClass());
    }

    @Test
    void getDefaultEntityClass() {
        assertEquals(User.class, userService.getDefaultEntityClass());
    }
}