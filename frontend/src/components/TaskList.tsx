import React from 'react';
import { Task, TaskStatus } from '../types/Task';
import './TaskList.css';

interface TaskListProps {
  tasks: Task[];
  onMarkAsDone: (id: string) => void;
  onDeleteTask: (id: string) => void;
  onEditTask: (task: Task) => void;
}

const TaskList: React.FC<TaskListProps> = ({ 
  tasks, 
  onMarkAsDone, 
  onDeleteTask, 
  onEditTask 
}) => {
  const getStatusColor = (status: TaskStatus): string => {
    switch (status) {
      case TaskStatus.TODO:
        return '#ff6b6b';
      case TaskStatus.IN_PROGRESS:
        return '#4ecdc4';
      case TaskStatus.DONE:
        return '#95e1d3';
      default:
        return '#999';
    }
  };

  const getStatusText = (status: TaskStatus): string => {
    switch (status) {
      case TaskStatus.TODO:
        return 'To Do';
      case TaskStatus.IN_PROGRESS:
        return 'In Progress';
      case TaskStatus.DONE:
        return 'Done';
      default:
        return status;
    }
  };

  if (tasks.length === 0) {
    return (
      <div className="task-list-empty">
        <p>No tasks available. Create your first task!</p>
      </div>
    );
  }

  return (
    <div className="task-list">
      {tasks.map((task) => (
        <div key={task.id} className={`task-item ${task.status.toLowerCase()}`}>
          <div className="task-content">
            <div className="task-header">
              <h3 className="task-title">{task.title}</h3>
              <span 
                className="task-status"
                style={{ backgroundColor: getStatusColor(task.status) }}
              >
                {getStatusText(task.status)}
              </span>
            </div>
            {task.description && (
              <p className="task-description">{task.description}</p>
            )}
          </div>
          <div className="task-actions">
            {task.status !== TaskStatus.DONE && (
              <button
                className="btn btn-success"
                onClick={() => task.id && onMarkAsDone(task.id)}
                title="Mark as Done"
              >
                ✓ Done
              </button>
            )}
            <button
              className="btn btn-secondary"
              onClick={() => onEditTask(task)}
              title="Edit Task"
            >
              ✏️ Edit
            </button>
            <button
              className="btn btn-danger"
              onClick={() => task.id && onDeleteTask(task.id)}
              title="Delete Task"
            >
              🗑️ Delete
            </button>
          </div>
        </div>
      ))}
    </div>
  );
};

export default TaskList; 