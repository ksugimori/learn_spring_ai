import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import TodoItem from '../TodoItem';
import type { Todo } from '../../types';

describe('TodoItem', () => {
  const mockTodo: Todo = {
    id: 1,
    title: 'Test Todo',
    dueDate: null,
    completed: false,
    userId: 1,
    createdAt: '2024-01-01T00:00:00',
    updatedAt: '2024-01-01T00:00:00',
  };

  it('renders todo title', () => {
    const mockOnToggle = vi.fn();
    const mockOnEdit = vi.fn();
    const mockOnDelete = vi.fn();

    render(
      <TodoItem
        todo={mockTodo}
        userName="Test User"
        onToggle={mockOnToggle}
        onEdit={mockOnEdit}
        onDelete={mockOnDelete}
      />
    );

    expect(screen.getByText('Test Todo')).toBeInTheDocument();
  });

  it('calls onToggle when checkbox is clicked', async () => {
    const mockOnToggle = vi.fn();
    const mockOnEdit = vi.fn();
    const mockOnDelete = vi.fn();

    render(
      <TodoItem
        todo={mockTodo}
        userName="Test User"
        onToggle={mockOnToggle}
        onEdit={mockOnEdit}
        onDelete={mockOnDelete}
      />
    );

    const checkbox = screen.getByRole('checkbox');
    await userEvent.click(checkbox);

    expect(mockOnToggle).toHaveBeenCalledWith(1, 1);
  });

  it('calls onEdit when edit button is clicked', async () => {
    const mockOnToggle = vi.fn();
    const mockOnEdit = vi.fn();
    const mockOnDelete = vi.fn();

    render(
      <TodoItem
        todo={mockTodo}
        userName="Test User"
        onToggle={mockOnToggle}
        onEdit={mockOnEdit}
        onDelete={mockOnDelete}
      />
    );

    const editButton = screen.getByText('編集');
    await userEvent.click(editButton);

    expect(mockOnEdit).toHaveBeenCalledWith(mockTodo);
  });

  it('calls onDelete when delete button is clicked', async () => {
    const mockOnToggle = vi.fn();
    const mockOnEdit = vi.fn();
    const mockOnDelete = vi.fn();

    render(
      <TodoItem
        todo={mockTodo}
        userName="Test User"
        onToggle={mockOnToggle}
        onEdit={mockOnEdit}
        onDelete={mockOnDelete}
      />
    );

    const deleteButton = screen.getByText('削除');
    await userEvent.click(deleteButton);

    expect(mockOnDelete).toHaveBeenCalledWith(1, 1);
  });

  it('displays due date when provided', () => {
    const todoWithDueDate: Todo = {
      ...mockTodo,
      dueDate: '2024-12-31',
    };
    const mockOnToggle = vi.fn();
    const mockOnEdit = vi.fn();
    const mockOnDelete = vi.fn();

    render(
      <TodoItem
        todo={todoWithDueDate}
        userName="Test User"
        onToggle={mockOnToggle}
        onEdit={mockOnEdit}
        onDelete={mockOnDelete}
      />
    );

    expect(screen.getByText(/期限/)).toBeInTheDocument();
  });
});
