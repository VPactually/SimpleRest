package com.vpactually.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private static final String FIND_TASKS_BY_STATUS_ID_SQL = "SELECT * FROM tasks WHERE status_id = ?";

    public TaskStatus(Integer id, String name, String slug, LocalDate createdAt) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.createdAt = createdAt;
    }

    public TaskStatus(Integer id, Set<Task> tasks) {
        this.id = id;
        this.tasks = tasks;
    }

    public TaskStatus(Integer id, String slug) {
        this.slug = slug;
        this.id = id;
    }

    public TaskStatus(Integer id) {
        this.id = id;
    }

    public TaskStatus fetchTasks() {
        tasks = fetchTasksBase(FIND_TASKS_BY_STATUS_ID_SQL, tasks, id);
        return this;
    }

}
