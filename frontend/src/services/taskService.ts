import axios from 'axios';
import { Task, CreateTaskRequest, UpdateTaskRequest, TaskStatus } from '../types/Task';

// Configure the base URL for API calls
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// API service functions
export const taskService = {
  // Get all tasks
  getAllTasks: async (): Promise<Task[]> => {
    const response = await api.get<Task[]>('/tasks');
    return response.data;
  },

  // Get tasks by status
  getTasksByStatus: async (status: TaskStatus): Promise<Task[]> => {
    const response = await api.get<Task[]>(`/tasks?status=${status}`);
    return response.data;
  },

  // Get a single task by ID
  getTaskById: async (id: string): Promise<Task> => {
    const response = await api.get<Task>(`/tasks/${id}`);
    return response.data;
  },

  // Create a new task
  createTask: async (task: CreateTaskRequest): Promise<Task> => {
    const response = await api.post<Task>('/tasks', task);
    return response.data;
  },

  // Update an existing task
  updateTask: async (id: string, task: UpdateTaskRequest): Promise<Task> => {
    const response = await api.put<Task>(`/tasks/${id}`, task);
    return response.data;
  },

  // Delete a task
  deleteTask: async (id: string): Promise<void> => {
    await api.delete(`/tasks/${id}`);
  },

  // Search tasks by title
  searchTasks: async (searchTerm: string): Promise<Task[]> => {
    const response = await api.get<Task[]>(`/tasks?search=${encodeURIComponent(searchTerm)}`);
    return response.data;
  },
};

export default taskService; 