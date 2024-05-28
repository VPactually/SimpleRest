package com.vpactually.dto.taskStatuses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusCreateUpdateDTO {
    private JsonNullable<String> name;
    private JsonNullable<String> slug;
}
