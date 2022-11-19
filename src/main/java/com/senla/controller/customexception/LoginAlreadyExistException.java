package com.senla.controller.customexception;

public class LoginAlreadyExistException extends RuntimeException {

    public LoginAlreadyExistException(String message) {
        super(message);
    }
}
