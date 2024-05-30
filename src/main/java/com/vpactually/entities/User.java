package com.vpactually.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
public class User implements BaseEntity {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private LocalDate createdAt;
    private Set<Task> tasks;

    private static final String FIND_USER_TASKS_BY_USER_ID_SQL = "SELECT * FROM tasks WHERE user_id = ?";

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, Set<Task> tasks) {
        this.id = id;
        this.tasks = tasks;
    }

    public User(Integer id, String name, String email, String password, LocalDate createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }

    public User fetchTasks() {
        tasks = fetchTasksBase(FIND_USER_TASKS_BY_USER_ID_SQL, tasks, id);
        return this;
    }

}
