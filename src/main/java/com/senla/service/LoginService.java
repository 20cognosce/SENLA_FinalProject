package com.senla.service;

import com.senla.controller.dto.LoginDto;
import com.senla.model.entity.User;

public interface LoginService {

    User tryToLoginReturnUserIfSuccess(LoginDto loginDto);
}
