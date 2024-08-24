package com.taskmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.taskmanager.enums.Status;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import com.taskmanager.repo.TaskRepository;
import com.taskmanager.repo.UserRepository;
import com.taskmanager.service.impl.TaskService;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private User user;
    private Task task;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("Test User");

        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Task Description");
        task.setStatus(Status.PENDING);  
        task.setAssignedTo(user);
    }

    @Test
    void createTask_ShouldReturnTask_WhenValidUserId() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask = taskService.createTask(task, user.getId());

        assertNotNull(createdTask);
        assertEquals(task.getId(), createdTask.getId());
        verify(taskRepository).save(task);
        verify(userRepository).findById(user.getId());
    }

    @Test
    void createTask_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.createTask(task, user.getId()));
        verify(userRepository).findById(user.getId());
    }

    @Test
    void getAllTasks_ShouldReturnPaginatedTaskList() {
        Pageable pageable = Pageable.ofSize(10);
        Page<Task> tasksPage = new PageImpl<>(Arrays.asList(task), pageable, 1);
        when(taskRepository.findAll(pageable)).thenReturn(tasksPage);

        Page<Task> tasks = taskService.getAllTasks(pageable);

        assertNotNull(tasks);
        assertEquals(1, tasks.getTotalElements());
        assertEquals(task.getId(), tasks.getContent().get(0).getId());
        verify(taskRepository).findAll(pageable);
    }

    @Test
    void getTaskById_ShouldReturnTask_WhenTaskExists() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        Task foundTask = taskService.getTaskById(task.getId());

        assertNotNull(foundTask);
        assertEquals(task.getId(), foundTask.getId());
        verify(taskRepository).findById(task.getId());
    }

    @Test
    void getTaskById_ShouldThrowException_WhenTaskNotFound() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.getTaskById(task.getId()));
        verify(taskRepository).findById(task.getId());
    }

    @Test
    void updateTask_ShouldReturnUpdatedTask() {
        // Arrange
        Task existingTask = new Task();
        existingTask.setId(task.getId());
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Description");
        existingTask.setStatus(Status.PENDING);

        // Mock existing task retrieval
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(existingTask));
        
        // Mock save operation to return updated task
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task updatedTask = invocation.getArgument(0);
            updatedTask.setId(existingTask.getId()); // Ensure the ID matches
            return updatedTask;
        });

        // Task details to be updated
        Task taskDetails = new Task();
        taskDetails.setTitle("Updated Title");
        taskDetails.setDescription("Updated Description");
        taskDetails.setStatus(Status.COMPLETED);

        // Act
        Task updatedTask = taskService.updateTask(task.getId(), taskDetails);

        // Assert
        assertNotNull(updatedTask);
        assertEquals(task.getId(), updatedTask.getId());
        assertEquals("Updated Title", updatedTask.getTitle());
        assertEquals("Updated Description", updatedTask.getDescription());
        assertEquals(Status.COMPLETED, updatedTask.getStatus());

        // Verify interactions
        verify(taskRepository).findById(task.getId());
       
    }


    @Test
    void deleteTask_ShouldDeleteTask_WhenTaskExists() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        taskService.deleteTask(task.getId());

        verify(taskRepository).deleteById(task.getId());
        verify(taskRepository).findById(task.getId());
    }

    @Test
    void deleteTask_ShouldThrowException_WhenTaskNotFound() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.deleteTask(task.getId()));
        verify(taskRepository).findById(task.getId());
    }
}
