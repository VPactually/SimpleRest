package com.vpactually.entities;

import com.vpactually.dao.LabelDAO;
import com.vpactually.dao.UserDAO;
import com.vpactually.util.DependencyContainer;
import com.vpactually.util.FetchType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Task implements BaseEntity {
    private Integer id;
    private String title;
    private String description;
    private LocalDate createdAt;
    private TaskStatus taskStatus;
    private User assignee;
    private Set<Label> labels = new HashSet<>();
    private transient FetchType fetchType = FetchType.LAZY;

    public Task(Integer id, String title, String description, LocalDate createdAt, TaskStatus taskStatus, User assignee, Set<Label> labels) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.taskStatus = taskStatus;
        this.assignee = assignee;
        this.labels = labels;
    }

    public Set<Label> getLabels() {
        if (fetchType.equals(FetchType.EAGER)) {
            labels = DependencyContainer.getInstance().getDependency(LabelDAO.class).findLabelsByTaskId(id);
        }
        return labels;
    }

    public User getAssignee() {
        if (fetchType.equals(FetchType.EAGER)) {
            assignee = DependencyContainer.getInstance().getDependency(UserDAO.class)
                    .findById(assignee.getId()).get();
        }
        return assignee;
    }
}
