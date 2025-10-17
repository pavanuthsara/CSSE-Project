import React, { useState } from 'react';
import './MedicalRecords.css';
import { FaUsers, FaCalendarCheck, FaUserPlus, FaIdCard, FaChartBar, FaEye, FaEdit, FaDownload, FaFilter, FaPlus, FaUpload } from 'react-icons/fa';

const MedicalRecords = () => {
  const [medicalRecords] = useState([
    {
      id: 1,
      title: 'Annual Physical Examination',
      provider: 'Dr. Sarah Johnson - General Medicine',
      date: 'January 19, 2025',
      description: 'Annual preventive care routine physical. Blood pressure: Slightly lower than 11 year old, cardio within ranges.',
      type: 'Physical Exam'
    },
    {
      id: 2,
      title: 'Blood Test Results',
      provider: 'Lab Tech - Laboratory Services',
      date: 'December 28, 2024',
      description: 'Complete blood count and metabolic panel. Cholesterol levels slightly elevated. Recommend dietary consultation.',
      type: 'Lab Results'
    },
    {
      id: 3,
      title: 'Vaccination Record',
      provider: 'Nurse Mary Wilson - Immunization',
      date: 'November 15, 2024',
      description: 'Annual flu vaccination administered. No adverse reactions reported. Next vaccination due: November 2025.',
      type: 'Vaccination'
    }
  ]);

  const [showAddRecordForm, setShowAddRecordForm] = useState(false);
  const [formData, setFormData] = useState({
    recordType: '',
    date: '',
    provider: '',
    title: '',
    description: '',
    attachments: []
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleFileUpload = (e) => {
    const files = Array.from(e.target.files);
    setFormData(prev => ({
      ...prev,
      attachments: [...prev.attachments, ...files]
    }));
  };

  const handleAddRecord = (e) => {
    e.preventDefault();
    console.log('Adding new record:', formData);
    // Add your record submission logic here
    setShowAddRecordForm(false);
  };

  const handleViewDetails = (recordId) => {
    console.log('View details:', recordId);
  };

  const handleEdit = (recordId) => {
    console.log('Edit record:', recordId);
  };

  const handleDownloadReport = (recordId) => {
    console.log('Download report:', recordId);
  };

  const handlePrintCertificate = (recordId) => {
    console.log('Print certificate:', recordId);
  };

  return (
    <div className="medical-records">
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

      <div className="records-container">
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
            <a href="/hospital/appointments" className="nav-item">
              <FaCalendarCheck className="nav-icon" />
              <span>Appointments</span>
            </a>
            <a href="/hospital/records" className="nav-item active">
              <FaIdCard className="nav-icon" />
              <span>Medical Records</span>
            </a>
            <a href="/hospital/health-cards" className="nav-item">
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
            <h2>Medical Records Management</h2>
            <div className="header-actions">
              <button className="filter-btn">
                <FaFilter /> Filter
              </button>
              <button className="add-record-btn" onClick={() => setShowAddRecordForm(!showAddRecordForm)}>
                <FaPlus /> Add Record
              </button>
            </div>
          </div>

          <div className="records-layout">
            {/* Patient Medical History */}
            <div className="history-section">
              <h3 className="section-title">Patient Medical History</h3>

              <div className="records-list">
                {medicalRecords.map((record) => (
                  <div key={record.id} className="record-card">
                    <div className="record-header">
                      <div className="record-title-section">
                        <h4>{record.title}</h4>
                        <div className="record-provider">{record.provider}</div>
                      </div>
                      <div className="record-date">{record.date}</div>
                    </div>
                    <p className="record-description">{record.description}</p>
                    <div className="record-actions">
                      <button className="action-btn-text" onClick={() => handleViewDetails(record.id)}>
                        <FaEye /> View Details
                      </button>
                      <button className="action-btn-text" onClick={() => handleEdit(record.id)}>
                        <FaEdit /> Edit
                      </button>
                      {record.type === 'Lab Results' && (
                        <button className="action-btn-text" onClick={() => handleDownloadReport(record.id)}>
                          <FaDownload /> Download Report
                        </button>
                      )}
                      {record.type === 'Vaccination' && (
                        <button className="action-btn-text" onClick={() => handlePrintCertificate(record.id)}>
                          <FaDownload /> Print Certificate
                        </button>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            </div>

            {/* Add New Medical Record Form */}
            {showAddRecordForm && (
              <div className="add-record-section">
                <h3 className="section-title">Add New Medical Record</h3>
                
                <form onSubmit={handleAddRecord} className="add-record-form">
                  <div className="form-group">
                    <label htmlFor="recordType">Record Type</label>
                    <select
                      id="recordType"
                      name="recordType"
                      value={formData.recordType}
                      onChange={handleInputChange}
                      required
                    >
                      <option value="">Select Record Type</option>
                      <option value="physical">Physical Exam</option>
                      <option value="lab">Lab Results</option>
                      <option value="vaccination">Vaccination</option>
                      <option value="imaging">Imaging</option>
                      <option value="prescription">Prescription</option>
                      <option value="other">Other</option>
                    </select>
                  </div>

                  <div className="form-group">
                    <label htmlFor="date">Date</label>
                    <input
                      type="date"
                      id="date"
                      name="date"
                      value={formData.date}
                      onChange={handleInputChange}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="provider">Healthcare Provider</label>
                    <input
                      type="text"
                      id="provider"
                      name="provider"
                      placeholder="Select Provider"
                      value={formData.provider}
                      onChange={handleInputChange}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="title">Title</label>
                    <input
                      type="text"
                      id="title"
                      name="title"
                      placeholder="Record title"
                      value={formData.title}
                      onChange={handleInputChange}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="description">Description</label>
                    <textarea
                      id="description"
                      name="description"
                      placeholder="Enter medical description"
                      value={formData.description}
                      onChange={handleInputChange}
                      rows="4"
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label>Attachments</label>
                    <div className="file-upload-area">
                      <FaUpload className="upload-icon" />
                      <p>Drop files here or click to upload</p>
                      <input
                        type="file"
                        multiple
                        onChange={handleFileUpload}
                        className="file-input"
                      />
                    </div>
                  </div>

                  <button type="submit" className="submit-record-btn">
                    Add Medical Record
                  </button>
                </form>
              </div>
            )}
          </div>
        </main>
      </div>
    </div>
  );
};

export default MedicalRecords;
