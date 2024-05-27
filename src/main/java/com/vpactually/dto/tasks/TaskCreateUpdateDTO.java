package com.vpactually.dto.tasks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateUpdateDTO {

    private JsonNullable<String> title;
    private JsonNullable<String> description;
    private JsonNullable<String> status;
    private JsonNullable<Integer> assigneeId;
    private JsonNullable<Set<Integer>> taskLabelIds;
}
