package com.vpactually.dto.users;

import lombok.Data;
import org.openapitools.jackson.nullable.JsonNullable;

@Data
public class UserCreateUpdateDTO {
    private JsonNullable<String> name;
    private JsonNullable<String> email;
    private JsonNullable<String> password;
}
