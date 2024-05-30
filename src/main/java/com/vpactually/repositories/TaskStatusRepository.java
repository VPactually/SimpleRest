package com.vpactually.repositories;

import com.vpactually.entities.TaskStatus;
import com.vpactually.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskStatusRepository implements Repository<Integer, TaskStatus> {
    private static final String FIND_ALL_SQL = "SELECT * FROM task_statuses";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM task_statuses WHERE id = ?";
    private static final String SAVE_SQL = "INSERT INTO task_statuses (name, slug, created_at) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE task_statuses SET name = ?, slug = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM task_statuses WHERE id = ?";

    @Override
    public List<TaskStatus> findAll() {
        List<TaskStatus> taskStatuses = new ArrayList<>();
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                var taskStatus = buildTaskStatus(resultSet);
                taskStatuses.add(taskStatus);
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return taskStatuses;
    }

    @Override
    public Optional<TaskStatus> findById(Integer id) {
        TaskStatus taskStatus = null;
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setObject(1, id);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                taskStatus = buildTaskStatus(resultSet);
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return Optional.ofNullable(taskStatus);
    }

    @Override
    public TaskStatus save(TaskStatus entity) {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(SAVE_SQL,
                Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, entity.getName());
            preparedStatement.setObject(2, entity.getSlug());
            preparedStatement.setObject(3, LocalDate.now());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return entity;
    }

    @Override
    public TaskStatus update(TaskStatus entity) {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(UPDATE_SQL)) {
            preparedStatement.setObject(1, entity.getName());
            preparedStatement.setObject(2, entity.getSlug());
            preparedStatement.setObject(3, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return entity;
    }

    @Override
    public boolean deleteById(Integer id) {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(DELETE_SQL)) {
            preparedStatement.setObject(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return true;
    }

    public static TaskStatus buildTaskStatus(ResultSet resultSet) throws SQLException {
        var taskStatus = new TaskStatus();
        taskStatus.setId(resultSet.getInt("id"));
        taskStatus.setName(resultSet.getString("name"));
        taskStatus.setSlug(resultSet.getString("slug"));
        taskStatus.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime().toLocalDate());
        return taskStatus;
    }

    public static TaskStatus buildTaskStatusByJoinTables(ResultSet resultSet) throws SQLException {
        var taskStatus = new TaskStatus();
        taskStatus.setId(resultSet.getInt(7));
        taskStatus.setName(resultSet.getString(8));
        taskStatus.setSlug(resultSet.getString(9));
        taskStatus.setCreatedAt(resultSet.getTimestamp(10).toLocalDateTime().toLocalDate());
        return taskStatus;
    }
}
