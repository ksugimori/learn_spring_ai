export interface User {
  id: number;
  name: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateUserRequest {
  name: string;
}

export interface UpdateUserRequest {
  name: string;
}
