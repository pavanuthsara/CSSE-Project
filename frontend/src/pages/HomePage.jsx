import React from 'react';
import { Link } from 'react-router-dom';

const HomePage = () => {
  return (
    <div className="app">
      <header className="header">
        <h1>Hospital Management System</h1>
      </header>
      <div className="container">
        <h2>Welcome</h2>
        <p>Please login or register to continue.</p>
        <nav className="nav">
          <Link to="/login">
            <button>Login</button>
          </Link>
          <Link to="/register">
            <button>Register</button>
          </Link>
        </nav>
      </div>
    </div>
  );
};

export default HomePage;