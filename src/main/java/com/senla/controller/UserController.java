package com.senla.controller;

import com.senla.controller.dto.UserDto;
import com.senla.controller.mapper.UserMapper;
import com.senla.model.entity.User;
import com.senla.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "/v1/users", produces = {"application/json; charset=UTF-8"})
@RestController
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

    @PostMapping("/register")
    public void registerGuest(@RequestBody UserDto userDto) {
        User user = userMapper.convertToUser(userDto);
        userService.createUser(user);
        //TODO: так как login - not null unique в БД,
        //то при попытке создания пользователя с уже существующим логином EntityManager должен кинуть эксепшн
    }

    /*@GetMapping(params = {"fieldToOrderBy", "asc", "limit"})
    public List<UserDto> getAll(@RequestBody UserSelectionDto selectionModel,
                                @RequestParam(value = "fieldToOrderBy", defaultValue = "id", required = false) String fieldToOrderBy,
                                @RequestParam(value = "asc", defaultValue = "true", required = false) boolean asc,
                                @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit) {

        Map<String, Object> selectParameters = userService.getMapOfObjectFieldsAndValues(selectionModel);
        selectParameters.entrySet().removeIf(entry -> Objects.isNull(entry.getValue()));

        List<User> users = userService.getAll(selectParameters, fieldToOrderBy, asc, limit);
        return users.stream().map(userMapper::convertToDto).collect(toList());
    }*/

    @GetMapping
    public ResponseEntity<?> greeting() {
        return ResponseEntity.ok("Я работаю");
    }
}
