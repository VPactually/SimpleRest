package com.vpactually.util;

import com.vpactually.entities.Label;
import com.vpactually.entities.Task;
import com.vpactually.entities.TaskStatus;
import com.vpactually.entities.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class DataUtil {

    public static final User ADMIN = new User(1, "admin", "admin@gmail.com",
            "admin", LocalDate.now());
    public static final User ANOTHER_USER = new User(null, "user", "user@gmail.com",
            "user", LocalDate.now());
    public static final TaskStatus EXISTING_STATUS_1 = new TaskStatus(1, "Draft", "draft",
            LocalDate.now());
    public static final TaskStatus EXISTING_STATUS_2 = new TaskStatus(4, "ToBeFixed", "to_be_fixed",
            LocalDate.now());
    public static final TaskStatus ANOTHER_STATUS = new TaskStatus(null, "NewStatus", "new_status",
            LocalDate.now());
    public static final Label EXISTING_LABEL_1 = new Label(1, "Bug", LocalDate.now(), new HashSet<>());
    public static final Label EXISTING_LABEL_2 = new Label(2, "Feature", LocalDate.now(), new HashSet<>());
    public static final Label ANOTHER_LABEL = new Label(null, "NewLabel", LocalDate.now(), new HashSet<>());
    public static final Task EXISTING_TASK = new Task(1, "Task 1", "Description 1", LocalDate.now(),
            new TaskStatus(5, "Published", "published", LocalDate.now()),
            ADMIN,
            Set.of(EXISTING_LABEL_1, EXISTING_LABEL_2));
    public static final Task ANOTHER_TASK = new Task(null, "New Task", "New Description", LocalDate.now(),
            new TaskStatus(1, "Draft", "draft", LocalDate.now()),
            ADMIN,
            new HashSet<>());

}
