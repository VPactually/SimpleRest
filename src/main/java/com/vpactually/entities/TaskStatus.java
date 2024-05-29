package com.vpactually.entities;

import com.vpactually.dao.TaskDAO;
import com.vpactually.util.ConnectionManager;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.SQLException;
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
    private Set<Task> tasks = new HashSet<>();
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

    public void fetchTasks() {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_TASKS_BY_STATUS_ID_SQL)) {
            preparedStatement.setObject(1, id);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                var task = TaskDAO.buildTask(resultSet);
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }

    public Set<Task> getTasks() {
        if (tasks.isEmpty()) {
            fetchTasks();
        }
        return tasks;
    }
}
