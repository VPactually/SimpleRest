package com.vpactually.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vpactually.repositories.LabelRepository;
import com.vpactually.repositories.TaskRepository;
import com.vpactually.repositories.TaskStatusRepository;
import com.vpactually.repositories.UserRepository;
import com.vpactually.mappers.LabelMapper;
import com.vpactually.mappers.TaskMapper;
import com.vpactually.mappers.TaskStatusMapper;
import com.vpactually.mappers.UserMapper;
import com.vpactually.services.LabelService;
import com.vpactually.services.TaskService;
import com.vpactually.services.TaskStatusService;
import com.vpactually.services.UserService;
import jakarta.servlet.ServletContext;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.Map;

public class DependencyContainer {

    private static final Map<String, Object> BEAN_MAP = new HashMap<String, Object>();


    public static void injectDependencies(ServletContext context) {

        var om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        var userDAO = new UserRepository();
        var taskDAO = new TaskRepository();
        var taskStatusDAO = new TaskStatusRepository();
        var labelDAO = new LabelRepository();

        var userMapper = Mappers.getMapper(UserMapper.class);
        var taskMapper = Mappers.getMapper(TaskMapper.class);
        var taskStatusMapper = Mappers.getMapper(TaskStatusMapper.class);
        var labelMapper = Mappers.getMapper(LabelMapper.class);

        var userService = new UserService(userDAO, userMapper);
        var taskService = new TaskService(taskDAO, taskMapper);
        var taskStatusService = new TaskStatusService(taskStatusDAO, taskStatusMapper);
        var labelService = new LabelService(labelDAO, labelMapper);

        BEAN_MAP.put("userService", userService);
        BEAN_MAP.put("taskService", taskService);
        BEAN_MAP.put("taskStatusService", taskStatusService);
        BEAN_MAP.put("labelService", labelService);

        context.setAttribute("objectMapper", om);
        context.setAttribute("userService", userService);
        context.setAttribute("taskService", taskService);
        context.setAttribute("taskStatusService", taskStatusService);
        context.setAttribute("labelService", labelService);
    }

    public static Object getDependency(String string) {
        return BEAN_MAP.get(string);
    }

    public static void registerDependency(String string, Object object) {
        BEAN_MAP.put(string, object);
    }

}

