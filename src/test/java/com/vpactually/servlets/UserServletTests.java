package com.vpactually.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vpactually.dto.users.UserCreateUpdateDTO;
import com.vpactually.dto.users.UserDTO;
import com.vpactually.services.UserService;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServletTests {

    @Mock
    private UserService userService;

    @Mock
    private ObjectMapper om;

    @Mock
    private HttpServletRequest req;

    @Mock
    private HttpServletResponse resp;

    @Mock
    private PrintWriter writer;

    private String URI = "/users";

    @InjectMocks
    private UserServlet userServlet;

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
        var user = new UserDTO(1, "test", "test", LocalDate.now());

        when(userService.findAll()).thenReturn(List.of(user));
        when(resp.getWriter()).thenReturn(writer);
        when(req.getRequestURI()).thenReturn(URI);

        userServlet.doGet(req, resp);

        verify(userService).findAll();
        verify(writer).write(om.writeValueAsString(List.of(user)));
    }

    @Test
    public void testDoGetById() throws IOException {
        var user = new UserDTO(1, "test", "test", LocalDate.now());

        when(userService.findById(anyInt())).thenReturn(Optional.of(user));
        when(resp.getWriter()).thenReturn(writer);
        when(req.getRequestURI()).thenReturn(URI + "/1");

        userServlet.doGet(req, resp);

        verify(userService).findById(anyInt());
        verify(writer).write(om.writeValueAsString(user));
    }

    @Test
    public void testDoPost() throws IOException, ServletException {
        var userDto = new UserDTO(2, "test", "test", LocalDate.now());

        when(userService.save(any(UserCreateUpdateDTO.class))).thenReturn(userDto);
        when(resp.getWriter()).thenReturn(writer);
        when(req.getRequestURI()).thenReturn(URI);

        userServlet.doPost(req, resp);

        verify(userService).save(any(UserCreateUpdateDTO.class));
        verify(writer).write(om.writeValueAsString(userDto));
    }

    @Test
    public void testDoPut() throws IOException, ServletException {
        var userDto = new UserDTO(1, "test", "test", LocalDate.now());
        when(userService.update(any(UserCreateUpdateDTO.class),anyInt())).thenReturn(userDto);
        when(resp.getWriter()).thenReturn(writer);
        when(req.getRequestURI()).thenReturn(URI + "/1");

        userServlet.doPut(req, resp);

        verify(userService).update(any(UserCreateUpdateDTO.class), anyInt());
        verify(resp).getWriter();
        verify(writer).write(om.writeValueAsString(userDto));
    }

    @Test
    public void testDoDelete() throws IOException, ServletException {
        when(req.getRequestURI()).thenReturn(URI + "/1");

        userServlet.doDelete(req, resp);

        verify(userService).deleteById(anyInt());
    }
}
