package com.senla.service.impl;

import com.senla.controller.customexception.EntityNotFoundByIdException;
import com.senla.dao.UserDao;
import com.senla.domain.dto.update.UserUpdateDto;
import com.senla.domain.model.entity.User;
import com.senla.domain.model.entityenum.Role;
import com.senla.domain.model.entityenum.UserAccountStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractServiceImplTest extends AbstractServiceImpl<User, UserDao> {

    @Mock
    private UserDao userDao;

    @Override
    protected UserDao getDefaultDao() {
        return userDao;
    }

    @Override
    protected Class<User> getDefaultEntityClass() {
        return User.class;
    }

    @Test
    void create() {
        User user = new User();
        create(user);
        verify(getDefaultDao()).create(user);
    }

    @Test
    void update() {
        User user = new User();
        update(user);
        verify(getDefaultDao()).update(user);
    }

    @Test
    void deleteById_EmptyOptionalReturnedFromDao_EntityNotFoundByIdExceptionThrown() {
        doReturn(Optional.empty()).when(getDefaultDao()).getById(1L);
        assertThrows(EntityNotFoundByIdException.class, () -> deleteById(1L));
    }

    @Test
    void deleteById_PresentOptionalReturnedFromDao_DefaultDaoDeleteMethodInvoked() {
        User user = new User();
        doReturn(Optional.of(user)).when(getDefaultDao()).getById(1L);

        deleteById(1L);
        verify(getDefaultDao()).delete(user);
    }

    @Test
    void getAll() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < (int) (Math.random() * 90 + 10); i++) {
            users.add(new User());
        }
        when(getDefaultDao().getAll(new HashMap<>(), "id", true, 100)).thenReturn(users);

        assertEquals(users, getAll(new HashMap<>(), "id", true, 100));
    }

    @Test
    void getById_EmptyOptionalReturnedFromDao_EntityNotFoundByIdExceptionThrown() {
        when(getDefaultDao().getById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundByIdException.class, () -> getById(1L));
    }

    @Test
    void getById_PresentOptionalReturnedFromDao_UserReturned() {
        User user = new User();
        when(getDefaultDao().getById(1L)).thenReturn(Optional.of(user));

        assertEquals(user, getById(1L));
    }

    @Test
    void updateEntityFromDto() {
        User user1 = new User();
        User user2 = User.builder()
                .name("user")
                .phone("+7(901)1234567")
                .dateOfBirth(LocalDate.now())
                .build();

        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setName("user");
        userUpdateDto.setPhone("+7(901)1234567");
        userUpdateDto.setDateOfBirth(LocalDate.now());

        updateEntityFromDto(user1, userUpdateDto, User.class);
        assertTrue(new ReflectionEquals(user1, "").matches(user2)); // by-field comparison
    }

    @Test
    void getMapOfObjectFieldsAndValues() {
        User user = User.builder()
                .id(1L)
                .login("user@gmail.com")
                .role(Role.USER)
                .status(UserAccountStatus.ACTIVE)
                .name("user")
                .phone("+7(901)1234567")
                .dateOfBirth(LocalDate.now())
                .build();

        HashMap<String, Object> map = new HashMap<>() {{
            put("id", 1L);
            put("login", "user@gmail.com");
            put("role", Role.USER);
            put("status", UserAccountStatus.ACTIVE);
            put("name", "user");
            put("phone", "+7(901)1234567");
            put("dateOfBirth", LocalDate.now());
        }};

        assertEquals(map, getMapOfObjectFieldsAndValues(user));
    }
}