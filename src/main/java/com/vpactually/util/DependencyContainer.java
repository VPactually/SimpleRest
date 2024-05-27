package com.vpactually.util;

import com.vpactually.dao.LabelDAO;
import com.vpactually.dao.TaskDAO;
import com.vpactually.dao.TaskStatusDAO;
import com.vpactually.dao.UserDAO;
import com.vpactually.mappers.LabelMapper;
import com.vpactually.mappers.TaskMapper;
import com.vpactually.mappers.TaskStatusMapper;
import com.vpactually.mappers.UserMapper;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.Map;

public class DependencyContainer {
    private static DependencyContainer instance;
    private Map<Class<?>, Object> dependencies = new HashMap<>();

    private DependencyContainer() {}

    public static DependencyContainer getInstance() {
        if (instance == null) {
            instance = new DependencyContainer();
            instance.registerDependency(UserDAO.class, new UserDAO());
            instance.registerDependency(LabelDAO.class, new LabelDAO());
            instance.registerDependency(TaskDAO.class, new TaskDAO());
            instance.registerDependency(TaskStatusDAO.class, new TaskStatusDAO());

            instance.registerDependency(UserMapper.class, Mappers.getMapperClass(UserMapper.class));
            instance.registerDependency(LabelMapper.class, Mappers.getMapperClass(LabelMapper.class));
            instance.registerDependency(TaskStatusMapper.class, Mappers.getMapperClass(TaskStatusMapper.class));
            instance.registerDependency(TaskMapper.class, Mappers.getMapperClass(TaskMapper.class));
        }
        return instance;
    }

    public void registerDependency(Class<?> type, Object instance) {
        dependencies.put(type, instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T getDependency(Class<T> type) {
        return (T) dependencies.get(type);
    }
}
