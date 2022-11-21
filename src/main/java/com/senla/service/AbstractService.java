package com.senla.service;

import com.senla.controller.customexception.EntityNotFoundByIdException;
import com.senla.domain.dto.selection.SelectionDto;
import org.springframework.lang.NonNull;

import javax.persistence.NonUniqueResultException;
import java.util.List;
import java.util.Map;

public interface AbstractService<T> {

    void create(T element) throws NonUniqueResultException;

    T update(T element);

    void deleteById(Long id) throws EntityNotFoundByIdException;

    /**
     * <br><b>NOTE:</b> method does not select null values.
     */
    List<T> getAll(@NonNull Map<String, Object> fieldNamesAndValuesToSelectBy,
                   String orderBy,
                   boolean asc,
                   int limit);

    T getById(Long id) throws EntityNotFoundByIdException;

    /**
     * Takes a detached original entity object and updates it fields according to DTO values.
     * <br><b>NOTE:</b> method does not set null values.
     * @param original Object we want to update with its DTO
     * @param dto DTO object which contains new values for setting
     * @param originalClass Class of the original object
     * @param <DTO> Class of the corresponding DTO
     * @param <O> Original object class
     * @return original object with updated fields
     */
    <DTO, O> O updateEntityFromDto(O original, DTO dto, Class<O> originalClass);

    /**
     * Converting an instance of any object, which is the selection model, into a map of field's name and value to select
     * <p>
     *     <b>NOTE:</b>
     *     Use only primitive types, boxed types, parsable types like LocalDate or Enum in model
     *     so that it could be selected within one table's line without joins.
     * </p>
     * @author Dmitry Vert
     * @since 2022-08-17
     * @param model an instance of any class, intended only for extracting values for query
     * @return {@link Map} that contains {@code String fieldName} as key
     * and {@code Object fieldValue} as value
     * */
     Map<String, Object> getMapOfObjectFieldsAndValues(Object model);
}
