package com.vpactually.mappers;

import com.vpactually.dto.users.UserCreateUpdateDTO;
import com.vpactually.dto.users.UserDTO;
import com.vpactually.entities.User;
import org.mapstruct.*;

@Mapper(
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {

    public UserMapper() {}

    public abstract User map(UserCreateUpdateDTO dto);

    public abstract UserDTO map(User model);

    public abstract void update(UserCreateUpdateDTO dto, @MappingTarget User model);

}