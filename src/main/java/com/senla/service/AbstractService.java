package com.senla.service;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface AbstractService<T> {

    void create(T element) throws KeyAlreadyExistsException;

    void delete(T element) throws NoSuchElementException;

    /*
    map.put("country", "Республика Беларусь");
    map.put("county", "Гродненская область");
    map.put("street", "улица Кабяка");
     */
    List<T> getAll(Map<String, Object> mapOfFieldNamesAndValuesToSelectBy,
                   String fieldToOrderBy,
                   boolean asc,
                   int limit);

    Optional<T> getById(long id);

    <DTO> T updateEntityFromDto(T original, DTO dto, Class<T> originalClass);

    /**
     * This method takes a selection DTO object, which is the model for selection.
     * If the field name is composite, it will be parsed and put renamed like dateOfBirth -> date_of_birth.
     * <p>
     *     <b>NOTE:</b>
     *     Use only primitive types, boxed types, parsable types like LocalDate or Enum in SelectionDto
     *     so that it could be selected within one table's line.
     * </p>
     * <p>
     *     <b>NOTE:</b>
     *     Field's name must be corresponded with table's column: name -> name, anyField -> any_field
     * </p>
     *
     * @author Dmitry Vert
     * @since 2022-08-17
     * @return {@link Map} that contains {@code String fieldName} as key
     * and {@code Object fieldValue} as value
     * */
    <SelectionDtoClass> Map<String, Object> getMapOfObjectFieldsAndValues(SelectionDtoClass model);
}
