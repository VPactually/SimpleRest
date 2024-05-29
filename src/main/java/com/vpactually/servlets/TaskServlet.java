package com.vpactually.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vpactually.dto.tasks.TaskCreateUpdateDTO;
import com.vpactually.services.TaskService;
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
        var createUpdateDTO = createTask(req);
        var task = taskService.save(createUpdateDTO);
        resp.getWriter().write(om.writeValueAsString(task));

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var pathVariable = req.getRequestURI().split("/");
        if (pathVariable.length == 3) {
            var id = Integer.parseInt(pathVariable[2]);
            taskService.deleteById(id);
        }
    }

    private TaskCreateUpdateDTO createTask(HttpServletRequest req) {
        var taskCreateUpdateDTO = new TaskCreateUpdateDTO();

        var title = req.getParameter("title");
        taskCreateUpdateDTO.setTitle(JsonNullable.of(title));

        var description = req.getParameter("description");
        taskCreateUpdateDTO.setDescription(JsonNullable.of(description));

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
