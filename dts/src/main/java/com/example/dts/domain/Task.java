package com.example.dts.domain;

import com.example.dts.data.entities.TaskEntity;
import com.example.dts.data.entities.TaskStatus;

import java.time.LocalDateTime;

/** Domain logic equivalent of Task
 * //Here we can include custom logic where necessary for conversion.
 * //Data layer entities store use more raw data types - if necessary could extend Task to
 * //more specific requirements.
 * @param id - id
 * @param title - title of task
 * @param description - description of task
 * @param status - status of task i.e. TO_DO, IN_PROGRESS or COMPLETED
 * @param dueDate - date when task is due
 * @param createdAt - date when task is created
 */
public record Task(
        Long id,
        String title,
        String description,
        TaskStatus status,
        LocalDateTime dueDate,
        LocalDateTime createdAt)
{
    public static Task convertTaskEntityToTask(TaskEntity task) {
        return new Task(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getDueDate(),
                task.getCreatedAt()
        );
    }
}