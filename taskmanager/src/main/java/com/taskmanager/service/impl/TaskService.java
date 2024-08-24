package com.taskmanager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import com.taskmanager.repo.TaskRepository;
import com.taskmanager.repo.UserRepository;
import com.taskmanager.service.ITaskService;

@Service
public class TaskService implements ITaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Task createTask(Task task, Long userId) {
        logger.info("Creating task with title '{}' for userId: {}", task.getTitle(), userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        task.setAssignedTo(user);
        Task savedTask = taskRepository.save(task);
        logger.info("Task created with id {}", savedTask.getId());
        return savedTask;
    }

    @Override
    public Page<Task> getAllTasks(Pageable pageable) {
        Page<Task> tasks = taskRepository.findAll(pageable);
        logger.info("Retrieved {} tasks", tasks.getNumberOfElements());
        return tasks;
    }

    @Override
    public Task getTaskById(Long id) {
        logger.info("Fetching task with id {}", id);
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));
    }

    @Override
    public Task updateTask(Long id, Task taskDetails) {
        logger.info("Updating task with id {}", id);
        Task task = getTaskById(id);
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setStatus(taskDetails.getStatus());

        // Optionally update the assigned user if needed
        if (taskDetails.getAssignedTo() != null) {
            User user = userRepository.findById(taskDetails.getAssignedTo().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + taskDetails.getAssignedTo().getId()));
            task.setAssignedTo(user);
        }

        Task updatedTask = taskRepository.save(task);
        logger.info("Task updated with id {}", updatedTask.getId());
        return updatedTask;
    }

    @Override
    public void deleteTask(Long id) {
        logger.info("Deleting task with id {}", id);
        Task task = getTaskById(id);
        taskRepository.deleteById(task.getId());
        logger.info("Task deleted with id {}", id);
    }
}
