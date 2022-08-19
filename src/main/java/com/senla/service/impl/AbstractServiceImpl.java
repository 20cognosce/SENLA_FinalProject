package com.senla.service.impl;

import com.senla.controller.dto.selection.SelectionDto;
import com.senla.dao.AbstractDao;
import com.senla.service.AbstractService;
import lombok.extern.slf4j.Slf4j;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public abstract class AbstractServiceImpl<T, D extends AbstractDao<T>> implements AbstractService<T> {

    protected abstract D getDefaultDao();

    @Override
    public void create(T element) throws KeyAlreadyExistsException {
        getDefaultDao().create(element);
    }

    @Override
    public void delete(T element) throws NoSuchElementException {
        getDefaultDao().delete(element);
    }

    @Override
    public List<T> getAll(Map<String, Object> mapOfFieldNamesAndValuesToSelectBy,
                          String orderBy,
                          boolean ascending,
                          int limit) {
        return getDefaultDao().getAll(mapOfFieldNamesAndValuesToSelectBy, orderBy, ascending, limit);
    }

    @Override
    public Optional<T> getById(long id) {
        return getDefaultDao().getById(id);
    }

    @Override
    public <DTO> T updateEntityFromDto(T original, DTO dto, Class<T> originalClass) {
        Field[] dtoFields = dto.getClass().getDeclaredFields();

        Arrays.stream(dtoFields).forEach(dtoField -> {
            try {
                dtoField.setAccessible(true);
                if (Objects.isNull(dtoField.get(dto))) {
                    // Null-поля в DTO не записываются в оригинал,
                    // чтобы в DTO можно было передать только 1 изменяемое поле,
                    // а остальные приняли null по умолчанию
                    return;
                }
                Field originalField = original.getClass().getDeclaredField(dtoField.getName());
                originalField.setAccessible(true);
                originalField.set(original, dtoField.get(dto));
            } catch (NoSuchFieldException e) {
                log.error("У оригинального объекта нет поля с именем " + dtoField.getName(), e);
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                log.error("Поле объекта DTO не доступно для модификации", e);
                throw new RuntimeException(e);
            }
        });
        return original;
    }

    @Override
    public <SelectionDtoClass> Map<String, Object> getMapOfObjectFieldsAndValues(@SelectionDto SelectionDtoClass model) {
        if (Objects.isNull(model)) {
            return new HashMap<>();
        }
        Field[] fields = model.getClass().getDeclaredFields();
        Map<String, Object> result = new HashMap<>();

        Arrays.stream(fields).forEach(field -> {
            try {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object fieldValue = field.get(model) instanceof Enum ? ((Enum<?>) field.get(model)).name() : field.get(model);
                result.put(fieldName, fieldValue);
            } catch (IllegalAccessException e) {
                log.error("Поле объекта DTO не доступно для модификации", e);
                throw new RuntimeException(e);
            }
        });
        return result;
    }
}
