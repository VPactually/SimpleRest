package com.vpactually.dto.tasks;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskUpdateDTO {
    private JsonNullable<String> title;
    private JsonNullable<String> description;
    private JsonNullable<Integer> statusId;
    private JsonNullable<Integer> assigneeId;
    private JsonNullable<Set<Integer>> taskLabelIds;

    public TaskUpdateDTO(TaskCreateDTO taskCreateDTO) {
        this.title = taskCreateDTO.getTitle();
        this.description = taskCreateDTO.getDescription();
        this.statusId = taskCreateDTO.getStatusId();
        this.assigneeId = taskCreateDTO.getAssigneeId();
        this.taskLabelIds = taskCreateDTO.getTaskLabelIds();
    }
}
