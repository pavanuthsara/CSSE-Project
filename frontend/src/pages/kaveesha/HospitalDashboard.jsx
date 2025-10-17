import React, { useState, useEffect } from 'react';
import './HospitalDashboard.css';
import { FaUsers, FaCalendarCheck, FaUserPlus, FaIdCard, FaSearch, FaQrcode, FaFilter, FaChartBar } from 'react-icons/fa';

const HospitalDashboard = () => {
  const [currentDate, setCurrentDate] = useState('');
  const [searchQuery, setSearchQuery] = useState('');
  const [dashboardStats, setDashboardStats] = useState({
    totalPatients: 1247,
    todaysAppointments: 89,
    newRegistrations: 34,
    activeHealthCards: 156
  });

  useEffect(() => {
    // Set current date
    const today = new Date();
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    setCurrentDate(today.toLocaleDateString('en-US', options));
  }, []);

  const handleSearch = () => {
    console.log('Searching for:', searchQuery);
    // Implement search functionality here
  };

  const handleQRScan = () => {
    console.log('Opening QR Scanner');
    // Implement QR scanner functionality here
  };

  const handleAdvancedFilter = () => {
    console.log('Opening Advanced Filter');
    // Implement advanced filter functionality here
  };

  return (
    <div className="hospital-dashboard">
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

      <div className="dashboard-container">
        {/* Sidebar */}
        <aside className="sidebar">
          <nav className="sidebar-nav">
            <a href="#dashboard" className="nav-item active">
              <FaChartBar className="nav-icon" />
              <span>Dashboard</span>
            </a>
            <a href="#register" className="nav-item">
              <FaUserPlus className="nav-icon" />
              <span>Register Patient</span>
            </a>
            <a href="#management" className="nav-item">
              <FaUsers className="nav-icon" />
              <span>Patient Management</span>
            </a>
            <a href="#appointments" className="nav-item">
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
          <div className="content-header">
            <div>
              <h2>Hospital Management Dashboard</h2>
              <p className="date-subtitle">Today's Date: {currentDate}</p>
            </div>
            <div className="current-date">
              <span className="date-label">Today's Date</span>
              <span className="date-value">{currentDate}</span>
            </div>
          </div>

          {/* Stats Cards */}
          <div className="stats-grid">
            <div className="stat-card">
              <div className="stat-content">
                <h3>Total Patients</h3>
                <p className="stat-number">{dashboardStats.totalPatients.toLocaleString()}</p>
              </div>
              <div className="stat-icon blue">
                <FaUsers />
              </div>
            </div>

            <div className="stat-card">
              <div className="stat-content">
                <h3>Today's Appointments</h3>
                <p className="stat-number">{dashboardStats.todaysAppointments}</p>
              </div>
              <div className="stat-icon green">
                <FaCalendarCheck />
              </div>
            </div>

            <div className="stat-card">
              <div className="stat-content">
                <h3>New Registrations</h3>
                <p className="stat-number">{dashboardStats.newRegistrations}</p>
              </div>
              <div className="stat-icon purple">
                <FaUserPlus />
              </div>
            </div>

            <div className="stat-card">
              <div className="stat-content">
                <h3>Active Health Cards</h3>
                <p className="stat-number">{dashboardStats.activeHealthCards}</p>
              </div>
              <div className="stat-icon orange">
                <FaIdCard />
              </div>
            </div>
          </div>

          {/* Patient Search Section */}
          <div className="search-section">
            <h3>Patient Search & Quick Access</h3>
            
            <div className="search-bar">
              <input
                type="text"
                placeholder="Search by Name, Patient ID, or Phone Number"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="search-input"
              />
              <button onClick={handleSearch} className="search-button">
                <FaSearch /> Search
              </button>
            </div>

            <div className="search-actions">
              <button onClick={handleQRScan} className="action-button">
                <FaQrcode /> Scan QR Code
              </button>
              <button onClick={handleAdvancedFilter} className="action-button">
                <FaFilter /> Advanced Filter
              </button>
            </div>

            <div className="qr-scanner-placeholder">
              <div className="qr-icon-large">
                <FaQrcode />
              </div>
              <p className="qr-label">QR Scanner</p>
              <p className="qr-sublabel">Scan patient health card</p>
            </div>
          </div>
        </main>
      </div>
    </div>
  );
};

export default HospitalDashboard;
