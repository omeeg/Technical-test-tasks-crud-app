package com.example.dts.controller;

import com.example.dts.domain.dtos.CreateTaskRequest;
import com.example.dts.domain.Task;
import com.example.dts.domain.dtos.GetTasksRequest;
import com.example.dts.services.TaskService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);


    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    //If requests are invalid i.e. don't match request bodies or attributes of CreateTaskRequest
    //or GetTasksRequest - this should be caught by @Valid annotation and ErrorHandler middleware

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody CreateTaskRequest request) {
        //errors for this are caught in service layer
        logger.info("Received create task request: {}",request);
        Task response = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task response = taskService.getTaskById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<Task>> getTasks(@Valid @ModelAttribute GetTasksRequest request) {
        Page<Task> response = taskService.getTasksPage(request);
        return ResponseEntity.ok(response);
    }
}

