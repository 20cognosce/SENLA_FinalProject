package com.senla.controller;

import com.senla.controller.dto.LoginDto;
import com.senla.controller.dto.TokenDto;
import com.senla.model.entity.User;
import com.senla.security.jwt.JwtTokenSupplier;
import com.senla.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class LoginController {

    private final LoginService loginService;
    private final JwtTokenSupplier jwtTokenSupplier;

    @PostMapping(value = "/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) {
        User user;
        try {
            user = loginService.tryToLoginReturnUserIfSuccess(loginDto);
        } catch (IllegalArgumentException e) {
            log.error("Неудачная попытка залогиниться", e);
            throw new RuntimeException(e);
        }
        String token = jwtTokenSupplier.generateToken(loginDto.getLogin(), user.getRole().name());
        return ResponseEntity.ok(new TokenDto(loginDto.getLogin(), token));
    }
}
