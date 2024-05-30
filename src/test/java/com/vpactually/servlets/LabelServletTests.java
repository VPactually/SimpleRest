package com.vpactually.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vpactually.dto.labels.LabelCreateUpdateDTO;
import com.vpactually.dto.labels.LabelDTO;
import com.vpactually.services.LabelService;
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
public class LabelServletTests {

    @Mock
    private LabelService service;

    @Mock
    private ObjectMapper om;

    @Mock
    private HttpServletRequest req;

    @Mock
    private HttpServletResponse resp;

    @Mock
    private PrintWriter writer;

    private String URI = "/labels";

    @InjectMocks
    private LabelServlet servlet;

    @BeforeAll
    public static void startContainer() throws SQLException {
        ContainerUtil.run();
    }

    @AfterAll
    public static void stopContainer() {
        ContainerUtil.stop();
    }

    @Test
    public void testDoGetAll() throws IOException, ServletException {
        var labelDTO = new LabelDTO(1, "test");

        when(service.findAll()).thenReturn(List.of(labelDTO));
        when(resp.getWriter()).thenReturn(writer);
        when(req.getRequestURI()).thenReturn(URI);

        servlet.doGet(req, resp);

        verify(service).findAll();
        verify(writer).write(om.writeValueAsString(List.of(labelDTO)));
    }

    @Test
    public void testDoGetById() throws IOException, ServletException {
        var labelDTO = new LabelDTO(1, "test");

        when(service.findById(anyInt())).thenReturn(Optional.of(labelDTO));
        when(resp.getWriter()).thenReturn(writer);
        when(req.getRequestURI()).thenReturn(URI + "/1");

        servlet.doGet(req, resp);

        verify(service).findById(anyInt());
        verify(writer).write(om.writeValueAsString(labelDTO));
    }

    @Test
    public void testDoPost() throws IOException, ServletException {
        var labelDTO = new LabelDTO(1, "test");

        when(service.save(any(LabelCreateUpdateDTO.class))).thenReturn(labelDTO);
        when(resp.getWriter()).thenReturn(writer);
        when(req.getRequestURI()).thenReturn(URI);

        servlet.doPost(req, resp);

        verify(service).save(any(LabelCreateUpdateDTO.class));
        verify(writer).write(om.writeValueAsString(labelDTO));
    }

    @Test
    public void testDoPut() throws IOException, ServletException {
        var labelDTO = new LabelDTO(1, "test");
        when(service.update(any(LabelCreateUpdateDTO.class),anyInt())).thenReturn(labelDTO);
        when(resp.getWriter()).thenReturn(writer);
        when(req.getRequestURI()).thenReturn(URI + "/1");

        servlet.doPut(req, resp);

        verify(service).update(any(LabelCreateUpdateDTO.class), anyInt());
        verify(resp).getWriter();
        verify(writer).write(om.writeValueAsString(labelDTO));
    }

    @Test
    public void testDoDelete() throws IOException, ServletException {
        when(req.getRequestURI()).thenReturn(URI + "/1");

        servlet.doDelete(req, resp);

        verify(service).deleteById(anyInt());
    }
}
