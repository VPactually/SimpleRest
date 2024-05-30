package com.vpactually.dao;

import com.vpactually.entities.Label;
import com.vpactually.entities.Task;
import com.vpactually.entities.TaskStatus;
import com.vpactually.entities.User;
import com.vpactually.util.ConnectionManager;
import com.vpactually.util.FetchType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.vpactually.dao.TaskStatusDAO.buildTaskStatus;

public class TaskDAO implements DAO<Integer, Task> {

    //language=PostgreSQL
    private static final String FIND_ALL_SQL = """
            SELECT t.*, ts.*, u.* FROM tasks AS t
            INNER JOIN task_statuses AS ts ON t.status_id = ts.id
            INNER JOIN users AS u ON t.user_id = u.id
            """;
    //language=PostgreSQL
    private static final String FIND_BY_ID_SQL = """
            SELECT t.*, ts.*, u.*
            FROM tasks AS t
            INNER JOIN task_statuses AS ts ON t.status_id = ts.id
            INNER JOIN users AS u ON t.user_id = u.id
            INNER JOIN task_labels AS tl ON t.id = tl.task_id
            WHERE t.id = ?
            """;
    private static final String SAVE_SQL =
            "INSERT INTO tasks (title, description, status_id, user_id, created_at) VALUES (?, ?, ?, ?, ?)";
    //language=PostgreSQL
    private static final String UPDATE_SQL =
            "UPDATE tasks SET title = ?, description = ?, status_id = ?, user_id = ? WHERE id = ?";
    //language=PostgreSQL
    private static final String DELETE_SQL = """
            DELETE FROM user_tasks WHERE task_id = ?;
            DELETE FROM task_labels WHERE task_id =?;
            DELETE FROM task_task_statuses WHERE task_id =?;
            DELETE FROM tasks WHERE id =?
            """;
    //language=PostgreSQL
    private static final String SAVE_TASK_LABELS_SQL = """
            INSERT INTO task_labels(task_id, label_id) VALUES (?,?)""";
    //language=PostgreSQL
    private static final String DELETE_TASK_LABELS_SQL = """
            DELETE FROM task_labels WHERE task_id = ?
            """;
    //language=PostgreSQL
    private static final String FIND_STATUS_BY_ID_SQL = "SELECT * FROM task_statuses WHERE id = ?";
    private static final String FIND_LABELS_BY_TASK_ID_SQL = """
            SELECT * FROM task_labels INNER JOIN labels l ON l.id = task_labels.label_id WHERE task_id = ?
            """;

    private static final String DELETE_TASK_STATUS_SQL = """
            DELETE FROM task_task_statuses WHERE task_id = ?
            """;

    private static final String SAVE_TASK_STATUS_SQL = """
            INSERT INTO task_task_statuses(task_id, task_status_id) VALUES (?,?)""";


    public List<Task> findAll(FetchType fetchType) {
        List<Task> tasks = new ArrayList<>();
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                var task = buildTask(resultSet);

                    task.setTaskStatus(new TaskStatus(resultSet.getInt(7), resultSet.getString(9)));
                    task.setAssignee(new User(resultSet.getInt(5)));
                    task.setLabels(findLabelsByTaskId(task.getId()).stream()
                            .map(Label::getId)
                            .map(Label::new)
                            .collect(Collectors.toSet()));

                tasks.add(task);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return tasks;
    }

    @Override
    public List<Task> findAll() {
        return findAll(FetchType.LAZY);
    }

    public Optional<Task> findById(Integer id, FetchType fetchType) {
        Task task = null;
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setObject(1, id);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                task = buildTask(resultSet);

                task.setTaskStatus(new TaskStatus(resultSet.getInt(7), resultSet.getString(9)));
                task.setAssignee(new User(resultSet.getInt(5)));
                task.setLabels(findLabelsByTaskId(id).stream()
                        .map(Label::getId)
                        .map(Label::new)
                        .collect(Collectors.toSet()));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return Optional.ofNullable(task);
    }

    @Override
    public Optional<Task> findById(Integer id) {
        return findById(id, FetchType.LAZY);
    }

    @Override
    public Task save(Task task) {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(SAVE_SQL,
                Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, task.getTitle());
            preparedStatement.setObject(2, task.getDescription());
            preparedStatement.setObject(3, task.getTaskStatus().getId());
            preparedStatement.setObject(4, task.getAssignee().getId());
            preparedStatement.setObject(5, LocalDate.now());
            preparedStatement.executeUpdate();
            var resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                task.setId(resultSet.getInt(1));
            }
            saveTaskLabels(task.getLabels(), task.getId());
            task.setCreatedAt(LocalDate.now());
            task.setTaskStatus(findTaskStatusByStatusId((task.getTaskStatus()).getId()).orElseThrow());
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return task;
    }

    @Override
    public Task update(Task task) {
        var labels = task.getLabels();
        var taskStatus = task.getTaskStatus();

        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(UPDATE_SQL)) {
            preparedStatement.setObject(1, task.getTitle());
            preparedStatement.setObject(2, task.getDescription());
            preparedStatement.setObject(3, taskStatus.getId());
            preparedStatement.setObject(4, task.getAssignee().getId());
            preparedStatement.setObject(5, task.getId());
            preparedStatement.executeUpdate();
            updateLabels(labels, task.getId());
            updateTaskStatus(taskStatus, task.getId());
            task.setTaskStatus(findTaskStatusByStatusId(taskStatus.getId()).orElseThrow());

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return task;
    }

    @Override
    public boolean deleteById(Integer id) {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(DELETE_SQL)) {
            preparedStatement.setObject(1, id);
            preparedStatement.setObject(2, id);
            preparedStatement.setObject(3, id);
            preparedStatement.setObject(4, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
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
            throw new RuntimeException(e.getMessage());
        }
        return task;
    }

    private void updateTaskStatus(TaskStatus taskStatus, Integer id) {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(DELETE_TASK_STATUS_SQL)) {
            preparedStatement.setObject(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(SAVE_TASK_STATUS_SQL)) {
            preparedStatement.setObject(1, id);
            preparedStatement.setObject(2, taskStatus.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<TaskStatus> findTaskStatusByStatusId(Integer id) {
        TaskStatus taskStatus = null;
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_STATUS_BY_ID_SQL)) {
            preparedStatement.setObject(1, id);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                taskStatus = buildTaskStatus(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return Optional.ofNullable(taskStatus);
    }

    public void saveTaskLabels(Set<Label> taskLabels, Integer taskId) {

        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(SAVE_TASK_LABELS_SQL)) {
            for (var label : taskLabels) {
                preparedStatement.setObject(1, taskId);
                preparedStatement.setObject(2, label.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void updateLabels(Set<Label> labels, Integer id) {
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(DELETE_TASK_LABELS_SQL)) {
            preparedStatement.setObject(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        saveTaskLabels(labels, id);
    }

    private Set<Label> findLabelsByTaskId(Integer id) {
        Set<Label> labels = new HashSet<>();
        try (var preparedStatement = ConnectionManager.getInstance().prepareStatement(FIND_LABELS_BY_TASK_ID_SQL)) {
            preparedStatement.setObject(1, id);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                var task = LabelDAO.buildLabel(resultSet);
                labels.add(task);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return labels;
    }


}
