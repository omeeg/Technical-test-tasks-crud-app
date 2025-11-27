package com.example.dts.exceptions;
//Call this for handling cases where we don't find a task.
//Not sure if we really need an exception here - since can be fine to have empty task result list.
public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String message) {
        super(message);
    }
}
