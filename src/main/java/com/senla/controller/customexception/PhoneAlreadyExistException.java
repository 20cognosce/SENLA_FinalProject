package com.senla.controller.customexception;

public class PhoneAlreadyExistException extends RuntimeException {

    public PhoneAlreadyExistException(String message) {
        super(message);
    }
}
