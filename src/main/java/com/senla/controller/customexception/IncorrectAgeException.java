package com.senla.controller.customexception;

public class IncorrectAgeException extends RuntimeException {

    public IncorrectAgeException() {
        super("Сервис доступен только для пользователей старше 18-ти лет.");
    }
}
