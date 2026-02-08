import React, { useState, useEffect } from 'react';
import { todosApi } from '../api/todos';
import type { Todo, TodoFilter } from '../types';
import TodoItem from '../components/TodoItem';
import TodoForm from '../components/TodoForm';

const TodoListPage: React.FC = () => {
  const [todos, setTodos] = useState<Todo[]>([]);
  const [editingTodo, setEditingTodo] = useState<Todo | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [keyword, setKeyword] = useState('');
  const [filterCompleted, setFilterCompleted] = useState<boolean | undefined>(undefined);
  const [sortBy, setSortBy] = useState('CREATED_AT');
  const [sortDirection, setSortDirection] = useState<'ASC' | 'DESC'>('DESC');

  const fetchTodos = async () => {
    setLoading(true);
    setError('');

    try {
      const filter: TodoFilter = {
        keyword: keyword || undefined,
        completed: filterCompleted,
        sortBy,
        sortDirection,
      };

      const data = await todosApi.getAll(filter);
      setTodos(data);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Todoの取得に失敗しました');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTodos();
  }, [filterCompleted, sortBy, sortDirection]);

  const handleCreateTodo = async (title: string, dueDate: string | null) => {
    try {
      await todosApi.create({ title, dueDate: dueDate || undefined });
      fetchTodos();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Todoの作成に失敗しました');
    }
  };

  const handleUpdateTodo = async (title: string, dueDate: string | null) => {
    if (!editingTodo) return;

    try {
      await todosApi.update(editingTodo.id, { title, dueDate: dueDate || undefined });
      setEditingTodo(null);
      fetchTodos();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Todoの更新に失敗しました');
    }
  };

  const handleToggleTodo = async (id: number) => {
    try {
      await todosApi.toggle(id);
      fetchTodos();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Todoの更新に失敗しました');
    }
  };

  const handleDeleteTodo = async (id: number) => {
    if (!confirm('本当に削除しますか？')) return;

    try {
      await todosApi.delete(id);
      fetchTodos();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Todoの削除に失敗しました');
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
          todo={editingTodo || undefined}
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
