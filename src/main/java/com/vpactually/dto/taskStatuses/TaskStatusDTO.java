package com.vpactually.dto.taskStatuses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskStatusDTO {
    private Integer id;
    private String name;
    private String slug;
}
