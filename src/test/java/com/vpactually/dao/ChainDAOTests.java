package com.vpactually.dao;

import com.vpactually.util.ContainerUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.JdbcDatabaseContainer;

import java.sql.SQLException;

public class ChainDAOTests {

    private static final UserDAO USER_DAO = UserDAO.getInstance();
    private static final TaskStatusDAO TASK_STATUS_DAO = TaskStatusDAO.getInstance();
    private static final TaskDAO TASK_DAO = TaskDAO.getInstance();
    private static final LabelDAO LABEL_DAO = LabelDAO.getInstance();

    private static JdbcDatabaseContainer<?> postgresqlContainer;

    @BeforeEach
    public void startContainer() throws SQLException {
        postgresqlContainer = ContainerUtil.run(postgresqlContainer);
    }

    @AfterAll
    public static void stopContainer() {
        postgresqlContainer.stop();
    }

//    @Test
//    void testChain1() {
//        assertThat(USER_DAO.findById(1).get()).isEqualTo(ADMIN);
//        assertThat(TASK_STATUS_DAO.findById(1).get()).isEqualTo(EXISTING_STATUS);
//        assertThat(TASK_DAO.findById(1).get()).isEqualTo(EXISTING_TASK);
//        assertThat(LABEL_DAO.findById(1).get()).isEqualTo(EXISTING_LABEL_1);
//
//        USER_DAO.save(ANOTHER_USER);
//        TASK_STATUS_DAO.save(ANOTHER_STATUS);
//        TASK_DAO.save(ANOTHER_TASK);
//        LABEL_DAO.save(ANOTHER_LABEL);
//
//        assertThat(USER_DAO.findById(2).get()).isEqualTo(ANOTHER_USER);
//        assertThat(TASK_STATUS_DAO.findById(6).get()).isEqualTo(ANOTHER_STATUS);
//        assertThat(TASK_DAO.findById(2).get()).isEqualTo(ANOTHER_TASK);
//        assertThat(LABEL_DAO.findById(3).get()).isEqualTo(ANOTHER_LABEL);
//    }
}
