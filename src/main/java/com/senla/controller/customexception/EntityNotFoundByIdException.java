package com.senla.controller.customexception;

import java.util.NoSuchElementException;

public class EntityNotFoundByIdException extends RuntimeException {

    public <T> EntityNotFoundByIdException(Long id, Class<T> clazz) {
        super("Объект класса " + "\"" + clazz.getName() + "\"" + " c id = " + id + " не найден.", new NoSuchElementException());
    }
}
