import React, { useState, useEffect, useCallback } from 'react';
import { isAxiosError } from 'axios';
import { todosApi } from '../api/todos';
import { usersApi } from '../api/users';
import type { Todo, TodoFilter } from '../types';
import type { User } from '../types/user';
import TodoItem from '../components/TodoItem';
import TodoForm from '../components/TodoForm';

const TodoListPage: React.FC = () => {
  const [todos, setTodos] = useState<Todo[]>([]);
  const [users, setUsers] = useState<User[]>([]);
  const [editingTodo, setEditingTodo] = useState<Todo | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [keyword, setKeyword] = useState('');
  const [filterCompleted, setFilterCompleted] = useState<boolean | undefined>(undefined);
  const [filterUserId, setFilterUserId] = useState<number | undefined>(undefined);
  const [sortBy, setSortBy] = useState('CREATED_AT');
  const [sortDirection, setSortDirection] = useState<'ASC' | 'DESC'>('DESC');

  const fetchUsers = useCallback(async () => {
    try {
      const data = await usersApi.getAll();
      setUsers(data);
    } catch (err: unknown) {
      if (isAxiosError(err)) {
        setError(err.response?.data?.message || 'ユーザーの取得に失敗しました');
      } else {
        setError('ユーザーの取得に失敗しました');
      }
    }
  }, []);

  const fetchTodos = useCallback(async () => {
    setLoading(true);
    setError('');

    try {
      const filter: TodoFilter = {
        keyword: keyword || undefined,
        completed: filterCompleted,
        sortBy,
        sortDirection,
      };

      const data = await todosApi.getAll(filter, filterUserId);
      setTodos(data);
    } catch (err: unknown) {
      if (isAxiosError(err)) {
        setError(err.response?.data?.message || 'Todoの取得に失敗しました');
      } else {
        setError('Todoの取得に失敗しました');
      }
    } finally {
      setLoading(false);
    }
  }, [keyword, filterCompleted, filterUserId, sortBy, sortDirection]);

  useEffect(() => {
    fetchUsers();
  }, [fetchUsers]);

  useEffect(() => {
    fetchTodos();
  }, [fetchTodos]);

  const handleCreateTodo = async (title: string, dueDate: string | null, userId: number) => {
    try {
      await todosApi.create({ title, dueDate: dueDate || undefined, userId });
      fetchTodos();
    } catch (err: unknown) {
      if (isAxiosError(err)) {
        setError(err.response?.data?.message || 'Todoの作成に失敗しました');
      } else {
        setError('Todoの作成に失敗しました');
      }
    }
  };

  const handleUpdateTodo = async (title: string, dueDate: string | null, userId: number) => {
    if (!editingTodo) return;

    try {
      await todosApi.update(editingTodo.id, { title, dueDate: dueDate || undefined, userId });
      setEditingTodo(null);
      fetchTodos();
    } catch (err: unknown) {
      if (isAxiosError(err)) {
        setError(err.response?.data?.message || 'Todoの更新に失敗しました');
      } else {
        setError('Todoの更新に失敗しました');
      }
    }
  };

  const handleToggleTodo = async (id: number, userId: number) => {
    try {
      await todosApi.toggle(id, userId);
      fetchTodos();
    } catch (err: unknown) {
      if (isAxiosError(err)) {
        setError(err.response?.data?.message || 'Todoの更新に失敗しました');
      } else {
        setError('Todoの更新に失敗しました');
      }
    }
  };

  const handleDeleteTodo = async (id: number, userId: number) => {
    if (!confirm('本当に削除しますか？')) return;

    try {
      await todosApi.delete(id, userId);
      fetchTodos();
    } catch (err: unknown) {
      if (isAxiosError(err)) {
        setError(err.response?.data?.message || 'Todoの削除に失敗しました');
      } else {
        setError('Todoの削除に失敗しました');
      }
    }
  };

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    fetchTodos();
  };

  return (
    <div style={styles.container}>
      <header style={styles.header}>
        <h1>Todo App</h1>
      </header>

      <div style={styles.content}>
        {error && <div style={styles.error}>{error}</div>}

        <TodoForm
          key={editingTodo?.id || 'new'}
          todo={editingTodo || undefined}
          users={users}
          onSubmit={editingTodo ? handleUpdateTodo : handleCreateTodo}
          onCancel={() => setEditingTodo(null)}
        />

        <div style={styles.filters}>
          <form onSubmit={handleSearch} style={styles.searchForm}>
            <input
              type="text"
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
              placeholder="検索..."
              style={styles.searchInput}
            />
            <button type="submit" style={styles.searchButton}>
              検索
            </button>
          </form>

          <div style={styles.filterGroup}>
            <label>ユーザー:</label>
            <select
              value={filterUserId === undefined ? 'all' : filterUserId.toString()}
              onChange={(e) => {
                const value = e.target.value;
                setFilterUserId(value === 'all' ? undefined : Number(value));
              }}
              style={styles.select}
            >
              <option value="all">すべてのユーザー</option>
              {users.map((user) => (
                <option key={user.id} value={user.id}>
                  {user.name}
                </option>
              ))}
            </select>
          </div>

          <div style={styles.filterGroup}>
            <label>フィルタ:</label>
            <select
              value={filterCompleted === undefined ? 'all' : filterCompleted.toString()}
              onChange={(e) => {
                const value = e.target.value;
                setFilterCompleted(value === 'all' ? undefined : value === 'true');
              }}
              style={styles.select}
            >
              <option value="all">すべて</option>
              <option value="false">未完了</option>
              <option value="true">完了済み</option>
            </select>
          </div>

          <div style={styles.filterGroup}>
            <label>並び順:</label>
            <select
              value={sortBy}
              onChange={(e) => setSortBy(e.target.value)}
              style={styles.select}
            >
              <option value="CREATED_AT">作成日時</option>
              <option value="UPDATED_AT">更新日時</option>
              <option value="DUE_DATE">期限</option>
              <option value="TITLE">タイトル</option>
            </select>
            <select
              value={sortDirection}
              onChange={(e) => setSortDirection(e.target.value as 'ASC' | 'DESC')}
              style={styles.select}
            >
              <option value="DESC">降順</option>
              <option value="ASC">昇順</option>
            </select>
          </div>
        </div>

        {loading ? (
          <div style={styles.loading}>読み込み中...</div>
        ) : (
          <div style={styles.todoList}>
            {todos.length === 0 ? (
              <div style={styles.empty}>Todoがありません</div>
            ) : (
              todos.map((todo) => (
                <TodoItem
                  key={todo.id}
                  todo={todo}
                  userName={users.find((u) => u.id === todo.userId)?.name || 'Unknown'}
                  onToggle={handleToggleTodo}
                  onEdit={setEditingTodo}
                  onDelete={handleDeleteTodo}
                />
              ))
            )}
          </div>
        )}
      </div>
    </div>
  );
};

const styles: { [key: string]: React.CSSProperties } = {
  container: {
    minHeight: '100vh',
    backgroundColor: '#f5f5f5',
  },
  header: {
    backgroundColor: '#007bff',
    color: 'white',
    padding: '1rem 2rem',
  },
  content: {
    maxWidth: '800px',
    margin: '0 auto',
    padding: '2rem',
  },
  error: {
    padding: '1rem',
    backgroundColor: '#f8d7da',
    color: '#721c24',
    border: '1px solid #f5c6cb',
    borderRadius: '4px',
    marginBottom: '1rem',
  },
  filters: {
    display: 'flex',
    gap: '1rem',
    marginBottom: '1rem',
    flexWrap: 'wrap',
    alignItems: 'center',
  },
  searchForm: {
    display: 'flex',
    gap: '0.5rem',
    flex: 1,
  },
  searchInput: {
    flex: 1,
    padding: '0.5rem',
    border: '1px solid #ddd',
    borderRadius: '4px',
  },
  searchButton: {
    padding: '0.5rem 1rem',
    backgroundColor: '#007bff',
    color: 'white',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
  },
  filterGroup: {
    display: 'flex',
    gap: '0.5rem',
    alignItems: 'center',
  },
  select: {
    padding: '0.5rem',
    border: '1px solid #ddd',
    borderRadius: '4px',
  },
  todoList: {
    display: 'flex',
    flexDirection: 'column',
  },
  loading: {
    textAlign: 'center',
    padding: '2rem',
    color: '#666',
  },
  empty: {
    textAlign: 'center',
    padding: '2rem',
    color: '#666',
  },
};

export default TodoListPage;
