package com.vpactually.repositories;

import com.vpactually.entities.Task;
import com.vpactually.util.ContainerUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Set;

import static com.vpactually.util.DataUtil.ANOTHER_LABEL;
import static com.vpactually.util.DataUtil.EXISTING_LABEL_1;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class LabelRepositoryTests {

    @InjectMocks
    private static LabelRepository labelRepository;

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
        var label = EXISTING_LABEL_1;
        label.setTasks(null);

        assertThat(labelRepository.findAll()).contains(label).doesNotContain(ANOTHER_LABEL);

    }

    @Test
    public void testFindById() {
        var label = EXISTING_LABEL_1;
        label.setTasks(Set.of(new Task(1)));
        assertThat(labelRepository.findById(label.getId()).get()).isEqualTo(EXISTING_LABEL_1);
    }


    @Test
    public void testSave() {
        assertThat(labelRepository.save(ANOTHER_LABEL)).isEqualTo(ANOTHER_LABEL);
        assertThat(labelRepository.findById(ANOTHER_LABEL.getId()).get().getId()).isEqualTo(ANOTHER_LABEL.getId());
    }

    @Test
    public void testUpdate() {
        var label = ANOTHER_LABEL;
        label.setId(1);
        assertThat(labelRepository.update(label)).isEqualTo(label);
    }

    @Test
    public void testDelete() {
        var label = ANOTHER_LABEL;
        labelRepository.save(label);

        assertThat(labelRepository.findById(label.getId()).get()).isEqualTo(label);
        assertThat(labelRepository.deleteById(ANOTHER_LABEL.getId())).isTrue();
        assertThat(labelRepository.findById(ANOTHER_LABEL.getId())).isEmpty();
    }

}
