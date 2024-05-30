package com.vpactually.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vpactually.dto.taskStatuses.TaskStatusCreateUpdateDTO;
import com.vpactually.services.TaskStatusService;
import com.vpactually.util.Generated;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.openapitools.jackson.nullable.JsonNullable;

import java.io.IOException;

@WebServlet("/task-statuses/*")
public class TaskStatusServlet extends HttpServlet {
    private TaskStatusService taskStatusService;
    private ObjectMapper om;

    @Generated
    @Override
    public void init() throws ServletException {
        super.init();
        taskStatusService = (TaskStatusService) getServletContext().getAttribute("taskStatusService");
        om = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var pathVariable = req.getRequestURI().split("/");
        if (pathVariable.length == 3) {
            resp.getWriter().write(om.writeValueAsString(taskStatusService.findById(Integer.parseInt(pathVariable[2]))
                    .orElseThrow()));
            return;
        }
        var taskStatuses = taskStatusService.findAll();
        resp.getWriter().write(om.writeValueAsString(taskStatuses));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getRequestURI().equals("/task-statuses")) {
            var createUpdateDTO = createTaskStatus(req);
            var taskStatus = taskStatusService.save(createUpdateDTO);
            resp.getWriter().write(om.writeValueAsString(taskStatus));
        } else {
            doPut(req, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var pathVariable = req.getRequestURI().split("/");
        if (pathVariable.length == 3) {
            var id = Integer.parseInt(pathVariable[2]);
            var createUpdateDTO = createTaskStatus(req);
            var taskStatus = taskStatusService.update(createUpdateDTO, id);
            resp.getWriter().write(om.writeValueAsString(taskStatus));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var pathVariable = req.getRequestURI().split("/");
        if (pathVariable.length == 3) {
            var id = Integer.parseInt(pathVariable[2]);
            taskStatusService.deleteById(id);
        }
    }

    @Generated
    private TaskStatusCreateUpdateDTO createTaskStatus(HttpServletRequest req) {
        var createUpdateDTO = new TaskStatusCreateUpdateDTO();
        createUpdateDTO.setName(JsonNullable.of(req.getParameter("name")));
        createUpdateDTO.setSlug(JsonNullable.of(req.getParameter("slug")));
        return createUpdateDTO;
    }
}
