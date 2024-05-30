package com.vpactually.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vpactually.dto.tasks.TaskCreateDTO;
import com.vpactually.dto.tasks.TaskDTO;
import com.vpactually.dto.tasks.TaskUpdateDTO;
import com.vpactually.services.TaskService;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServletTests {

    @Mock
    private TaskService service;

    @Mock
    private ObjectMapper om;

    @Mock
    private HttpServletRequest req;

    @Mock
    private HttpServletResponse resp;

    @Mock
    private PrintWriter writer;

    private String URI = "/tasks";

    @InjectMocks
    private TaskServlet servlet;

    @BeforeAll
    public static void startContainer() throws SQLException {
        ContainerUtil.run();
    }

    @AfterAll
    public static void stopContainer() {
        ContainerUtil.stop();
    }

    @Test
    public void testDoGet() throws IOException, ServletException {
        var taskDTO = new TaskDTO(1, "test", "test", "test", 1,
                LocalDate.now().toString(), Set.of(1));

        when(service.findAll()).thenReturn(List.of(taskDTO));
        when(resp.getWriter()).thenReturn(writer);
        when(req.getRequestURI()).thenReturn(URI);

        servlet.doGet(req, resp);

        verify(service).findAll();
        verify(writer).write(om.writeValueAsString(List.of(taskDTO)));
    }

    @Test
    public void testDoGetById() throws IOException, ServletException {
        var taskDTO = new TaskDTO(1, "test", "test", "test", 1,
                LocalDate.now().toString(), Set.of(1));

        when(service.findById(anyInt())).thenReturn(Optional.of(taskDTO));
        when(resp.getWriter()).thenReturn(writer);
        when(req.getRequestURI()).thenReturn(URI + "/1");

        servlet.doGet(req, resp);

        verify(service).findById(anyInt());
        verify(writer).write(om.writeValueAsString(taskDTO));
    }

    @Test
    public void testDoPost() throws IOException, ServletException {
        var taskDTO = new TaskDTO(1, "test", "test", "test", 1,
                LocalDate.now().toString(), Set.of(1));

        when(service.save(any(TaskCreateDTO.class))).thenReturn(taskDTO);
        when(resp.getWriter()).thenReturn(writer);
        when(req.getRequestURI()).thenReturn(URI);

        servlet.doPost(req, resp);

        verify(service).save(any(TaskCreateDTO.class));
        verify(writer).write(om.writeValueAsString(taskDTO));
    }

    @Test
    public void testDoPut() throws IOException, ServletException {
        var taskDTO = new TaskDTO(1, "test", "test", "test", 1,
                LocalDate.now().toString(), Set.of(1));
        when(service.update(any(TaskUpdateDTO.class), anyInt())).thenReturn(taskDTO);
        when(resp.getWriter()).thenReturn(writer);
        when(req.getRequestURI()).thenReturn(URI + "/1");

        servlet.doPut(req, resp);

        verify(service).update(any(TaskUpdateDTO.class), anyInt());
        verify(resp).getWriter();
        verify(writer).write(om.writeValueAsString(taskDTO));
    }

    @Test
    public void testDoDelete() throws IOException, ServletException {
        when(req.getRequestURI()).thenReturn(URI + "/1");

        servlet.doDelete(req, resp);

        verify(service).deleteById(anyInt());
    }
}
