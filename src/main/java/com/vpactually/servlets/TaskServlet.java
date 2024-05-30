package com.vpactually.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vpactually.dto.tasks.TaskCreateDTO;
import com.vpactually.dto.tasks.TaskUpdateDTO;
import com.vpactually.services.TaskService;
import com.vpactually.util.Generated;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.openapitools.jackson.nullable.JsonNullable;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@WebServlet("/tasks/*")
public class TaskServlet extends HttpServlet {

    private TaskService taskService;
    private ObjectMapper om;

    @Generated
    @Override
    public void init() {
        taskService = (TaskService) getServletContext().getAttribute("taskService");
        om = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var pathVariable = req.getRequestURI().split("/");
        if (pathVariable.length == 3) {
            resp.getWriter().write(om.writeValueAsString(taskService.findById(Integer.parseInt(pathVariable[2]))
                    .orElseThrow()));
            return;
        }
        var tasks = taskService.findAll();
        resp.getWriter().write(om.writeValueAsString(tasks));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getRequestURI().equals("/tasks")) {
            var createUpdateDTO = createTask(req);
            var task = taskService.save(createUpdateDTO);
            resp.getWriter().write(om.writeValueAsString(task));
        } else {
            doPut(req, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var pathVariable = req.getRequestURI().split("/");
        if (pathVariable.length == 3) {
            var id = Integer.parseInt(pathVariable[2]);
            var updateDTO = new TaskUpdateDTO(createTask(req));
            var task = taskService.update(updateDTO, id);
            resp.getWriter().write(om.writeValueAsString(task));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var pathVariable = req.getRequestURI().split("/");
        if (pathVariable.length == 3) {
            var id = Integer.parseInt(pathVariable[2]);
            taskService.deleteById(id);
        }
    }

    @Generated
    private TaskCreateDTO createTask(HttpServletRequest req) {
        var taskCreateUpdateDTO = new TaskCreateDTO();
        taskCreateUpdateDTO.setTitle(JsonNullable.of(req.getParameter("title")));
        taskCreateUpdateDTO.setDescription(JsonNullable.of(req.getParameter("description")));
        var statusId = req.getParameter("statusId");
        taskCreateUpdateDTO.setStatusId(JsonNullable.of(statusId == null ? null : Integer.parseInt(statusId)));
        var assigneeId = req.getParameter("assigneeId");
        taskCreateUpdateDTO.setAssigneeId(JsonNullable.of(assigneeId == null ? null : Integer.parseInt(assigneeId)));
        var labels = req.getParameterValues("taskLabelIds");
        taskCreateUpdateDTO.setTaskLabelIds(labels == null ? null :
                JsonNullable.of(Arrays.stream(labels).map(Integer::parseInt).collect(Collectors.toSet())));
        return taskCreateUpdateDTO;
    }
}
