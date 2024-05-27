package com.vpactually.dao;

import com.vpactually.entities.Task;
import com.vpactually.util.ConnectionManager;
import com.vpactually.util.FetchType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class TaskDAO implements DAO<Integer, Task> {

    private static final TaskDAO INSTANCE = new TaskDAO();
    private static final TaskStatusDAO TASK_STATUS_DAO = TaskStatusDAO.getInstance();
    private static final UserDAO USER_DAO = UserDAO.getInstance();
    private static final LabelDAO LABEL_DAO = LabelDAO.getInstance();

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
    private static final String FIND_USER_TASKS_BY_USER_ID_SQL = """
            SELECT * FROM tasks WHERE user_id = ?
            """;
    private static final String SAVE_TASK_LABELS_SQL = """
            INSERT INTO task_labels (task_id, label_id) VALUES (?, ?)
            """;
    private static final String DELETE_TASK_LABELS_SQL = """
            DELETE FROM task_labels WHERE task_id = ?
            """;
    private static final String SAVE_USER_TASKS_SQL = """
            INSERT INTO user_tasks VALUES (?, ?)
            """;

    private static final String DELETE_USER_TASKS_SQL = """
            DELETE FROM user_tasks WHERE user_id = ?
            """;


    @Override
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                var task = buildTask(resultSet);
                task.setAssignee(USER_DAO.findById(resultSet.getInt("user_id")).orElseThrow());
                task.getAssignee().setFetchType(FetchType.LAZY);
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
                task.setAssignee(USER_DAO.findById(resultSet.getInt("user_id")).orElseThrow());
                task.getAssignee().setFetchType(FetchType.LAZY);
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
            task.setFetchType(FetchType.EAGER);
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
            task.setFetchType(FetchType.EAGER);
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

    public Task buildTask(ResultSet resultSet) {
        var task = new Task();
        try {
            task.setId(resultSet.getInt("id"));
            task.setTitle(resultSet.getString("title"));
            task.setDescription(resultSet.getString("description"));
            task.setCreatedAt(resultSet.getDate("created_at").toLocalDate());
            task.setTaskStatus(TASK_STATUS_DAO.findById(resultSet.getInt("status_id")).orElseThrow());
            task.setLabels(LABEL_DAO.findLabelsByTaskId(resultSet.getInt("id")));
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return task;
    }

    public static TaskDAO getInstance() {
        return INSTANCE;
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

    public Set<Task> findUserTasksByUserId(Integer id) {
        Set<Task> tasks = new HashSet<>();
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_USER_TASKS_BY_USER_ID_SQL)) {
            preparedStatement.setObject(1, id);
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

    public void saveUserTasks(Set<Task> tasks, Integer userId) {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(DELETE_USER_TASKS_SQL)) {
            preparedStatement.setObject(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.fillInStackTrace();
        }

        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(SAVE_USER_TASKS_SQL)) {
            for (var task : tasks) {
                preparedStatement.setObject(1, userId);
                preparedStatement.setObject(2, task.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }
}
