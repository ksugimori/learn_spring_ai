export interface User {
  id: number;
  username: string;
}

export interface Todo {
  id: number;
  title: string;
  dueDate: string | null;
  completed: boolean;
  userId: number;
  createdAt: string;
  updatedAt: string;
}

export interface AuthResponse {
  token: string;
  username: string;
  message?: string;
}

export interface ErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path?: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
}

export interface TodoRequest {
  title: string;
  dueDate?: string;
}

export interface TodoUpdateRequest {
  title: string;
  dueDate?: string;
}

export interface TodoFilter {
  completed?: boolean;
  dueDateFrom?: string;
  dueDateTo?: string;
  keyword?: string;
  hasNoDueDate?: boolean;
  sortBy?: string;
  sortDirection?: 'ASC' | 'DESC';
}
