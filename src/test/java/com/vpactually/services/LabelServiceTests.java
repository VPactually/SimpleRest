package com.vpactually.services;

import com.vpactually.dao.LabelDAO;
import com.vpactually.dto.labels.LabelCreateUpdateDTO;
import com.vpactually.dto.labels.LabelDTO;
import com.vpactually.entities.Label;
import com.vpactually.mappers.LabelMapper;
import com.vpactually.util.ContainerUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.vpactually.util.DataUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LabelServiceTests {

    @Mock
    private LabelDAO labelDAO;

    @Mock
    private LabelMapper labelMapper;

    @InjectMocks
    private LabelService labelService;

    @BeforeAll
    public static void startContainer() throws SQLException {
        ContainerUtil.run();
    }

    @AfterAll
    public static void stopContainer() {
        ContainerUtil.stop();
    }

    @Test
    public void testFindAll() {
        var labels = List.of(EXISTING_LABEL_1, EXISTING_LABEL_2);
        var labelDTOs = List.of(
                new LabelDTO(EXISTING_LABEL_1.getId(), EXISTING_LABEL_1.getName()),
                new LabelDTO(EXISTING_LABEL_2.getId(), EXISTING_LABEL_2.getName())
        );

        when(labelDAO.findAll()).thenReturn(labels);
        when(labelMapper.map(labels.get(0))).thenReturn(labelDTOs.get(0));
        when(labelMapper.map(labels.get(1))).thenReturn(labelDTOs.get(1));

        List<LabelDTO> result = labelService.findAll();

        assertThat(result.toString()).isEqualTo(labelDTOs.toString());
        verify(labelDAO).findAll();
        verify(labelMapper, times(2)).map(any(Label.class));
    }

    @Test
    public void testFindById() {
        when(labelDAO.findById(EXISTING_LABEL_1.getId())).thenReturn(Optional.of(EXISTING_LABEL_1));
        when(labelMapper.map(EXISTING_LABEL_1)).thenReturn(new LabelDTO(EXISTING_LABEL_1.getId(), EXISTING_LABEL_1.getName()));

        LabelDTO result = labelService.findById(EXISTING_LABEL_1.getId()).get();

        assertThat(result.toString()).isEqualTo(new LabelDTO(EXISTING_LABEL_1.getId(), EXISTING_LABEL_1.getName()).toString());
        verify(labelDAO).findById(EXISTING_LABEL_1.getId());
        verify(labelMapper).map(EXISTING_LABEL_1);
    }

    @Test
    public void testSave() {
        var labelCreateUpdateDTO = new LabelCreateUpdateDTO(JsonNullable.of(ANOTHER_LABEL.getName()));
        var label = ANOTHER_LABEL;

        when(labelMapper.map(labelCreateUpdateDTO)).thenReturn(label);
        when(labelDAO.save(label)).thenReturn(label);
        when(labelMapper.map(label)).thenReturn(new LabelDTO(label.getId(), label.getName()));


        LabelDTO result = labelService.save(labelCreateUpdateDTO);

        assertThat(result.toString()).isEqualTo(new LabelDTO(label.getId(), label.getName()).toString());
        verify(labelMapper).map(labelCreateUpdateDTO);
        verify(labelDAO).save(label);
        verify(labelMapper).map(label);
    }

    @Test
    public void testUpdate() {
        var labelCreateUpdateDTO = new LabelCreateUpdateDTO(JsonNullable.of("newLabelName"));
        var label = EXISTING_LABEL_1;
        label.setName("newLabelName");

        when(labelDAO.findById(EXISTING_LABEL_1.getId())).thenReturn(Optional.of(EXISTING_LABEL_1));
        when(labelDAO.update(EXISTING_LABEL_1)).thenReturn(label);
        when(labelMapper.map(EXISTING_LABEL_1)).thenReturn(new LabelDTO(EXISTING_LABEL_1.getId(), EXISTING_LABEL_1.getName()));

        LabelDTO result = labelService.update(labelCreateUpdateDTO, EXISTING_LABEL_1.getId());

        assertThat(result.toString()).isEqualTo(new LabelDTO(EXISTING_LABEL_1.getId(), "newLabelName").toString());
        verify(labelDAO).findById(EXISTING_LABEL_1.getId());
        verify(labelDAO).update(EXISTING_LABEL_1);
        verify(labelMapper).map(EXISTING_LABEL_1);
    }

    @Test
    public void testDeleteById() {
        when(labelDAO.deleteById(EXISTING_LABEL_1.getId())).thenReturn(true);
        labelService.deleteById(EXISTING_LABEL_1.getId());
        verify(labelDAO).deleteById(EXISTING_LABEL_1.getId());
    }
}
