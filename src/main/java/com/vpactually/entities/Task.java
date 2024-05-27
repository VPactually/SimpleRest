package com.vpactually.entities;

import com.vpactually.dao.LabelDAO;
import com.vpactually.dao.UserDAO;
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
            labels = LabelDAO.getInstance().findLabelsByTaskId(id);
        }
        return labels;
    }

    public User getAssignee() {
        if (fetchType.equals(FetchType.EAGER)) {
            assignee = UserDAO.getInstance().findById(assignee.getId()).get();
        }
        return assignee;
    }

    public void addLabel(Label label) {
        getLabels().add(label);
    }

    public void removeLabel(Label label) {
        getLabels().remove(label);
    }
}
