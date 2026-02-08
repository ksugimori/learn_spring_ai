import React from 'react';
import type { User } from '../types/user';

interface UserListProps {
  users: User[];
  onEdit: (user: User) => void;
  onDelete: (id: number) => void;
}

const UserList: React.FC<UserListProps> = ({ users, onEdit, onDelete }) => {
  const handleDelete = (user: User) => {
    if (confirm(`ユーザー「${user.name}」を削除しますか？`)) {
      onDelete(user.id);
    }
  };

  if (users.length === 0) {
    return <div style={styles.empty}>ユーザーがいません</div>;
  }

  return (
    <div style={styles.container}>
      <h2>ユーザー一覧</h2>
      <div style={styles.list}>
        {users.map((user) => (
          <div key={user.id} style={styles.item}>
            <div style={styles.info}>
              <strong>{user.name}</strong>
              <span style={styles.id}>ID: {user.id}</span>
            </div>
            <div style={styles.actions}>
              <button onClick={() => onEdit(user)} style={styles.editButton}>
                編集
              </button>
              <button onClick={() => handleDelete(user)} style={styles.deleteButton}>
                削除
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

const styles: { [key: string]: React.CSSProperties } = {
  container: {
    marginTop: '2rem',
  },
  list: {
    display: 'flex',
    flexDirection: 'column',
    gap: '0.5rem',
  },
  item: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: '1rem',
    backgroundColor: 'white',
    border: '1px solid #ddd',
    borderRadius: '4px',
  },
  info: {
    display: 'flex',
    flexDirection: 'column',
    gap: '0.25rem',
  },
  id: {
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
  empty: {
    textAlign: 'center',
    padding: '2rem',
    color: '#666',
  },
};

export default UserList;
