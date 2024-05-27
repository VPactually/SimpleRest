package com.vpactually.dto.taskStatuses;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class TaskStatusCreateUpdateDTO {
    private JsonNullable<String> name;
    private JsonNullable<String> slug;
}
