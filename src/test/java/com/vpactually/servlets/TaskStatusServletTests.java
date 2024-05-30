package com.vpactually.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vpactually.dto.taskStatuses.TaskStatusCreateUpdateDTO;
import com.vpactually.dto.taskStatuses.TaskStatusDTO;
import com.vpactually.services.TaskStatusService;
import com.vpactually.util.ContainerUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskStatusServletTests {

    @Mock
    private TaskStatusService service;

    @Mock
    private ObjectMapper om;

    @Mock
    private HttpServletRequest req;

    @Mock
    private HttpServletResponse resp;

    @Mock
    private PrintWriter writer;

    private String URI = "/task-statuses";

    @InjectMocks
    private TaskStatusServlet servlet;

    @BeforeAll
    public static void startContainer() throws SQLException {
        ContainerUtil.run();
    }

    @AfterAll
    public static void stopContainer() {
        ContainerUtil.stop();
    }

    @Test
    public void testDoGetAll() throws IOException {
        var taskStatusDTO = new TaskStatusDTO(1, "test", "test");

        when(service.findAll()).thenReturn(List.of(taskStatusDTO));
        when(resp.getWriter()).thenReturn(writer);
        when(req.getRequestURI()).thenReturn(URI);

        servlet.doGet(req, resp);

        verify(service).findAll();
        verify(writer).write(om.writeValueAsString(List.of(taskStatusDTO)));
    }

    @Test
    public void testDoGetById() throws IOException {
        var taskStatusDTO = new TaskStatusDTO(1, "test", "test");

        when(service.findById(anyInt())).thenReturn(Optional.of(taskStatusDTO));
        when(resp.getWriter()).thenReturn(writer);
        when(req.getRequestURI()).thenReturn(URI + "/1");

        servlet.doGet(req, resp);

        verify(service).findById(anyInt());
        verify(writer).write(om.writeValueAsString(taskStatusDTO));
    }

    @Test
    public void testDoPost() throws IOException, ServletException {
        var taskStatusDto = new TaskStatusDTO(2, "test", "test");

        when(service.save(any(TaskStatusCreateUpdateDTO.class))).thenReturn(taskStatusDto);
        when(resp.getWriter()).thenReturn(writer);
        when(req.getRequestURI()).thenReturn(URI);

        servlet.doPost(req, resp);

        verify(service).save(any(TaskStatusCreateUpdateDTO.class));
        verify(writer).write(om.writeValueAsString(taskStatusDto));
    }

    @Test
    public void testDoPut() throws IOException, ServletException {
        var userDto = new TaskStatusDTO(1, "test", "test");
        when(service.update(any(TaskStatusCreateUpdateDTO.class),anyInt())).thenReturn(userDto);
        when(resp.getWriter()).thenReturn(writer);
        when(req.getRequestURI()).thenReturn(URI + "/1");

        servlet.doPut(req, resp);

        verify(service).update(any(TaskStatusCreateUpdateDTO.class), anyInt());
        verify(resp).getWriter();
        verify(writer).write(om.writeValueAsString(userDto));
    }

    @Test
    public void testDoDelete() throws IOException, ServletException {
        when(req.getRequestURI()).thenReturn(URI + "/1");

        servlet.doDelete(req, resp);

        verify(service).deleteById(anyInt());
    }
}
