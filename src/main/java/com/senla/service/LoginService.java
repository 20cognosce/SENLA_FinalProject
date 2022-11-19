package com.senla.service;

import com.senla.domain.dto.LoginDto;
import com.senla.domain.model.entity.User;

public interface LoginService {

    User tryToLoginReturnUserIfSuccess(LoginDto loginDto);
}
