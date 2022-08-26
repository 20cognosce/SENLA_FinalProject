package com.senla.controller.customexception;

public class IllegalRoleForActionException extends RuntimeException {

    public IllegalRoleForActionException(String message) {
        super(message);
    }
}
