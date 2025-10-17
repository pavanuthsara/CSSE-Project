import React, { useState } from 'react';
import './AppointmentScheduler.css';
import { FaUsers, FaCalendarCheck, FaUserPlus, FaIdCard, FaChartBar, FaChevronLeft, FaChevronRight, FaPlus } from 'react-icons/fa';

const AppointmentScheduler = () => {
  const [currentDate, setCurrentDate] = useState(new Date(2025, 0, 15)); // January 15, 2025
  
  const todaysAppointments = [
    {
      time: '9:00 AM',
      duration: '30 min',
      patientName: 'John Smith',
      reason: 'Routine Checkup'
    },
    {
      time: '10:30 AM',
      duration: '45 min',
      patientName: 'Sarah Johnson',
      reason: 'Blood Test Results'
    },
    {
      time: '2:00 PM',
      duration: '60 min',
      patientName: 'Michael Chen',
      reason: 'Physical Therapy'
    }
  ];

  const monthNames = [
    'January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'
  ];

  const daysOfWeek = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

  const getDaysInMonth = (date) => {
    const year = date.getFullYear();
    const month = date.getMonth();
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const daysInMonth = lastDay.getDate();
    const startingDayOfWeek = firstDay.getDay();

    const days = [];

    // Add previous month's days
    const prevMonthLastDay = new Date(year, month, 0).getDate();
    for (let i = startingDayOfWeek - 1; i >= 0; i--) {
      days.push({
        day: prevMonthLastDay - i,
        isCurrentMonth: false
      });
    }

    // Add current month's days
    for (let i = 1; i <= daysInMonth; i++) {
      days.push({
        day: i,
        isCurrentMonth: true,
        isSelected: i === 15 // Highlight the 15th
      });
    }

    // Add next month's days to complete the grid
    const remainingDays = 42 - days.length; // 6 rows x 7 days = 42
    for (let i = 1; i <= remainingDays; i++) {
      days.push({
        day: i,
        isCurrentMonth: false
      });
    }

    return days;
  };

  const previousMonth = () => {
    setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() - 1, 1));
  };

  const nextMonth = () => {
    setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 1));
  };

  const handleScheduleNewAppointment = () => {
    console.log('Schedule new appointment');
    // Implement schedule new appointment functionality
  };

  const days = getDaysInMonth(currentDate);

  return (
    <div className="appointment-scheduler">
      {/* Header */}
      <header className="dashboard-header">
        <div className="header-left">
          <div className="logo">
            <div className="logo-icon">+</div>
            <h1>MedCare Hospital</h1>
          </div>
        </div>
        <div className="header-right">
          <div className="user-profile">
            <div className="user-avatar">DS</div>
            <span className="user-name">Dr. Sarah Johnson</span>
          </div>
        </div>
      </header>

      <div className="scheduler-container">
        {/* Sidebar */}
        <aside className="sidebar">
          <nav className="sidebar-nav">
            <a href="/hospital" className="nav-item">
              <FaChartBar className="nav-icon" />
              <span>Dashboard</span>
            </a>
            <a href="/hospital/register" className="nav-item">
              <FaUserPlus className="nav-icon" />
              <span>Register Patient</span>
            </a>
            <a href="/hospital/management" className="nav-item">
              <FaUsers className="nav-icon" />
              <span>Patient Management</span>
            </a>
            <a href="/hospital/appointments" className="nav-item active">
              <FaCalendarCheck className="nav-icon" />
              <span>Appointments</span>
            </a>
            <a href="#records" className="nav-item">
              <FaIdCard className="nav-icon" />
              <span>Medical Records</span>
            </a>
            <a href="#health-cards" className="nav-item">
              <FaIdCard className="nav-icon" />
              <span>Health Cards</span>
            </a>
            <a href="#reports" className="nav-item">
              <FaChartBar className="nav-icon" />
              <span>Reports</span>
            </a>
          </nav>
        </aside>

        {/* Main Content */}
        <main className="main-content">
          <h2 className="page-title">Appointment Scheduler</h2>

          <div className="scheduler-layout">
            {/* Calendar Section */}
            <div className="calendar-section">
              <div className="calendar-header">
                <h3>{monthNames[currentDate.getMonth()]} {currentDate.getFullYear()}</h3>
                <div className="calendar-nav">
                  <button onClick={previousMonth} className="nav-arrow">
                    <FaChevronLeft />
                  </button>
                  <button onClick={nextMonth} className="nav-arrow">
                    <FaChevronRight />
                  </button>
                </div>
              </div>

              <div className="calendar">
                <div className="calendar-days-header">
                  {daysOfWeek.map((day) => (
                    <div key={day} className="day-name">
                      {day}
                    </div>
                  ))}
                </div>

                <div className="calendar-days">
                  {days.map((dayObj, index) => (
                    <div
                      key={index}
                      className={`calendar-day ${
                        !dayObj.isCurrentMonth ? 'other-month' : ''
                      } ${dayObj.isSelected ? 'selected' : ''}`}
                    >
                      {dayObj.day}
                    </div>
                  ))}
                </div>
              </div>
            </div>

            {/* Today's Appointments Section */}
            <div className="appointments-section">
              <h3 className="section-title">Today's Appointments</h3>

              <div className="appointments-list">
                {todaysAppointments.map((appointment, index) => (
                  <div key={index} className="appointment-card">
                    <div className="appointment-time">
                      <span className="time">{appointment.time}</span>
                      <span className="duration">{appointment.duration}</span>
                    </div>
                    <div className="appointment-details">
                      <div className="patient-name">{appointment.patientName}</div>
                      <div className="appointment-reason">{appointment.reason}</div>
                    </div>
                  </div>
                ))}
              </div>

              <button className="schedule-btn" onClick={handleScheduleNewAppointment}>
                <FaPlus /> Schedule New Appointment
              </button>
            </div>
          </div>
        </main>
      </div>
    </div>
  );
};

export default AppointmentScheduler;
