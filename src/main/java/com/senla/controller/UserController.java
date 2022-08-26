package com.senla.controller;

import com.senla.controller.customexception.EntityNotFoundByIdException;
import com.senla.controller.customexception.IllegalRoleForActionException;
import com.senla.controller.customexception.IncorrectAgeException;
import com.senla.controller.customexception.IncorrectLoginException;
import com.senla.controller.customexception.IncorrectPasswordException;
import com.senla.controller.customexception.IncorrectPhoneException;
import com.senla.controller.dto.UserDto;
import com.senla.controller.dto.creation.UserCreationDto;
import com.senla.controller.dto.selection.UserSelectionDto;
import com.senla.controller.dto.update.UserCredentialsUpdateDto;
import com.senla.controller.dto.update.UserUpdateDto;
import com.senla.controller.mapper.UserMapper;
import com.senla.model.entity.User;
import com.senla.model.entityenum.Role;
import com.senla.security.UserDetailsImpl;
import com.senla.service.LoginService;
import com.senla.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class UserController {

    @Value("${root.login}")
    private String rootLogin;
    @Value("${root.password}")
    private String rootPassword;

    private final UserMapper userMapper;
    private final UserService userService;
    private final LoginService loginService;

    @PostMapping
    public void createRootUser() {
        if (userService.isRootCreated()) {
            throw new UnsupportedOperationException("ROOT пользователь уже создан");
        }

        UserCreationDto creationDto = UserCreationDto.builder()
                .login(rootLogin)
                .password(rootPassword)
                .name("root")
                .phone(null)
                .dateOfBirth(LocalDate.of(1970, 1, 1))
                .build();

        User root = userMapper.convertToUser(creationDto); //to hash the password
        root.setRole(Role.ROOT);
        userService.create(root);
    }

    @PostMapping(value = "/new")
    public void createUser(@RequestBody UserCreationDto userCreationDto) {
        String login = userCreationDto.getLogin();
        String password = userCreationDto.getPassword();
        String phone = userCreationDto.getPhone();

        if (userService.isLoginIncorrect(login)) {
            throw new IncorrectLoginException();
        }
        if (userService.isLoginUnavailable(login)) {
            throw new KeyAlreadyExistsException("Пользователь с таким логином уже существует");
        }
        if (userService.isPasswordIncorrect(password)) {
            throw new IncorrectPasswordException();
        }
        if (userService.isPhoneIncorrect(phone)) {
            throw new IncorrectPhoneException();
        }
        if (userService.isPhoneUnavailable(phone)) {
            throw new KeyAlreadyExistsException("Пользователь с таким номером телефона уже существует");
        }
        if (userCreationDto.getDateOfBirth().isAfter(LocalDate.now().minusYears(18))) {
            throw new IncorrectAgeException();
        }

        User user = userMapper.convertToUser(userCreationDto);
        try {
            userService.create(user);
        } catch (Exception e) {
            log.error("Неудачная попытка создания пользователя", e);
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/{id}")
    public UserDto getById(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userService.getById(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(id, User.class));

        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority(Role.USER.name()))) {
            if (userDetails.getId().equals(id)) {
                return userMapper.convertToDto(user);
            } else {
                throw new IllegalRoleForActionException("Доступ и данным других пользователей запрещен для роли USER");
            }
        }

        return userMapper.convertToDto(user);
    }

    @GetMapping
    public List<UserDto> getAll(@RequestBody(required = false) UserSelectionDto selectionModel,
                                @RequestParam(value = "orderBy", defaultValue = "id", required = false) String orderBy,
                                @RequestParam(value = "asc", defaultValue = "true", required = false) boolean asc,
                                @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit) {

        Map<String, Object> selectParameters = userService.getMapOfObjectFieldsAndValues(selectionModel);
        selectParameters.entrySet().removeIf(entry -> Objects.isNull(entry.getValue()));

        List<User> users = userService.getAll(selectParameters, orderBy, asc, limit);
        return users.stream().map(userMapper::convertToDto).collect(toList());
    }

    @PatchMapping(value = "/{id}/role")
    public void updateUserRole(@PathVariable("id") Long id, @RequestParam(value = "role") String role) {
        Role newRole = Role.valueOf(role);
        User user = userService.getById(id).orElseThrow(() -> new EntityNotFoundByIdException(id, User.class));
        if (newRole == Role.ROOT || user.getRole() == Role.ROOT) {
            throw new IllegalArgumentException("Невозможно присвоить или изменить роль ROOT");
        }
        user.setRole(newRole);
        userService.update(user);
    }

    @PatchMapping(value = "/{id}")
    public void updateUserCredentials(@PathVariable Long id,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @RequestBody UserCredentialsUpdateDto credentialsUpdateDto) {

        String login = credentialsUpdateDto.getLogin();
        String password = credentialsUpdateDto.getPassword();
        User user = userDetails.getUser();

        if (!Objects.equals(user.getId(), id)) {
            throw new IllegalArgumentException("Редактирование данных других пользователей запрещено.");
        }

        if (Objects.nonNull(login)) {
            if (userService.isLoginUnavailable(login)) {
                throw new KeyAlreadyExistsException("Пользователь с таким логином уже существует");
            }
            if (userService.isLoginIncorrect(login)) {
                throw new IncorrectLoginException();
            }
            user.setLogin(login);
        }
        if (Objects.nonNull(password)) {
            if (userService.isPasswordIncorrect(password)) {
                throw new IncorrectPasswordException();
            }
            UserCreationDto creationDto = new UserCreationDto();
            creationDto.setPassword(password);
            user.setHashPassword(userMapper.convertToUser(creationDto).getHashPassword());
        }

        userService.update(user);
    }

    @PatchMapping(value = "/{id}")
    public void updateUser(@PathVariable("id") Long id,
                           @AuthenticationPrincipal UserDetailsImpl userDetails,
                           @RequestBody UserUpdateDto userUpdateDto) {

        User user = userDetails.getUser();
        if (!Objects.equals(user.getId(), id)) {
            throw new IllegalArgumentException("Редактирование данных других пользователей запрещено.");
        }
        if (userService.isPhoneIncorrect(userUpdateDto.getPhone())) {
            throw new IncorrectPhoneException();
        }
        if (userService.isPhoneUnavailable(userUpdateDto.getPhone())) {
            throw new KeyAlreadyExistsException("Пользователь с таким номером телефона уже существует");
        }
        if (userUpdateDto.getDateOfBirth().isAfter(LocalDate.now().minusYears(18))) {
            throw new IncorrectAgeException();
        }

        //TODO: test
        user = userService.updateEntityFromDto(user, userUpdateDto, User.class);
        userService.update(user);
    }
}
