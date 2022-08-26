package com.senla.controller.customexception;

public class IncorrectLoginException extends RuntimeException {

    public IncorrectLoginException() {
        super("Некорректный формат логина. Используйте свой почтовый ящик c доменом .com, .ru или .by");
    }
}
