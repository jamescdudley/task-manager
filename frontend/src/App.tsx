import React, { useState, useEffect } from 'react';
import { Task, TaskStatus, CreateTaskRequest, UpdateTaskRequest } from './types/Task';
import { taskService } from './services/taskService';
import TaskList from './components/TaskList';
import TaskForm from './components/TaskForm';
import './App.css';

function App() {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [showForm, setShowForm] = useState(false);
  const [editingTask, setEditingTask] = useState<Task | null>(null);
  const [statusFilter, setStatusFilter] = useState<TaskStatus | 'ALL'>('ALL');
  const [searchTerm, setSearchTerm] = useState('');

  // Load tasks on component mount
  useEffect(() => {
    loadTasks();
  }, []);

  const loadTasks = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await taskService.getAllTasks();
      setTasks(data);
    } catch (err) {
      setError('Failed to load tasks. Please check if the backend is running.');
      console.error('Error loading tasks:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateTask = async (taskData: CreateTaskRequest) => {
    try {
      const newTask = await taskService.createTask(taskData);
      setTasks(prev => [...prev, newTask]);
      setShowForm(false);
      setError(null);
    } catch (err) {
      setError('Failed to create task');
      console.error('Error creating task:', err);
    }
  };

  const handleUpdateTask = async (taskData: UpdateTaskRequest) => {
    if (!editingTask?.id) return;
    
    try {
      const updatedTask = await taskService.updateTask(editingTask.id, taskData);
      setTasks(prev => prev.map(task => 
        task.id === editingTask.id ? updatedTask : task
      ));
      setEditingTask(null);
      setShowForm(false);
      setError(null);
    } catch (err) {
      setError('Failed to update task');
      console.error('Error updating task:', err);
    }
  };

  const handleDeleteTask = async (id: string) => {
    if (!window.confirm('Are you sure you want to delete this task?')) {
      return;
    }

    try {
      await taskService.deleteTask(id);
      setTasks(prev => prev.filter(task => task.id !== id));
      setError(null);
    } catch (err) {
      setError('Failed to delete task');
      console.error('Error deleting task:', err);
    }
  };

  const handleMarkAsDone = async (id: string) => {
    const task = tasks.find(t => t.id === id);
    if (!task) return;

    try {
      const updatedTask = await taskService.updateTask(id, {
        ...task,
        status: TaskStatus.DONE
      });
      setTasks(prev => prev.map(t => t.id === id ? updatedTask : t));
      setError(null);
    } catch (err) {
      setError('Failed to mark task as done');
      console.error('Error marking task as done:', err);
    }
  };

  const handleEditTask = (task: Task) => {
    setEditingTask(task);
    setShowForm(true);
  };

  const handleCloseForm = () => {
    setShowForm(false);
    setEditingTask(null);
  };

  const handleSearch = async () => {
    if (!searchTerm.trim()) {
      loadTasks();
      return;
    }

    try {
      setLoading(true);
      const data = await taskService.searchTasks(searchTerm);
      setTasks(data);
      setError(null);
    } catch (err) {
      setError('Failed to search tasks');
      console.error('Error searching tasks:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleFormSubmit = async (taskData: CreateTaskRequest | UpdateTaskRequest) => {
    if (editingTask) {
      // For editing, we know it's an UpdateTaskRequest
      await handleUpdateTask(taskData as UpdateTaskRequest);
    } else {
      // For creating, we know it's a CreateTaskRequest
      await handleCreateTask(taskData as CreateTaskRequest);
    }
  };

  const filteredTasks = statusFilter === 'ALL' 
    ? tasks 
    : tasks.filter(task => task.status === statusFilter);

  const getTaskCounts = () => {
    return {
      total: tasks.length,
      todo: tasks.filter(t => t.status === TaskStatus.TODO).length,
      inProgress: tasks.filter(t => t.status === TaskStatus.IN_PROGRESS).length,
      done: tasks.filter(t => t.status === TaskStatus.DONE).length,
    };
  };

  const counts = getTaskCounts();

  return (
    <div className="app">
      <header className="app-header">
        <h1>📋 Task Manager</h1>
        <p>Organize your tasks efficiently</p>
      </header>

      <main className="app-main">
        {error && (
          <div className="error-banner">
            <span>⚠️ {error}</span>
            <button onClick={() => setError(null)}>×</button>
          </div>
        )}

        <div className="controls">
          <div className="search-section">
            <div className="search-bar">
              <input
                type="text"
                placeholder="Search tasks..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
              />
              <button onClick={handleSearch} className="search-btn">
                🔍
              </button>
              {searchTerm && (
                <button 
                  onClick={() => {
                    setSearchTerm('');
                    loadTasks();
                  }} 
                  className="clear-search-btn"
                >
                  ✕
                </button>
              )}
            </div>
          </div>

          <div className="filter-section">
            <label>Filter by status:</label>
            <select 
              value={statusFilter} 
              onChange={(e) => setStatusFilter(e.target.value as TaskStatus | 'ALL')}
            >
              <option value="ALL">All Tasks ({counts.total})</option>
              <option value={TaskStatus.TODO}>To Do ({counts.todo})</option>
              <option value={TaskStatus.IN_PROGRESS}>In Progress ({counts.inProgress})</option>
              <option value={TaskStatus.DONE}>Done ({counts.done})</option>
            </select>
          </div>

          <button 
            onClick={() => setShowForm(true)} 
            className="add-task-btn"
          >
            ➕ Add New Task
          </button>
        </div>

        <div className="task-stats">
          <div className="stat-card">
            <span className="stat-number">{counts.total}</span>
            <span className="stat-label">Total</span>
          </div>
          <div className="stat-card todo">
            <span className="stat-number">{counts.todo}</span>
            <span className="stat-label">To Do</span>
          </div>
          <div className="stat-card in-progress">
            <span className="stat-number">{counts.inProgress}</span>
            <span className="stat-label">In Progress</span>
          </div>
          <div className="stat-card done">
            <span className="stat-number">{counts.done}</span>
            <span className="stat-label">Done</span>
          </div>
        </div>

        {loading ? (
          <div className="loading">
            <div className="spinner"></div>
            <p>Loading tasks...</p>
          </div>
        ) : (
          <TaskList
            tasks={filteredTasks}
            onMarkAsDone={handleMarkAsDone}
            onDeleteTask={handleDeleteTask}
            onEditTask={handleEditTask}
          />
        )}

        {showForm && (
          <TaskForm
            task={editingTask || undefined}
            onSubmit={handleFormSubmit}
            onCancel={handleCloseForm}
            isEditing={!!editingTask}
          />
        )}
      </main>
    </div>
  );
}

export default App;
