package com.senla.service.impl;

import com.senla.controller.customexception.EntityNotFoundByIdException;
import com.senla.controller.dto.selection.SelectionDto;
import com.senla.dao.AbstractDao;
import com.senla.model.entity.RentalPoint;
import com.senla.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NonUniqueResultException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public abstract class AbstractServiceImpl<T, D extends AbstractDao<T>> implements AbstractService<T> {

    protected abstract D getDefaultDao();

    @Transactional
    @Override
    public void create(T element) {
        getDefaultDao().create(element);
    }

    @Transactional
    @Override
    public void update(T element) {
        getDefaultDao().update(element);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        getDefaultDao().delete(getDefaultDao().getById(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(id, RentalPoint.class)));
    }

    @Override
    public List<T> getAll(Map<String, Object> mapOfFieldNamesAndValuesToSelectBy,
                          String orderBy,
                          boolean ascending,
                          int limit) {
        return getDefaultDao().getAll(mapOfFieldNamesAndValuesToSelectBy, orderBy, ascending, limit);
    }

    @Override
    public Optional<T> getById(Long id) {
        return getDefaultDao().getById(id);
    }

    @Override
    public <DTO, O> O updateEntityFromDto(O original, DTO dto, Class<O> originalClass) {
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
    public Map<String, Object> getMapOfObjectFieldsAndValues(SelectionDto model) {
        if (Objects.isNull(model)) {
            return new HashMap<>();
        }

        Field[] fields = model.getClass().getDeclaredFields();
        Map<String, Object> result = new HashMap<>();

        Arrays.stream(fields).forEach(field -> {
            try {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object fieldValue = field.get(model);
                result.put(fieldName, fieldValue);
            } catch (IllegalAccessException e) {
                log.error("Поле объекта DTO не доступно для модификации", e);
                throw new RuntimeException(e);
            }
        });
        return result;
    }
}
