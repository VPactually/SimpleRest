package com.vpactually.entities;

import com.vpactually.dao.LabelDAO;
import com.vpactually.util.DependencyContainer;
import com.vpactually.util.FetchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Label implements BaseEntity{
    private Integer id;
    private String name;
    private LocalDate createdAt;
    private Set<Task> tasks = new HashSet<>();
    private transient FetchType fetchType = FetchType.LAZY;

    public Label(Integer id) {
        this.id = id;
    }

    public Label(Integer id, String name, LocalDate createdAt, Set<Task> tasks) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.tasks = tasks;
    }

    public Set<Task> getTasks() {
        if (fetchType.equals(FetchType.EAGER)) {
            tasks = DependencyContainer.getInstance().getDependency(LabelDAO.class).findTasksByLabelId(id);
        }
        return tasks;
    }

    public void addTask(Task task) {
        getTasks().add(task);
    }

    public void removeTask(Task task) {
        getTasks().remove(task);
    }
}
