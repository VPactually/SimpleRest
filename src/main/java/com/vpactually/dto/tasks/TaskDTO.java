package com.vpactually.dto.tasks;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskDTO {
    private Integer id;
    private String title;
    private String description;
    private String status;
    private Integer assigneeId;
    private String createdAt;
    private Set<Integer> taskLabelIds = new HashSet<>();
}
