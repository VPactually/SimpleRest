package com.vpactually.dto.tasks;

import lombok.*;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskCreateDTO {
    private JsonNullable<String> title;
    private JsonNullable<String> description;
    private JsonNullable<Integer> statusId;
    private JsonNullable<Integer> assigneeId;
    private JsonNullable<Set<Integer>> taskLabelIds;
}
