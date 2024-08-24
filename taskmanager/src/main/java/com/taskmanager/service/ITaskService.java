package com.taskmanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.taskmanager.model.Task;

public interface ITaskService {
    Task createTask(Task task, Long userId);
    Page<Task> getAllTasks(Pageable pageable);
    Task getTaskById(Long id);
    Task updateTask(Long id, Task taskDetails);
    void deleteTask(Long id);
}
