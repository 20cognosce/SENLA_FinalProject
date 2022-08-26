package com.senla.controller.customexception;

public class IncorrectPasswordException extends RuntimeException {

    public IncorrectPasswordException() {
        super("Пароль должен быть больше 8-ми символов и содержать только латинские буквы, " +
                "специальные символы #?!@$%^&*- и цифры.");
    }
}
