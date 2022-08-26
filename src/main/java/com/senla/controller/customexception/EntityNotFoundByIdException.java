package com.senla.controller.customexception;

import java.util.NoSuchElementException;

public class EntityNotFoundByIdException extends RuntimeException {

    public <EntityClass> EntityNotFoundByIdException(Long id, EntityClass entityClass) {
        super("Объект класса " + "\"" + entityClass + "\"" + " c id = " + id + " не найден.", new NoSuchElementException());
    }
}
