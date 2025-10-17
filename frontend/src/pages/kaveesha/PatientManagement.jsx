import React, { useState } from 'react';
import './PatientManagement.css';
import { FaUsers, FaCalendarCheck, FaUserPlus, FaIdCard, FaChartBar, FaEye, FaEdit, FaFileAlt, FaDownload, FaChevronLeft, FaChevronRight } from 'react-icons/fa';

const PatientManagement = () => {
  const [patients] = useState([
    {
      id: 'P-2025-001247',
      name: 'John Smith',
      gender: 'Male',
      age: 39,
      contact: '(555) 123-4567',
      lastVisit: 'Jan 10, 2025',
      status: 'Active'
    },
    {
      id: 'P-2025-001248',
      name: 'Sarah Johnson',
      gender: 'Female',
      age: 45,
      contact: '(555) 987-6543',
      lastVisit: 'Jan 08, 2025',
      status: 'Active'
    },
    {
      id: 'P-2025-001249',
      name: 'Michael Chen',
      gender: 'Male',
      age: 32,
      contact: '(555) 456-7890',
      lastVisit: 'Jan 05, 2025',
      status: 'Pending'
    }
  ]);

  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage, setItemsPerPage] = useState(10);
  const [totalResults] = useState(1247);

  const handleView = (patientId) => {
    console.log('View patient:', patientId);
    // Implement view functionality
  };

  const handleEdit = (patientId) => {
    console.log('Edit patient:', patientId);
    // Implement edit functionality
  };

  const handleViewRecords = (patientId) => {
    console.log('View records for:', patientId);
    // Implement view records functionality
  };

  const handleExport = () => {
    console.log('Exporting data...');
    // Implement export functionality
  };

  const handleItemsPerPageChange = (e) => {
    setItemsPerPage(Number(e.target.value));
    setCurrentPage(1);
  };

  const totalPages = Math.ceil(totalResults / itemsPerPage);

  const goToNextPage = () => {
    if (currentPage < totalPages) {
      setCurrentPage(currentPage + 1);
    }
  };

  const goToPreviousPage = () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
    }
  };

  const goToPage = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  const renderPagination = () => {
    const pages = [];
    const showPages = 3;
    
    if (currentPage > 1) {
      pages.push(
        <button key="prev" onClick={goToPreviousPage} className="pagination-arrow">
          <FaChevronLeft />
        </button>
      );
    }

    for (let i = 1; i <= Math.min(showPages, totalPages); i++) {
      pages.push(
        <button
          key={i}
          onClick={() => goToPage(i)}
          className={`pagination-number ${currentPage === i ? 'active' : ''}`}
        >
          {i}
        </button>
      );
    }

    if (currentPage < totalPages) {
      pages.push(
        <button key="next" onClick={goToNextPage} className="pagination-arrow">
          <FaChevronRight />
        </button>
      );
    }

    return pages;
  };

  return (
    <div className="patient-management">
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

      <div className="management-container">
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
            <a href="/hospital/management" className="nav-item active">
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
            <h2>Patient Management</h2>
            <div className="header-actions">
              <div className="show-selector">
                <label>Show:</label>
                <select value={itemsPerPage} onChange={handleItemsPerPageChange}>
                  <option value={10}>10</option>
                  <option value={25}>25</option>
                  <option value={50}>50</option>
                  <option value={100}>100</option>
                </select>
              </div>
              <button className="export-btn" onClick={handleExport}>
                <FaDownload /> Export
              </button>
            </div>
          </div>

          {/* Patient Table */}
          <div className="table-container">
            <table className="patient-table">
              <thead>
                <tr>
                  <th>PATIENT</th>
                  <th>PATIENT ID</th>
                  <th>AGE</th>
                  <th>CONTACT</th>
                  <th>LAST VISIT</th>
                  <th>STATUS</th>
                  <th>ACTIONS</th>
                </tr>
              </thead>
              <tbody>
                {patients.map((patient) => (
                  <tr key={patient.id}>
                    <td>
                      <div className="patient-info">
                        <div className="patient-avatar"></div>
                        <div className="patient-details">
                          <div className="patient-name">{patient.name}</div>
                          <div className="patient-gender">{patient.gender}</div>
                        </div>
                      </div>
                    </td>
                    <td>{patient.id}</td>
                    <td>{patient.age}</td>
                    <td>{patient.contact}</td>
                    <td>{patient.lastVisit}</td>
                    <td>
                      <span className={`status-badge ${patient.status.toLowerCase()}`}>
                        {patient.status}
                      </span>
                    </td>
                    <td>
                      <div className="action-buttons">
                        <button
                          className="action-btn view-btn"
                          onClick={() => handleView(patient.id)}
                          title="View"
                        >
                          <FaEye />
                        </button>
                        <button
                          className="action-btn edit-btn"
                          onClick={() => handleEdit(patient.id)}
                          title="Edit"
                        >
                          <FaEdit />
                        </button>
                        <button
                          className="action-btn records-btn"
                          onClick={() => handleViewRecords(patient.id)}
                          title="View Records"
                        >
                          <FaFileAlt />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {/* Pagination */}
          <div className="pagination-container">
            <div className="pagination-info">
              Showing {((currentPage - 1) * itemsPerPage) + 1} to {Math.min(currentPage * itemsPerPage, totalResults)} of {totalResults} results
            </div>
            <div className="pagination-controls">
              {renderPagination()}
            </div>
          </div>
        </main>
      </div>
    </div>
  );
};

export default PatientManagement;
