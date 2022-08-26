package com.senla.controller.customexception;

public class IncorrectPhoneException extends RuntimeException {

    public IncorrectPhoneException() {
        super("Некорректный формат номера телефона. Необходимый формат: \"+7/8/+375(ХХХ)1234567\".");
    }
}
