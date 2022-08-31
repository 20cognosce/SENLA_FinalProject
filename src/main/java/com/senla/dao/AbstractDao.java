package com.senla.dao;

import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AbstractDao<T> {

    void create(T element);

    Optional<T> getById(Long id);

    void update(T element);

    void delete(T element);
    void refresh(T element);

    List<T> getAll(@NonNull Map<String, Object> mapOfFieldNamesAndValuesToSelectBy,
                   String orderBy,
                   boolean asc,
                   int limit);
}
