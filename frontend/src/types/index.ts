export interface Todo {
  id: number;
  title: string;
  dueDate: string | null;
  completed: boolean;
  userId: number;
  createdAt: string;
  updatedAt: string;
}

export interface TodoRequest {
  title: string;
  dueDate?: string;
  userId: number;
}

export interface TodoUpdateRequest {
  title: string;
  dueDate?: string;
  userId: number;
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
