package com.vpactually.dto.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateUpdateDTO {
    private JsonNullable<String> name;
    private JsonNullable<String> email;
    private JsonNullable<String> password;
}
