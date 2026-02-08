import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, Link } from 'react-router-dom';
import TodoListPage from './pages/TodoListPage';
import UsersPage from './pages/UsersPage';

const Navigation: React.FC = () => {
  return (
    <nav style={styles.nav}>
      <Link to="/todos" style={styles.link}>
        TODO管理
      </Link>
      <Link to="/users" style={styles.link}>
        ユーザー管理
      </Link>
    </nav>
  );
};

const App: React.FC = () => {
  return (
    <Router>
      <div>
        <Navigation />
        <Routes>
          <Route path="/todos" element={<TodoListPage />} />
          <Route path="/users" element={<UsersPage />} />
          <Route path="/" element={<Navigate to="/todos" />} />
          <Route path="*" element={<Navigate to="/todos" />} />
        </Routes>
      </div>
    </Router>
  );
};

const styles: { [key: string]: React.CSSProperties } = {
  nav: {
    backgroundColor: '#343a40',
    padding: '1rem 2rem',
    display: 'flex',
    gap: '1rem',
  },
  link: {
    color: 'white',
    textDecoration: 'none',
    padding: '0.5rem 1rem',
    borderRadius: '4px',
    transition: 'background-color 0.2s',
  },
};

export default App;
