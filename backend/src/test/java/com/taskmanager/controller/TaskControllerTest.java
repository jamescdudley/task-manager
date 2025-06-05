package com.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private Task sampleTask;
    private UUID taskId;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        sampleTask = new Task("Test Task", "Test Description", TaskStatus.TODO);
        sampleTask.setId(taskId);
    }

    @Test
    void getAllTasks_ShouldReturnTaskList() throws Exception {
        // Given
        List<Task> tasks = Arrays.asList(sampleTask);
        when(taskService.getAllTasks()).thenReturn(tasks);

        // When & Then
        mockMvc.perform(get("/tasks"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Test Task")))
                .andExpect(jsonPath("$[0].description", is("Test Description")))
                .andExpect(jsonPath("$[0].status", is("TODO")));
    }

    @Test
    void getAllTasks_WithStatusFilter_ShouldReturnFilteredTasks() throws Exception {
        // Given
        List<Task> todoTasks = Arrays.asList(sampleTask);
        when(taskService.getTasksByStatus(TaskStatus.TODO)).thenReturn(todoTasks);

        // When & Then
        mockMvc.perform(get("/tasks?status=TODO"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status", is("TODO")));
    }

    @Test
    void getTaskById_WhenTaskExists_ShouldReturnTask() throws Exception {
        // Given
        when(taskService.getTaskById(taskId)).thenReturn(Optional.of(sampleTask));

        // When & Then
        mockMvc.perform(get("/tasks/" + taskId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("Test Task")))
                .andExpect(jsonPath("$.description", is("Test Description")))
                .andExpect(jsonPath("$.status", is("TODO")));
    }

    @Test
    void getTaskById_WhenTaskNotExists_ShouldReturn404() throws Exception {
        // Given
        when(taskService.getTaskById(taskId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/tasks/" + taskId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void createTask_WithValidData_ShouldReturnCreatedTask() throws Exception {
        // Given
        Task newTask = new Task("New Task", "New Description", TaskStatus.TODO);
        Task createdTask = new Task("New Task", "New Description", TaskStatus.TODO);
        createdTask.setId(UUID.randomUUID());
        
        when(taskService.createTask(any(Task.class))).thenReturn(createdTask);

        // When & Then
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTask)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("New Task")))
                .andExpect(jsonPath("$.description", is("New Description")))
                .andExpect(jsonPath("$.status", is("TODO")));
    }

    @Test
    void createTask_WithBlankTitle_ShouldReturn400() throws Exception {
        // Given
        Task invalidTask = new Task("", "Description", TaskStatus.TODO);

        // When & Then
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTask)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTask_WhenTaskExists_ShouldReturnUpdatedTask() throws Exception {
        // Given
        Task updateData = new Task("Updated Task", "Updated Description", TaskStatus.IN_PROGRESS);
        Task updatedTask = new Task("Updated Task", "Updated Description", TaskStatus.IN_PROGRESS);
        updatedTask.setId(taskId);
        
        when(taskService.updateTask(eq(taskId), any(Task.class))).thenReturn(Optional.of(updatedTask));

        // When & Then
        mockMvc.perform(put("/tasks/" + taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("Updated Task")))
                .andExpect(jsonPath("$.description", is("Updated Description")))
                .andExpect(jsonPath("$.status", is("IN_PROGRESS")));
    }

    @Test
    void updateTask_WhenTaskNotExists_ShouldReturn404() throws Exception {
        // Given
        Task updateData = new Task("Updated Task", "Updated Description", TaskStatus.IN_PROGRESS);
        when(taskService.updateTask(eq(taskId), any(Task.class))).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(put("/tasks/" + taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTask_WhenTaskExists_ShouldReturn204() throws Exception {
        // Given
        when(taskService.deleteTask(taskId)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/tasks/" + taskId))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTask_WhenTaskNotExists_ShouldReturn404() throws Exception {
        // Given
        when(taskService.deleteTask(taskId)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/tasks/" + taskId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
} 