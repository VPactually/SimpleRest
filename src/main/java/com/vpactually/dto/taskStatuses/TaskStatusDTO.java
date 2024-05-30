package com.vpactually.dto.taskStatuses;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

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
