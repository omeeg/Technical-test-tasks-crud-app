package com.example.dts.entities.repositories;

import com.example.dts.data.entities.TaskEntity;
import com.example.dts.data.entities.TaskStatus;
import com.example.dts.data.repositories.TaskEntityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class TaskEntityRepositoryTest {

    @Autowired
    private TaskEntityRepository taskRepository;

    @Test
    void saveAndRetrieveTask() {
        TaskEntity task = new TaskEntity();
        task.setTitle("Test task");
        task.setDescription("Create a test task");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setDueDate(LocalDateTime.now().plusDays(1));

        TaskEntity saved = taskRepository.save(task);
        assertThat(saved.getId()).isNotNull();

        TaskEntity retrieved = taskRepository.findById(saved.getId()).orElseThrow();
        assertThat(retrieved.getTitle()).isEqualTo("Test task");
        assertThat(retrieved.getCreatedAt()).isNotNull();
    }
}
