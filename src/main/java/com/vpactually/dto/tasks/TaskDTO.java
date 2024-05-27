package com.vpactually.dto.tasks;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class TaskDTO {
    private Integer id;
    private String title;
    private String description;
    private String status;
    private Integer assigneeId;
    private String createdAt;
    private Set<Integer> taskLabelIds = new HashSet<>();
}
