package com.taskmanager.service;

import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task sampleTask;
    private UUID taskId;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        sampleTask = new Task("Test Task", "Test Description", TaskStatus.TODO);
        sampleTask.setId(taskId);
    }

    @Test
    void getAllTasks_ShouldReturnAllTasks() {
        // Given
        List<Task> expectedTasks = Arrays.asList(sampleTask);
        when(taskRepository.findAll()).thenReturn(expectedTasks);

        // When
        List<Task> actualTasks = taskService.getAllTasks();

        // Then
        assertEquals(expectedTasks, actualTasks);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getTaskById_WhenTaskExists_ShouldReturnTask() {
        // Given
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(sampleTask));

        // When
        Optional<Task> result = taskService.getTaskById(taskId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(sampleTask, result.get());
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void getTaskById_WhenTaskNotExists_ShouldReturnEmpty() {
        // Given
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When
        Optional<Task> result = taskService.getTaskById(taskId);

        // Then
        assertFalse(result.isPresent());
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void createTask_ShouldSaveAndReturnTask() {
        // Given
        Task newTask = new Task("New Task", "New Description", null);
        Task savedTask = new Task("New Task", "New Description", TaskStatus.TODO);
        savedTask.setId(UUID.randomUUID());
        
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // When
        Task result = taskService.createTask(newTask);

        // Then
        assertNotNull(result);
        assertEquals(TaskStatus.TODO, newTask.getStatus()); // Should set default status
        verify(taskRepository, times(1)).save(newTask);
    }

    @Test
    void updateTask_WhenTaskExists_ShouldUpdateAndReturnTask() {
        // Given
        Task updateData = new Task("Updated Title", "Updated Description", TaskStatus.IN_PROGRESS);
        Task existingTask = new Task("Old Title", "Old Description", TaskStatus.TODO);
        existingTask.setId(taskId);
        
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        // When
        Optional<Task> result = taskService.updateTask(taskId, updateData);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Updated Title", existingTask.getTitle());
        assertEquals("Updated Description", existingTask.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, existingTask.getStatus());
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void updateTask_WhenTaskNotExists_ShouldReturnEmpty() {
        // Given
        Task updateData = new Task("Updated Title", "Updated Description", TaskStatus.IN_PROGRESS);
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When
        Optional<Task> result = taskService.updateTask(taskId, updateData);

        // Then
        assertFalse(result.isPresent());
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void deleteTask_WhenTaskExists_ShouldReturnTrue() {
        // Given
        when(taskRepository.existsById(taskId)).thenReturn(true);

        // When
        boolean result = taskService.deleteTask(taskId);

        // Then
        assertTrue(result);
        verify(taskRepository, times(1)).existsById(taskId);
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    void deleteTask_WhenTaskNotExists_ShouldReturnFalse() {
        // Given
        when(taskRepository.existsById(taskId)).thenReturn(false);

        // When
        boolean result = taskService.deleteTask(taskId);

        // Then
        assertFalse(result);
        verify(taskRepository, times(1)).existsById(taskId);
        verify(taskRepository, never()).deleteById(taskId);
    }

    @Test
    void getTasksByStatus_ShouldReturnFilteredTasks() {
        // Given
        List<Task> expectedTasks = Arrays.asList(sampleTask);
        when(taskRepository.findByStatus(TaskStatus.TODO)).thenReturn(expectedTasks);

        // When
        List<Task> result = taskService.getTasksByStatus(TaskStatus.TODO);

        // Then
        assertEquals(expectedTasks, result);
        verify(taskRepository, times(1)).findByStatus(TaskStatus.TODO);
    }

    @Test
    void searchTasksByTitle_ShouldReturnMatchingTasks() {
        // Given
        String searchTerm = "Test";
        List<Task> expectedTasks = Arrays.asList(sampleTask);
        when(taskRepository.findByTitleContainingIgnoreCase(searchTerm)).thenReturn(expectedTasks);

        // When
        List<Task> result = taskService.searchTasksByTitle(searchTerm);

        // Then
        assertEquals(expectedTasks, result);
        verify(taskRepository, times(1)).findByTitleContainingIgnoreCase(searchTerm);
    }
} 