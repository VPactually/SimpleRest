package com.vpactually.mappers;

import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.openapitools.jackson.nullable.JsonNullable;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class JsonNullableMapper {

    public static <T> JsonNullable<T> wrap(T entity) {
        return JsonNullable.of(entity);
    }

    public static <T> T unwrap(JsonNullable<T> jsonNullable) {
        return jsonNullable == null ? null : jsonNullable.orElse((T) new Object());
    }

    @Condition
    public static <T> boolean isPresent(JsonNullable<T> nullable) {
        return nullable != null && nullable.isPresent() && nullable.get() != null;
    }
}

