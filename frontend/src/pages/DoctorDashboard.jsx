
import React, { useEffect, useState } from 'react';
import { getDoctorDashboard } from '../services/api';

const DoctorDashboard = () => {
  const [dashboardData, setDashboardData] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const token = localStorage.getItem('token');
        if (token) {
          const response = await getDoctorDashboard(token);
          setDashboardData(response.data);
        }
      } catch (error) {
        setError('Failed to fetch dashboard data');
        console.error('Failed to fetch dashboard data', error);
      }
    };

    fetchData();
  }, []);

  if (error) {
    return <p style={{ color: 'red' }}>{error}</p>;
  }

  if (!dashboardData) {
    return <p>Loading...</p>;
  }

  const { doctor, user } = dashboardData;

  return (
    <div className="app">
      <header className="header">
        <h1>Doctor Dashboard</h1>
      </header>
      <div className="container">
        <p>Welcome, {user.firstName} {user.lastName}!</p>
        <h3>Doctor Details</h3>
        <p>Specialization: {doctor.specialization}</p>
        <p>Department: {doctor.department}</p>
        <p>Years of Experience: {doctor.yearsOfExperience}</p>
        <p>Available Hours: {doctor.availableHours}</p>
      </div>
    </div>
  );
};

export default DoctorDashboard;
