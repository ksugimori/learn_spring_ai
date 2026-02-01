import React from 'react';
import type { Todo } from '../types';

interface TodoItemProps {
  todo: Todo;
  onToggle: (id: number) => void;
  onEdit: (todo: Todo) => void;
  onDelete: (id: number) => void;
}

const TodoItem: React.FC<TodoItemProps> = ({ todo, onToggle, onEdit, onDelete }) => {
  const formatDate = (dateString: string | null) => {
    if (!dateString) return '';
    return new Date(dateString).toLocaleDateString('ja-JP');
  };

  const isOverdue = () => {
    if (!todo.dueDate || todo.completed) return false;
    return new Date(todo.dueDate) < new Date();
  };

  return (
    <div style={{
      ...styles.container,
      ...(todo.completed ? styles.completed : {}),
      ...(isOverdue() ? styles.overdue : {})
    }}>
      <div style={styles.content}>
        <input
          type="checkbox"
          checked={todo.completed}
          onChange={() => onToggle(todo.id)}
          style={styles.checkbox}
        />
        <div style={styles.details}>
          <span style={todo.completed ? styles.titleCompleted : styles.title}>
            {todo.title}
          </span>
          {todo.dueDate && (
            <span style={styles.dueDate}>
              期限: {formatDate(todo.dueDate)}
              {isOverdue() && ' (期限切れ)'}
            </span>
          )}
        </div>
      </div>
      <div style={styles.actions}>
        <button onClick={() => onEdit(todo)} style={styles.editButton}>
          編集
        </button>
        <button onClick={() => onDelete(todo.id)} style={styles.deleteButton}>
          削除
        </button>
      </div>
    </div>
  );
};

const styles: { [key: string]: React.CSSProperties } = {
  container: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: '1rem',
    backgroundColor: 'white',
    borderRadius: '4px',
    marginBottom: '0.5rem',
    boxShadow: '0 1px 3px rgba(0, 0, 0, 0.1)',
  },
  completed: {
    backgroundColor: '#f0f0f0',
  },
  overdue: {
    borderLeft: '4px solid #dc3545',
  },
  content: {
    display: 'flex',
    alignItems: 'center',
    gap: '1rem',
    flex: 1,
  },
  checkbox: {
    width: '20px',
    height: '20px',
    cursor: 'pointer',
  },
  details: {
    display: 'flex',
    flexDirection: 'column',
    gap: '0.25rem',
  },
  title: {
    fontSize: '1rem',
    color: '#333',
  },
  titleCompleted: {
    fontSize: '1rem',
    color: '#999',
    textDecoration: 'line-through',
  },
  dueDate: {
    fontSize: '0.875rem',
    color: '#666',
  },
  actions: {
    display: 'flex',
    gap: '0.5rem',
  },
  editButton: {
    padding: '0.5rem 1rem',
    backgroundColor: '#007bff',
    color: 'white',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
  },
  deleteButton: {
    padding: '0.5rem 1rem',
    backgroundColor: '#dc3545',
    color: 'white',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
  },
};

export default TodoItem;
