import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import PatientRegistrationPage from './pages/PatientRegistrationPage';
import PatientDashboard from './pages/PatientDashboard';
import AppointmentBookingPage from './pages/AppointmentBookingPage';
import DoctorDashboard from './pages/DoctorDashboard';
import StaffDashboard from './pages/StaffDashboard';
import AdminDashboard from './pages/AdminDashboard';
import Dashboard from './pages/Dashboard';
import './App.css';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/patient/register" element={<PatientRegistrationPage />} />
        <Route path="/patient/dashboard" element={<PatientDashboard />} />
        <Route path="/book-appointment" element={<AppointmentBookingPage />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/doctor/dashboard" element={<DoctorDashboard />} />
        <Route path="/staff/dashboard" element={<StaffDashboard />} />
        <Route path="/admin/dashboard" element={<AdminDashboard />} />
      </Routes>
    </Router>
  );
}

export default App;