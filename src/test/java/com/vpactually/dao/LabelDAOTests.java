package com.vpactually.dao;

import com.vpactually.util.ContainerUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static com.vpactually.util.DataUtil.ANOTHER_LABEL;
import static com.vpactually.util.DataUtil.EXISTING_LABEL_1;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class LabelDAOTests {

    @InjectMocks
    private static LabelDAO LABEL_DAO;

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
        assertThat(LABEL_DAO.findAll()).contains(EXISTING_LABEL_1).doesNotContain(ANOTHER_LABEL);

    }

    @Test
    public void testFindById() {
        assertThat(LABEL_DAO.findById(EXISTING_LABEL_1.getId()).get()).isEqualTo(EXISTING_LABEL_1);
    }


    @Test
    public void testSave() {
        assertThat(LABEL_DAO.save(ANOTHER_LABEL)).isEqualTo(ANOTHER_LABEL);
        assertThat(LABEL_DAO.findById(ANOTHER_LABEL.getId()).get().getId()).isEqualTo(ANOTHER_LABEL.getId());
    }

    @Test
    public void testUpdate() {
        var label = ANOTHER_LABEL;
        label.setId(1);
        assertThat(LABEL_DAO.update(label)).isEqualTo(label);
    }

    @Test
    public void testDelete() {
        var label = ANOTHER_LABEL;
        LABEL_DAO.save(label);

        assertThat(LABEL_DAO.findById(label.getId()).get()).isEqualTo(label);
        assertThat(LABEL_DAO.deleteById(ANOTHER_LABEL.getId())).isTrue();
        assertThat(LABEL_DAO.findById(ANOTHER_LABEL.getId())).isEmpty();
    }

}
