package com.vpactually.entities;

import com.vpactually.dao.TaskDAO;
import com.vpactually.util.ConnectionManager;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Label implements BaseEntity{
    private Integer id;
    private String name;
    private LocalDate createdAt;
    private Set<Task> tasks = new HashSet<>();
    private static final String FIND_TASKS_BY_LABEL_ID_SQL = """
            SELECT * FROM task_labels INNER JOIN  tasks ON task_labels.task_id = tasks.id WHERE label_id = ?
            """;

    public Label(Integer id) {
        this.id = id;
    }

    public Label(Integer id, String name, LocalDate createdAt, Set<Task> tasks) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.tasks = tasks;
    }


    public void fetchTasks() {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_TASKS_BY_LABEL_ID_SQL)) {
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
