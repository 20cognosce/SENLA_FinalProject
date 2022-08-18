package com.senla.controller.custom_exceptions;

import java.util.NoSuchElementException;

public class EntityNotFoundByIdException extends RuntimeException {

    public <EntityClass> EntityNotFoundByIdException(EntityClass entityClass, Long id) {
        super("Объект класса " + "\"" + entityClass + "\"" + " c id = " + id + " не найден.", new NoSuchElementException());
    }
}
