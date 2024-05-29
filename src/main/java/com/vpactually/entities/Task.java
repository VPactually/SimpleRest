package com.vpactually.entities;

import com.vpactually.dao.LabelDAO;
import com.vpactually.dao.TaskStatusDAO;
import com.vpactually.dao.UserDAO;
import com.vpactually.util.ConnectionManager;
import lombok.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Task implements BaseEntity {
    private Integer id;
    private String title;
    private String description;
    private LocalDate createdAt;
    private TaskStatus taskStatus;
    private User assignee;
    private Set<Label> labels = new HashSet<>();

    private static final String FIND_LABELS_BY_TASK_ID_SQL = """
            SELECT * FROM task_labels INNER JOIN labels l ON l.id = task_labels.label_id WHERE task_id = ?
            """;
    private static final String FIND_USER_BY_TASK_ID_SQL = """
            SELECT * FROM user_tasks INNER JOIN users u on u.id = user_tasks.user_id WHERE task_id = ?
            """;
    private static final String FIND_TASK_STATUS_BY_TASK_ID_SQL = """
            SELECT * FROM task_task_statuses tts
            INNER JOIN task_statuses ts on ts.id = tts.task_status_id
            WHERE task_id = ?
            """;

    public Task(Integer id, String title, String description, LocalDate createdAt, TaskStatus taskStatus, User assignee, Set<Label> labels) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.taskStatus = taskStatus;
        this.assignee = assignee;
        this.labels = labels;
    }

    public Task fetchLabels() {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_LABELS_BY_TASK_ID_SQL)) {
            preparedStatement.setObject(1, id);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                var task = LabelDAO.buildLabel(resultSet);
                labels.add(task);
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return this;
    }

    public void fetchAssignee() {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_USER_BY_TASK_ID_SQL)) {
            preparedStatement.setObject(1, id);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                assignee = UserDAO.buildUser(resultSet);
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }

    public void fetchTaskStatus() {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_TASK_STATUS_BY_TASK_ID_SQL)) {
            preparedStatement.setObject(1, id);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                taskStatus = TaskStatusDAO.buildTaskStatus(resultSet);
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }

    public Set<Label> getLabels() {
        if (labels.isEmpty()) {
            fetchLabels();
        }
        return labels;
    }

    public User getAssignee() {
        if (assignee == null) {
            assignee = new User();
        } else  {
            fetchAssignee();
        }
        return assignee;
    }

    public TaskStatus getTaskStatus() {
        if (taskStatus == null) {
            taskStatus = new TaskStatus();
        } else  {
            fetchTaskStatus();
        }
        return taskStatus;
    }


    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", taskStatus=" + taskStatus +
                ", assignee=" + assignee +
                ", labels=" + labels +
                '}';
    }
}
