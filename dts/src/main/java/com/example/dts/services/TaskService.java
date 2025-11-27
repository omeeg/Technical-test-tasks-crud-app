package com.example.dts.services;

import com.example.dts.data.entities.TaskStatus;
import com.example.dts.domain.dtos.CreateTaskRequest;
import com.example.dts.domain.dtos.GetTasksRequest;
import com.example.dts.exceptions.TaskNotFoundException;
import com.example.dts.data.entities.TaskEntity;
import com.example.dts.data.repositories.TaskEntityRepository;
import com.example.dts.domain.Task;
import com.example.dts.validation.*;
import org.jetbrains.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    public final TaskEntityRepository taskRepository;

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    //springboot automatically injects
    public TaskService(TaskEntityRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // ====================================
    //       GET SERVICE METHODS
    // ====================================

    //Paginated and ordered get
    public Page<Task> getTasksPage(GetTasksRequest request) {
        //More time would use a .toString() method define it in request here for logs
        logger.info("Getting {} tasks on page {} ordered by {} ({})", request.getSize(), request.getPage(), request.getOrderBy(), request.isAscending() ? "ASC" : "DESC");
        validateGetTaskRequest(request);
        Sort sort = request.isAscending() ? Sort.by(request.getOrderBy()).ascending() : Sort.by(request.getOrderBy()).descending();

        return taskRepository.findAll(PageRequest.of(request.getPage(), request.getSize(), sort))
                .map(Task::convertTaskEntityToTask);
    }

    public Task getTaskById(Long id) {
        if(id==null || id<0) throw new IllegalArgumentException("Invalid task id");
        TaskEntity task = taskRepository.findById(id).orElseThrow(()->new TaskNotFoundException("No user of that Id Found"));
        return Task.convertTaskEntityToTask(task);
    }

    // ====================================
    //        POST SERVICE METHODS
    // ====================================

    public Task createTask(CreateTaskRequest request) {
        // Validate request
        validateCreateTaskRequest(request);

        //Converting to task entity
        TaskEntity newEntity = new TaskEntity();
        newEntity.setTitle(request.getTitle());
        newEntity.setDescription(request.getDescription());
        //already validated that this cast operation won't throw
        newEntity.setStatus(TaskStatus.TO_DO);
        newEntity.setDueDate(request.getDueDate());

        //saving new taskEntity and returning task
        TaskEntity savedTask = taskRepository.save(newEntity);
        return Task.convertTaskEntityToTask(savedTask);
    }

    // ====================================
     //  VALIDATION
     // ===================================

    //Possibly could move these validate methods into validation package
    @VisibleForTesting
    private void validateGetTaskRequest(GetTasksRequest request) {
        if(request.getPage() < 0) {
            logger.error("Invalid page number {}", request.getPage());
            throw new IllegalArgumentException("Invalid page number");
        }
        if(request.getSize() <= 0) {
            logger.error("Invalid page size {}", request.getSize());
            throw new IllegalArgumentException("Invalid page size");
        }
        if(!ValidOrderByHelper.isValidOrderBy(request.getOrderBy())) {
            logger.error("Invalid order by operator {}", request.getOrderBy());
            throw new IllegalArgumentException("Invalid order by argument");
        }
    }


    @VisibleForTesting
    private void validateCreateTaskRequest(CreateTaskRequest request) {
        if (request == null) throw new IllegalArgumentException("CreateTaskRequest cannot be null");
        //This log might cause issues sometimes (?) if null request - so i put here after null check
        logger.info("Attempting to add a new task request {}",request.toString());

        //Validate title
        if (ValidTitleHelper.isBlankTitle(request.getTitle()))
            throw new IllegalArgumentException("Title is required");
        if (ValidTitleHelper.isTitleTooLong(request.getTitle()))
            throw new IllegalArgumentException("Title cannot exceed 255 characters");

        //Validate description
        if (ValidDescriptionHelper.isDescriptionTooLong(request.getDescription()))
            throw new IllegalArgumentException("Description cannot exceed 1000 characters");

        //Validate due date
        if (ValidDateHelper.isDateInPast(request.getDueDate()))
            throw new IllegalArgumentException("Due date cannot be in the past");
    }

}


