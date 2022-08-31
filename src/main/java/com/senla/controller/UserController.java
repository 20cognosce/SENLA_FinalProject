package com.senla.controller;

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
import com.senla.model.entity.Subscription;
import com.senla.model.entity.Tariff;
import com.senla.model.entity.User;
import com.senla.model.entityenum.Role;
import com.senla.security.UserDetailsImpl;
import com.senla.service.SubscriptionService;
import com.senla.service.TariffService;
import com.senla.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final TariffService tariffService;
    private final SubscriptionService subscriptionService;

    @PostMapping("/root")
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

    @PostMapping
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
        if (userService.isAgeUnder18(userCreationDto.getDateOfBirth())) {
            throw new IncorrectAgeException();
        }

        User user = userMapper.convertToUser(userCreationDto);

        try {
            userService.create(user);
        } catch (Exception e) {
            throw new RuntimeException("Неудачная попытка создания пользователя", e);
        }
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

    @GetMapping(value = "/{id}")
    public UserDto getById(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        return userMapper.convertToDto(user);
    }

    @PatchMapping(value = "/{id}")
    public void updateUserById(@PathVariable("id") Long id, @RequestBody UserUpdateDto userUpdateDto) {
        User user = userService.getById(id);
        String phone = userUpdateDto.getPhone();
        LocalDate dateOfBirth = userUpdateDto.getDateOfBirth();

        if (Objects.nonNull(phone)) {
            if (userService.isPhoneIncorrect(phone)) {
                throw new IncorrectPhoneException();
            }
            if (userService.isPhoneUnavailable(phone)) {
                throw new KeyAlreadyExistsException("Пользователь с таким номером телефона уже существует");
            }
        }

        if (Objects.nonNull(dateOfBirth)) {
            if (userService.isAgeUnder18(dateOfBirth)) {
                throw new IncorrectAgeException();
            }
        }

        user = userService.updateEntityFromDto(user, userUpdateDto, User.class);
        userService.update(user);
    }

    @PatchMapping(value = "/{id}/credentials")
    public void updateUserCredentialsById(@PathVariable Long id,
                                          @RequestBody UserCredentialsUpdateDto credentialsUpdateDto) {

        String login = credentialsUpdateDto.getLogin();
        String password = credentialsUpdateDto.getPassword();
        User user = userService.getById(id);

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

    @PatchMapping(value = "/{id}", params = {"role"})
    public void updateUserRoleById(@PathVariable("id") Long id, @RequestParam(value = "role") String role) {
        Role newRole = Role.valueOf(role);
        User user = userService.getById(id);
        if (newRole == Role.ROOT || user.getRole() == Role.ROOT) {
            throw new IllegalArgumentException("Невозможно присвоить или изменить роль ROOT");
        }
        user.setRole(newRole);
        userService.update(user);
    }

    @PatchMapping(value = "/{id}/tariff", params = {"tariff-id"})
    public void updateUserTariffById(@PathVariable Long id,
                                     @RequestParam(value = "tariff-id") Long tariffId) {
        Tariff tariff = tariffService.getById(tariffId);
        User user = userService.getById(id);
        user.setTariff(tariff);
        userService.update(user);
    }

    @PatchMapping(value = "/{id}/subscription", params = {"subscription-id"})
    public void updateUserSubscriptionById(@PathVariable Long id,
                                           @RequestParam(value = "subscription-id") Long subscriptionId) {
        Subscription subscription = subscriptionService.getById(subscriptionId);
        User user = userService.getById(id);
        subscriptionService.setSubscriptionToUser(user, subscription);
    }

    @GetMapping(value = "/my")
    public UserDto getUserByAuth(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return userMapper.convertToDto(user);
    }

    @PatchMapping(value = "/my")
    public void updateUserByAuth(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                 @RequestBody UserUpdateDto userUpdateDto) {
        updateUserById(userDetails.getId(), userUpdateDto);
    }

    @PatchMapping(value = "/my/credentials")
    public void updateUserCredentialsByAuth(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody UserCredentialsUpdateDto credentialsUpdateDto) {
        updateUserCredentialsById(userDetails.getId(), credentialsUpdateDto);
    }

    @PatchMapping(value = "/my/tariff", params = {"tariff-id"})
    public void updateUserTariffByAuth(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                       @RequestParam(value = "tariff-id") Long tariffId) {
        updateUserTariffById(userDetails.getId(), tariffId);
    }

    @PatchMapping(value = "/my/subscription", params = {"subscription-id"})
    public void updateUserSubscriptionByAuth(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @RequestParam(value = "subscription-id") Long subscriptionId) {
        if (true) { //assuming user has paid for subscription
            updateUserSubscriptionById(userDetails.getId(), subscriptionId);
        }
    }
}
