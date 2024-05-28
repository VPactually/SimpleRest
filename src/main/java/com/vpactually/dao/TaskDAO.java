package com.vpactually.dao;

import com.vpactually.entities.Task;
import com.vpactually.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskDAO implements DAO<Integer, Task> {

    private static final String FIND_ALL_SQL = "SELECT * FROM tasks";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM tasks WHERE id = ?";
    private static final String SAVE_SQL =
            "INSERT INTO tasks (title, description, status_id, user_id, created_at) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL =
            "UPDATE tasks SET title = ?, description = ?, status_id = ?, user_id = ? WHERE id = ?";
    private static final String DELETE_SQL = """
            DELETE FROM task_labels WHERE task_id = ?;
            DELETE FROM tasks WHERE id = ?
            """;

    private static final String SAVE_TASK_LABELS_SQL = """
            INSERT INTO task_labels (task_id, label_id) VALUES (?, ?)""";


    @Override
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                var task = buildTask(resultSet);
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return tasks;
    }

    @Override
    public Optional<Task> findById(Integer id) {
        Task task = null;
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setObject(1, id);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                task = buildTask(resultSet);
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return Optional.ofNullable(task);
    }

    @Override
    public Task save(Task task) {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, task.getTitle());
            preparedStatement.setObject(2, task.getDescription());
            preparedStatement.setObject(3, task.getTaskStatus().getId());
            preparedStatement.setObject(4, task.getAssignee().getId());
            preparedStatement.setObject(5, task.getCreatedAt());
            preparedStatement.executeUpdate();
            var resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                task.setId(resultSet.getInt(1));
            }
            saveTaskLabels(task);
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return task;
    }

    @Override
    public Task update(Task task) {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(UPDATE_SQL)) {
            preparedStatement.setObject(1, task.getTitle());
            preparedStatement.setObject(2, task.getDescription());
            preparedStatement.setObject(3, task.getTaskStatus().getId());
            preparedStatement.setObject(4, task.getAssignee().getId());
            preparedStatement.setObject(5, task.getId());
            preparedStatement.executeUpdate();
            saveTaskLabels(task);
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return task;
    }

    @Override
    public boolean deleteById(Integer id) {
        if (!findById(id).get().getLabels().isEmpty()) {
            return false;
        }
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(DELETE_SQL)) {
            preparedStatement.setObject(1, id);
            preparedStatement.setObject(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return true;
    }

    public static Task buildTask(ResultSet resultSet) {
        var task = new Task();
        try {
            task.setId(resultSet.getInt("id"));
            task.setTitle(resultSet.getString("title"));
            task.setDescription(resultSet.getString("description"));
            task.setCreatedAt(resultSet.getDate("created_at").toLocalDate());
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return task;
    }

    public void saveTaskLabels(Task task) {
        var taskLabels = task.getLabels();
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(SAVE_TASK_LABELS_SQL)) {
            for (var label : taskLabels) {
                preparedStatement.setObject(1, task.getId());
                preparedStatement.setObject(2, label.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }


}
