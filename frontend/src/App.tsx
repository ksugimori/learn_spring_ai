import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import TodoListPage from './pages/TodoListPage';

const App: React.FC = () => {
  return (
    <Router>
      <Routes>
        <Route path="/todos" element={<TodoListPage />} />
        <Route path="/" element={<Navigate to="/todos" />} />
        <Route path="*" element={<Navigate to="/todos" />} />
      </Routes>
    </Router>
  );
};

export default App;
