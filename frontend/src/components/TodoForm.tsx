import React, { useState, useEffect } from 'react';
import type { Todo } from '../types';
import type { User } from '../types/user';

interface TodoFormProps {
  todo?: Todo;
  users: User[];
  onSubmit: (title: string, dueDate: string | null, userId: number) => void;
  onCancel: () => void;
}

const TodoForm: React.FC<TodoFormProps> = ({ todo, users, onSubmit, onCancel }) => {
  const [title, setTitle] = useState('');
  const [dueDate, setDueDate] = useState('');
  const [userId, setUserId] = useState<number | ''>('');

  useEffect(() => {
    if (todo) {
      setTitle(todo.title);
      setDueDate(todo.dueDate || '');
      setUserId(todo.userId);
    } else {
      setTitle('');
      setDueDate('');
      setUserId(users.length > 0 ? users[0].id : '');
    }
  }, [todo, users]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (userId === '') {
      alert('ユーザーを選択してください');
      return;
    }
    onSubmit(title, dueDate || null, userId);
    setTitle('');
    setDueDate('');
    setUserId(users.length > 0 ? users[0].id : '');
  };

  return (
    <form onSubmit={handleSubmit} style={styles.form}>
      <div style={styles.formGroup}>
        <label htmlFor="userId" style={styles.label}>
          ユーザー
        </label>
        <select
          id="userId"
          value={userId}
          onChange={(e) => setUserId(Number(e.target.value))}
          required
          style={styles.select}
        >
          <option value="">選択してください</option>
          {users.map((user) => (
            <option key={user.id} value={user.id}>
              {user.name}
            </option>
          ))}
        </select>
      </div>

      <div style={styles.formGroup}>
        <label htmlFor="title" style={styles.label}>
          タイトル
        </label>
        <input
          id="title"
          type="text"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          required
          style={styles.input}
          placeholder="Todoを入力..."
        />
      </div>

      <div style={styles.formGroup}>
        <label htmlFor="dueDate" style={styles.label}>
          期限
        </label>
        <input
          id="dueDate"
          type="date"
          value={dueDate}
          onChange={(e) => setDueDate(e.target.value)}
          style={styles.input}
        />
      </div>

      <div style={styles.actions}>
        <button type="submit" style={styles.submitButton}>
          {todo ? '更新' : '追加'}
        </button>
        {todo && (
          <button type="button" onClick={onCancel} style={styles.cancelButton}>
            キャンセル
          </button>
        )}
      </div>
    </form>
  );
};

const styles: { [key: string]: React.CSSProperties } = {
  form: {
    display: 'flex',
    flexDirection: 'column',
    gap: '1rem',
    padding: '1rem',
    backgroundColor: 'white',
    borderRadius: '4px',
    boxShadow: '0 1px 3px rgba(0, 0, 0, 0.1)',
    marginBottom: '1rem',
  },
  formGroup: {
    display: 'flex',
    flexDirection: 'column',
    gap: '0.5rem',
  },
  label: {
    fontWeight: 'bold',
    color: '#555',
  },
  input: {
    padding: '0.75rem',
    border: '1px solid #ddd',
    borderRadius: '4px',
    fontSize: '1rem',
  },
  select: {
    padding: '0.75rem',
    border: '1px solid #ddd',
    borderRadius: '4px',
    fontSize: '1rem',
    backgroundColor: 'white',
  },
  actions: {
    display: 'flex',
    gap: '0.5rem',
  },
  submitButton: {
    padding: '0.75rem 1.5rem',
    backgroundColor: '#28a745',
    color: 'white',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
  },
  cancelButton: {
    padding: '0.75rem 1.5rem',
    backgroundColor: '#6c757d',
    color: 'white',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
  },
};

export default TodoForm;
