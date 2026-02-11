import React, { useState, useEffect, useCallback } from 'react';
import { isAxiosError } from 'axios';
import { usersApi } from '../api/users';
import type { User } from '../types/user';
import UserForm from '../components/UserForm';
import UserList from '../components/UserList';

const UsersPage: React.FC = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [editingUser, setEditingUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const fetchUsers = useCallback(async () => {
    setLoading(true);
    setError('');

    try {
      const data = await usersApi.getAll();
      setUsers(data);
    } catch (err: unknown) {
      if (isAxiosError(err)) {
        setError(err.response?.data?.message || 'ユーザーの取得に失敗しました');
      } else {
        setError('ユーザーの取得に失敗しました');
      }
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchUsers();
  }, [fetchUsers]);

  const handleCreateUser = async (name: string) => {
    try {
      await usersApi.create({ name });
      fetchUsers();
      setError('');
    } catch (err: unknown) {
      if (isAxiosError(err)) {
        if (err.response?.status === 409) {
          setError('このユーザー名は既に使用されています');
        } else {
          setError(err.response?.data?.message || 'ユーザーの作成に失敗しました');
        }
      } else {
        setError('ユーザーの作成に失敗しました');
      }
    }
  };

  const handleUpdateUser = async (name: string) => {
    if (!editingUser) return;

    try {
      await usersApi.update(editingUser.id, { name });
      setEditingUser(null);
      fetchUsers();
      setError('');
    } catch (err: unknown) {
      if (isAxiosError(err)) {
        if (err.response?.status === 409) {
          setError('このユーザー名は既に使用されています');
        } else {
          setError(err.response?.data?.message || 'ユーザーの更新に失敗しました');
        }
      } else {
        setError('ユーザーの更新に失敗しました');
      }
    }
  };

  const handleDeleteUser = async (id: number) => {
    try {
      await usersApi.delete(id);
      fetchUsers();
      setError('');
    } catch (err: unknown) {
      if (isAxiosError(err)) {
        if (err.response?.status === 400) {
          setError('このユーザーにはTODOが存在するため削除できません');
        } else {
          setError(err.response?.data?.message || 'ユーザーの削除に失敗しました');
        }
      } else {
        setError('ユーザーの削除に失敗しました');
      }
    }
  };

  return (
    <div style={styles.container}>
      <header style={styles.header}>
        <h1>ユーザー管理</h1>
      </header>

      <div style={styles.content}>
        {error && <div style={styles.error}>{error}</div>}

        <UserForm
          key={editingUser?.id || 'new'}
          user={editingUser || undefined}
          onSubmit={editingUser ? handleUpdateUser : handleCreateUser}
          onCancel={() => setEditingUser(null)}
        />

        {loading ? (
          <div style={styles.loading}>読み込み中...</div>
        ) : (
          <UserList users={users} onEdit={setEditingUser} onDelete={handleDeleteUser} />
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
  loading: {
    textAlign: 'center',
    padding: '2rem',
    color: '#666',
  },
};

export default UsersPage;
