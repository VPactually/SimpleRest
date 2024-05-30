package com.vpactually.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
public class Label implements BaseEntity {
    private Integer id;
    private String name;
    private LocalDate createdAt;
    private Set<Task> tasks;

    private static final String FIND_TASKS_BY_LABEL_ID_SQL = """
            SELECT * FROM task_labels INNER JOIN  tasks ON task_labels.task_id = tasks.id WHERE label_id = ?
            """;

    public Label(Integer id) {
        this.id = id;
    }

    public Label(Integer id, Set<Task> tasks) {
        this.id = id;
        this.tasks = tasks;
    }

    public Label(Integer id, String name, LocalDate createdAt, Set<Task> tasks) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.tasks = tasks;
    }

    public Label fetchTasks() {
        tasks = fetchTasksBase(FIND_TASKS_BY_LABEL_ID_SQL, tasks, id);
        return this;
    }

}
