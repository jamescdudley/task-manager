export enum TaskStatus {
  TODO = 'TODO',
  IN_PROGRESS = 'IN_PROGRESS', 
  DONE = 'DONE'
}

export interface Task {
  id?: string;
  title: string;
  description?: string;
  status: TaskStatus;
}

export interface CreateTaskRequest {
  title: string;
  description?: string;
  status?: TaskStatus;
}

export interface UpdateTaskRequest {
  title: string;
  description?: string;
  status: TaskStatus;
} 