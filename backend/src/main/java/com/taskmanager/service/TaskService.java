package com.taskmanager.service;

import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {
    
    private final TaskRepository taskRepository;
    
    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    
    public Optional<Task> getTaskById(UUID id) {
        return taskRepository.findById(id);
    }
    
    public Task createTask(Task task) {
        // Ensure status is set to TODO if not provided
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.TODO);
        }
        return taskRepository.save(task);
    }
    
    public Optional<Task> updateTask(UUID id, Task taskUpdate) {
        return taskRepository.findById(id)
                .map(existingTask -> {
                    existingTask.setTitle(taskUpdate.getTitle());
                    existingTask.setDescription(taskUpdate.getDescription());
                    existingTask.setStatus(taskUpdate.getStatus());
                    return taskRepository.save(existingTask);
                });
    }
    
    public boolean deleteTask(UUID id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }
    
    public List<Task> searchTasksByTitle(String title) {
        return taskRepository.findByTitleContainingIgnoreCase(title);
    }
} 