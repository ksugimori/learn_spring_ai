import apiClient from './client';
import type { User, CreateUserRequest, UpdateUserRequest } from '../types/user';

export const usersApi = {
  getAll: async (): Promise<User[]> => {
    const response = await apiClient.get<User[]>('/api/users');
    return response.data;
  },

  getById: async (id: number): Promise<User> => {
    const response = await apiClient.get<User>(`/api/users/${id}`);
    return response.data;
  },

  create: async (request: CreateUserRequest): Promise<User> => {
    const response = await apiClient.post<User>('/api/users', request);
    return response.data;
  },

  update: async (id: number, request: UpdateUserRequest): Promise<User> => {
    const response = await apiClient.put<User>(`/api/users/${id}`, request);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`/api/users/${id}`);
  },
};
