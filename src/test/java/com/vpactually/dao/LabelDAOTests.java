package com.vpactually.dao;

import com.vpactually.entities.Label;
import com.vpactually.util.ContainerUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.JdbcDatabaseContainer;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;

import static com.vpactually.util.DataUtil.ANOTHER_LABEL;
import static com.vpactually.util.DataUtil.EXISTING_LABEL_1;
import static org.assertj.core.api.Assertions.assertThat;

public class LabelDAOTests {


    private static final LabelDAO LABEL_DAO = LabelDAO.getInstance();

    private static JdbcDatabaseContainer<?> postgresqlContainer;

    @BeforeAll
    public static void startContainer() throws SQLException {
        postgresqlContainer = ContainerUtil.run(postgresqlContainer);
    }

    @AfterAll
    public static void stopContainer() {
        postgresqlContainer.stop();
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
    public void testFindLabelsByTaskId() {
        var label1 = new Label(1, "Bug", LocalDate.now(), new HashSet<>());
        var label2 = new Label(2, "Feature", LocalDate.now(), new HashSet<>());

        var labelsByTaskId = LABEL_DAO.findLabelsByTaskId(1);
        assertThat(labelsByTaskId).contains(label1, label2);


    }


    @Test
    public void testSave() {
        assertThat(LABEL_DAO.save(ANOTHER_LABEL)).isEqualTo(ANOTHER_LABEL);
        assertThat(LABEL_DAO.findById(ANOTHER_LABEL.getId()).get()).isEqualTo(ANOTHER_LABEL);
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

        assertThat(LABEL_DAO.deleteById(EXISTING_LABEL_1.getId())).isFalse();
    }

}
