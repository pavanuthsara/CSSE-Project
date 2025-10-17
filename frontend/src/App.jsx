import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import DoctorDashboard from './pages/DoctorDashboard';
import StaffDashboard from './pages/StaffDashboard';
import AdminDashboard from './pages/AdminDashboard';
import Dashboard from './pages/Dashboard';
import HospitalDashboard from './pages/kaveesha/HospitalDashboard';
import PatientRegistration from './pages/kaveesha/PatientRegistration';
import './App.css';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/doctor/dashboard" element={<DoctorDashboard />} />
        <Route path="/staff/dashboard" element={<StaffDashboard />} />
        <Route path="/admin/dashboard" element={<AdminDashboard />} />
        <Route path="/hospital" element={<HospitalDashboard />} />
        <Route path="/hospital/register" element={<PatientRegistration />} />
      </Routes>
    </Router>
  );
}

export default App;