package com.vpactually.services;

import com.vpactually.dao.LabelDAO;
import com.vpactually.dto.labels.LabelCreateUpdateDTO;
import com.vpactually.dto.labels.LabelDTO;
import com.vpactually.entities.Label;
import com.vpactually.util.ContainerUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;
import org.testcontainers.containers.JdbcDatabaseContainer;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.vpactually.util.DataUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class LabelServiceTests {

    @InjectMocks
    private LabelService labelService = LabelService.getInstance();

    @Mock
    private LabelDAO labelDAO;

    @Mock
    private static JdbcDatabaseContainer<?> postgresqlContainer;

    @BeforeAll
    static void beforeAll() throws SQLException {
        postgresqlContainer = ContainerUtil.run(postgresqlContainer);
    }

    @AfterAll
    static void afterAll() {
        postgresqlContainer.stop();
    }

    @Test
    void testFindAll() {
        var labels = List.of(EXISTING_LABEL_1, EXISTING_LABEL_2);
        var labelDTOs = List.of(
                new LabelDTO(EXISTING_LABEL_1.getId(), EXISTING_LABEL_1.getName()),
                new LabelDTO(EXISTING_LABEL_2.getId(), EXISTING_LABEL_2.getName()));

        when(labelDAO.findAll()).thenReturn(labels);

        List<LabelDTO> all = labelService.findAll();

        assertEquals(labelDTOs.toString(), all.toString());
        verify(labelDAO, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Label label = EXISTING_LABEL_1;
        LabelDTO labelDTO = new LabelDTO(EXISTING_LABEL_1.getId(), EXISTING_LABEL_1.getName());

        Optional<LabelDTO> result = labelService.findById(1);

        assertTrue(result.isPresent());
        assertEquals(labelDTO.toString(), result.get().toString());
    }

    @Test
    void testUpdate() {
        Label label = EXISTING_LABEL_1;
        label.setName("new name");
        LabelCreateUpdateDTO labelCreateUpdateDTO = new LabelCreateUpdateDTO(JsonNullable.of("new name"));

        LabelDTO labelDTO = new LabelDTO(EXISTING_LABEL_1.getId(), "new name");
        LabelDTO result = labelService.update(labelCreateUpdateDTO, 1);


        assertEquals(labelDTO.toString(), result.toString());
    }

    @Test
    void testSave() {
        LabelDTO labelDTO = new LabelDTO(3, "new name");
        LabelCreateUpdateDTO labelCreateUpdateDTO = new LabelCreateUpdateDTO(JsonNullable.of(ANOTHER_LABEL.getName()));

        LabelDTO result = labelService.save(labelCreateUpdateDTO);

        assertEquals(labelDTO.toString(), result.toString());
    }

    @Test
    void testDeleteById() {
        labelService.deleteById(1);
        assertThat(labelDAO.findById(1)).isEmpty();
    }
}