package com.vpactually.entities;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class TaskStatus implements BaseEntity {
    private Integer id;
    private String name;
    private String slug;
    private LocalDate createdAt;
    private Set<Task> tasks;

    public TaskStatus(Integer id, String name, String slug, LocalDate createdAt) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.createdAt = createdAt;
    }

    public Set<Task> getTasks() {
        if (tasks == null) {
            tasks = new HashSet<>();
        }
        return tasks;
    }
}
