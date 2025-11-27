package com.example.dts.controller;

import com.example.dts.data.entities.TaskStatus;
import com.example.dts.domain.Task;
import com.example.dts.domain.dtos.CreateTaskRequest;
import com.example.dts.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private Task sampleTask;
    private CreateTaskRequest validRequest;
    private LocalDateTime futureDate;

    @BeforeEach
    void setUp() {
        futureDate = LocalDateTime.of(2025, 12, 31, 23, 59);

        sampleTask = new Task(
                1L,
                "Sample Task",
                "Sample Description",
                TaskStatus.TO_DO,
                futureDate,
                LocalDateTime.now()
        );

        validRequest = new CreateTaskRequest(
                "New Task",
                "Task Description",
                futureDate
        );
    }

    @Test
    void createTask_ValidRequest_Returns201() throws Exception {
        when(taskService.createTask(any(CreateTaskRequest.class))).thenReturn(sampleTask);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Sample Task"))
                .andExpect(jsonPath("$.description").value("Sample Description"))
                .andExpect(jsonPath("$.status").value("TO_DO"))
                .andExpect(jsonPath("$.dueDate").exists())
                .andExpect(jsonPath("$.creationDate").exists());

        verify(taskService, times(1)).createTask(any(CreateTaskRequest.class));
    }

    @Test
    void createTask_NullDescription_Returns201() throws Exception {
        CreateTaskRequest requestWithNullDesc = new CreateTaskRequest(
                "Task Title",
                null,
                futureDate
        );

        Task taskWithNullDesc = new Task(
                2L,
                "Task Title",
                null,
                TaskStatus.TO_DO,
                futureDate,
                LocalDateTime.now()
        );

        when(taskService.createTask(any(CreateTaskRequest.class))).thenReturn(taskWithNullDesc);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWithNullDesc)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Task Title"))
                .andExpect(jsonPath("$.description").isEmpty());

        verify(taskService, times(1)).createTask(any(CreateTaskRequest.class));
    }

    @Test
    void createTask_MissingBody_Returns400() throws Exception {
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(taskService, never()).createTask(any());
    }

    @Test
    void createTask_BlankTitle_Returns400() throws Exception {
        CreateTaskRequest invalidRequest = new CreateTaskRequest(
                "   ",
                "Description",
                futureDate
        );

        when(taskService.createTask(any(CreateTaskRequest.class)))
                .thenThrow(new IllegalArgumentException("Title is required"));

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTask_PastDueDate_Returns400() throws Exception {
        // Arrange
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        CreateTaskRequest invalidRequest = new CreateTaskRequest(
                "Valid Title",
                "Description",
                pastDate
        );

        when(taskService.createTask(any(CreateTaskRequest.class)))
                .thenThrow(new IllegalArgumentException("Due date cannot be in the past"));

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

}
