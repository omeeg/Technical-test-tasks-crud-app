package com.example.dts.services;

import com.example.dts.data.entities.TaskEntity;
import com.example.dts.data.entities.TaskStatus;
import com.example.dts.data.repositories.TaskEntityRepository;
import com.example.dts.domain.dtos.CreateTaskRequest;
import com.example.dts.domain.Task;
import com.example.dts.domain.dtos.GetTasksRequest;
import com.example.dts.exceptions.TaskNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTests {

    @Mock
    private TaskEntityRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private TaskEntity testTaskEntity;
    private LocalDateTime futureDate;

    @BeforeEach
    void setUp() {
        futureDate = LocalDateTime.now().plusDays(7);

        testTaskEntity = new TaskEntity();
        testTaskEntity.setId(1L);
        testTaskEntity.setTitle("Test Task");
        testTaskEntity.setDescription("Description of Task");
        testTaskEntity.setStatus(TaskStatus.TO_DO);
        testTaskEntity.setDueDate(futureDate);
        testTaskEntity.setCreationDate(LocalDateTime.now());
    }

    //Create tests

    @Test
    @DisplayName("Should successfully create task with all fields")
    void createTask_ValidRequest_ReturnsTask() {
        CreateTaskRequest request = new CreateTaskRequest(
                "Test Task",
                "Task Description",
                futureDate
        );

        when(taskRepository.save(ArgumentMatchers.any(TaskEntity.class))).thenReturn(testTaskEntity);

        Task result = taskService.createTask(request);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Test Task", result.title());
        verify(taskRepository, times(1)).save(ArgumentMatchers.any(TaskEntity.class));
    }

    @Test
    @DisplayName("Should create task with null description")
    void createTask_NullDescription_Succeeds() {
        CreateTaskRequest request = new CreateTaskRequest(
                "Task Title",
                null,
                futureDate
        );

        TaskEntity entityWithNullDesc = new TaskEntity();
        entityWithNullDesc.setId(1L);
        entityWithNullDesc.setTitle("Task Title");
        entityWithNullDesc.setDescription(null);
        entityWithNullDesc.setStatus(TaskStatus.TO_DO);
        entityWithNullDesc.setDueDate(futureDate);

        when(taskRepository.save(ArgumentMatchers.any(TaskEntity.class))).thenReturn(entityWithNullDesc);

        Task result = taskService.createTask(request);

        assertNotNull(result);
        assertNull(result.description());
        verify(taskRepository, times(1)).save(ArgumentMatchers.any(TaskEntity.class));
    }

    @Test
    @DisplayName("Should throw exception when request is null")
    void createTask_NullRequest_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> taskService.createTask(null)
        );
        assertEquals("CreateTaskRequest cannot be null", exception.getMessage());
        verify(taskRepository, never()).save(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Should throw exception when title is blank")
    void createTask_BlankTitle_ThrowsException() {
        CreateTaskRequest request = new CreateTaskRequest(
                "   ",
                "Description",
                futureDate
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> taskService.createTask(request)
        );
        assertEquals("Title is required", exception.getMessage());
        verify(taskRepository, never()).save(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Should throw exception when title exceeds 255 characters")
    void createTask_TitleTooLong_ThrowsException() {
        String longTitle = "a".repeat(256);
        CreateTaskRequest request = new CreateTaskRequest(
                longTitle,
                "Description",
                futureDate
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> taskService.createTask(request)
        );
        assertEquals("Title cannot exceed 255 characters", exception.getMessage());
        verify(taskRepository, never()).save(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Should throw exception when due date is in the past")
    void createTask_PastDueDate_ThrowsException() {
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        CreateTaskRequest request = new CreateTaskRequest(
                "Valid Title",
                "Description",
                pastDate
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> taskService.createTask(request)
        );
        assertEquals("Due date cannot be in the past", exception.getMessage());
        verify(taskRepository, never()).save(any());
    }

//    @Test
//    @DisplayName("Should throw exception when status is COMPLETED")
//    void createTask_CompletedStatus_ThrowsException() {
//        CreateTaskRequest request = new CreateTaskRequest(
//                "Valid Title",
//                "Description",
//                "COMPLETED",
//                futureDate
//        );
//
//        IllegalArgumentException exception = assertThrows(
//                IllegalArgumentException.class,
//                () -> taskService.createTask(request)
//        );
//        assertEquals("Task cannot be already completed", exception.getMessage());
//        verify(taskRepository, never()).save(any());
//    }

    //SERVICE GETTER TESTS

    @Test
    @DisplayName("Should return task when valid ID is provided")
    void getTaskById_ValidId_ReturnsTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTaskEntity));

        Task result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Test Task", result.title());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when task not found")
    void getTaskById_NonExistentId_ThrowsException() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(999L));
        verify(taskRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should return empty page when no tasks exist")
    void getTasksPage_NoTasks_ReturnsEmptyPage() {
        Page<TaskEntity> emptyPage = Page.empty();
        when(taskRepository.findAll(any(PageRequest.class))).thenReturn(emptyPage);

        Page<Task> result = taskService.getTasksPage(new GetTasksRequest(0, 10, "dueDate", true));

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(taskRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    @DisplayName("Should throw exception when orderBy field is invalid")
    void getTasksPage_InvalidOrderBy_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> taskService.getTasksPage(new GetTasksRequest(0, 10, "gobledeygoob", true))
        );

        assertEquals("Invalid order by argument", exception.getMessage());
        verify(taskRepository, never()).findAll(any(PageRequest.class));
    }
}
