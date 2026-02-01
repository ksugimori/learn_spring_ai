import apiClient from './client';
import type { Todo, TodoRequest, TodoUpdateRequest, TodoFilter } from '../types';

export const todosApi = {
  getAll: async (filter?: TodoFilter): Promise<Todo[]> => {
    const params = new URLSearchParams();

    if (filter?.completed !== undefined) {
      params.append('completed', String(filter.completed));
    }
    if (filter?.dueDateFrom) {
      params.append('dueDateFrom', filter.dueDateFrom);
    }
    if (filter?.dueDateTo) {
      params.append('dueDateTo', filter.dueDateTo);
    }
    if (filter?.keyword) {
      params.append('keyword', filter.keyword);
    }
    if (filter?.hasNoDueDate !== undefined) {
      params.append('hasNoDueDate', String(filter.hasNoDueDate));
    }
    if (filter?.sortBy) {
      params.append('sortBy', filter.sortBy);
    }
    if (filter?.sortDirection) {
      params.append('sortDirection', filter.sortDirection);
    }

    const response = await apiClient.get<Todo[]>('/api/todos', { params });
    return response.data;
  },

  getById: async (id: number): Promise<Todo> => {
    const response = await apiClient.get<Todo>(`/api/todos/${id}`);
    return response.data;
  },

  create: async (data: TodoRequest): Promise<Todo> => {
    const response = await apiClient.post<Todo>('/api/todos', data);
    return response.data;
  },

  update: async (id: number, data: TodoUpdateRequest): Promise<Todo> => {
    const response = await apiClient.put<Todo>(`/api/todos/${id}`, data);
    return response.data;
  },

  toggle: async (id: number): Promise<Todo> => {
    const response = await apiClient.patch<Todo>(`/api/todos/${id}/toggle`);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`/api/todos/${id}`);
  },

  getCompleted: async (): Promise<Todo[]> => {
    const response = await apiClient.get<Todo[]>('/api/todos/completed');
    return response.data;
  },

  getActive: async (): Promise<Todo[]> => {
    const response = await apiClient.get<Todo[]>('/api/todos/active');
    return response.data;
  },

  getOverdue: async (): Promise<Todo[]> => {
    const response = await apiClient.get<Todo[]>('/api/todos/overdue');
    return response.data;
  },

  search: async (keyword: string): Promise<Todo[]> => {
    const response = await apiClient.get<Todo[]>('/api/todos/search', {
      params: { keyword },
    });
    return response.data;
  },
};
