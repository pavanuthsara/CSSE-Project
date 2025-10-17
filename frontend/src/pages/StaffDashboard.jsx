
import React, { useEffect, useState } from 'react';
import { getStaffDashboard } from '../services/api';

const StaffDashboard = () => {
  const [dashboardData, setDashboardData] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const token = localStorage.getItem('token');
        if (token) {
          const response = await getStaffDashboard(token);
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

  const { staff, user } = dashboardData;

  return (
    <div className="app">
      <header className="header">
        <h1>Staff Dashboard</h1>
      </header>
      <div className="container">
        <p>Welcome, {user.firstName} {user.lastName}!</p>
        <h3>Staff Details</h3>
        <p>Position: {staff.position}</p>
        <p>Department: {staff.department}</p>
        <p>Shift Timings: {staff.shiftTimings}</p>
      </div>
    </div>
  );
};

export default StaffDashboard;
