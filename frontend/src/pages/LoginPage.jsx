
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../services/api';

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await login({ username, password });
      const { token, role } = response.data;
      localStorage.setItem('token', token);
      localStorage.setItem('role', role);

      if (role === 'DOCTOR') {
        navigate('/doctor/dashboard');
      } else if (role === 'STAFF') {
        navigate('/staff/dashboard');
      } else if (role === 'ADMIN') {
        navigate('/admin/dashboard');
      } else if (role === 'PATIENT') {
        navigate('/patient/dashboard');
      } else {
        navigate('/dashboard');
      }
    } catch (error) {
      setError('Invalid username or password');
      console.error('Login failed', error);
    }
  };

  return (
    <div className="app">
      <header className="header">
        <h1>Login</h1>
      </header>
      <div className="container">
        {error && <p style={{ color: 'red' }}>{error}</p>}
        <form onSubmit={handleLogin}>
          <div>
            <label>Username:</label>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
          </div>
          <div>
            <label>Password:</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>
          <button type="submit">Login</button>
        </form>
        <div style={{ marginTop: '20px', textAlign: 'center' }}>
          <p>Don't have an account?</p>
          <a href="/patient/register" style={{ color: '#007bff', textDecoration: 'none' }}>
            Register as Patient
          </a>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
