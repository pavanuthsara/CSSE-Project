import React, { useState } from 'react';
import './HealthCardGenerator.css';
import { FaUsers, FaCalendarCheck, FaUserPlus, FaIdCard, FaChartBar, FaSearch, FaDownload, FaPrint, FaQrcode } from 'react-icons/fa';

const HealthCardGenerator = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedPatient, setSelectedPatient] = useState(null);
  const [cardConfig, setCardConfig] = useState({
    cardType: 'standard',
    expiryDate: '',
    includeEmergencyContact: true
  });

  const patients = [
    {
      id: 'P-2025-001247',
      name: 'John Smith',
      dateOfBirth: 'March 15, 1985',
      bloodType: 'A+',
      gender: 'Male',
      phone: '(555) 123-4567',
      emergencyContact: '(555) 117-8547'
    },
    {
      id: 'P-2025-001248',
      name: 'Sarah Johnson',
      dateOfBirth: 'July 22, 1990',
      bloodType: 'O+',
      gender: 'Female',
      phone: '(555) 987-6543',
      emergencyContact: '(555) 456-7890'
    }
  ];

  const handleSearch = () => {
    const found = patients.find(p => 
      p.name.toLowerCase().includes(searchQuery.toLowerCase()) || 
      p.id.includes(searchQuery)
    );
    if (found) {
      setSelectedPatient(found);
    }
  };

  const handlePatientSelect = (patient) => {
    setSelectedPatient(patient);
    setSearchQuery(patient.name);
  };

  const handleConfigChange = (e) => {
    const { name, value, type, checked } = e.target;
    setCardConfig(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleGenerateCard = () => {
    console.log('Generating card for:', selectedPatient);
    console.log('Configuration:', cardConfig);
    // Implement card generation logic
  };

  const handlePrintCard = () => {
    console.log('Printing card...');
    window.print();
  };

  return (
    <div className="health-card-generator">
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

      <div className="generator-container">
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
            <a href="/hospital/records" className="nav-item">
              <FaIdCard className="nav-icon" />
              <span>Medical Records</span>
            </a>
            <a href="/hospital/health-cards" className="nav-item active">
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
          <h2 className="page-title">Digital Health Card Generator</h2>

          <div className="generator-layout">
            {/* Configuration Section */}
            <div className="config-section">
              {/* Patient Selection */}
              <div className="config-card">
                <h3>Patient Selection</h3>
                
                <div className="search-section">
                  <label>Search Patient</label>
                  <div className="search-bar">
                    <input
                      type="text"
                      placeholder="John Smith"
                      value={searchQuery}
                      onChange={(e) => setSearchQuery(e.target.value)}
                    />
                    <button className="search-icon-btn" onClick={handleSearch}>
                      <FaSearch />
                    </button>
                  </div>
                </div>

                {/* Patient List */}
                {!selectedPatient && (
                  <div className="patient-list">
                    {patients.map((patient) => (
                      <div 
                        key={patient.id} 
                        className="patient-list-item"
                        onClick={() => handlePatientSelect(patient)}
                      >
                        <div className="patient-avatar-small"></div>
                        <div className="patient-info-small">
                          <div className="patient-name-small">{patient.name}</div>
                          <div className="patient-id-small">{patient.id}</div>
                          <div className="patient-dob-small">DOB: {patient.dateOfBirth}</div>
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </div>

              {/* Card Configuration */}
              <div className="config-card">
                <h3>Card Configuration</h3>
                
                <div className="form-group">
                  <label>Card Type</label>
                  <select
                    name="cardType"
                    value={cardConfig.cardType}
                    onChange={handleConfigChange}
                  >
                    <option value="standard">Standard Health Card</option>
                    <option value="premium">Premium Health Card</option>
                    <option value="emergency">Emergency Card</option>
                  </select>
                </div>

                <div className="form-group">
                  <label>Expiry Date</label>
                  <input
                    type="date"
                    name="expiryDate"
                    value={cardConfig.expiryDate}
                    onChange={handleConfigChange}
                  />
                </div>

                <div className="checkbox-group">
                  <input
                    type="checkbox"
                    id="includeEmergencyContact"
                    name="includeEmergencyContact"
                    checked={cardConfig.includeEmergencyContact}
                    onChange={handleConfigChange}
                  />
                  <label htmlFor="includeEmergencyContact">Include Emergency Contact Info</label>
                </div>
              </div>
            </div>

            {/* Preview Section */}
            <div className="preview-section">
              <h3>Health Card Preview</h3>
              
              <div className="health-card-preview">
                <div className="card-header">
                  <div className="card-logo">
                    <div className="card-logo-icon">+</div>
                    <span>MedCare Hospital</span>
                  </div>
                  <div className="card-qr">
                    <FaQrcode />
                  </div>
                </div>

                {selectedPatient ? (
                  <div className="card-content">
                    <div className="card-patient-name">{selectedPatient.name}</div>
                    <div className="card-details">
                      <div className="card-detail-row">
                        <span className="detail-label">Patient ID:</span>
                        <span className="detail-value">{selectedPatient.id}</span>
                      </div>
                      <div className="card-detail-row">
                        <span className="detail-label">DOB:</span>
                        <span className="detail-value">{selectedPatient.dateOfBirth}</span>
                      </div>
                      <div className="card-detail-row">
                        <span className="detail-label">Blood Type:</span>
                        <span className="detail-value">{selectedPatient.bloodType}</span>
                      </div>
                      <div className="card-detail-row">
                        <span className="detail-label">Gender:</span>
                        <span className="detail-value">{selectedPatient.gender}</span>
                      </div>
                      {cardConfig.includeEmergencyContact && (
                        <div className="card-detail-row">
                          <span className="detail-label">Emergency:</span>
                          <span className="detail-value">{selectedPatient.emergencyContact}</span>
                        </div>
                      )}
                    </div>
                  </div>
                ) : (
                  <div className="card-placeholder">
                    <p>Select a patient to preview health card</p>
                  </div>
                )}
              </div>

              <div className="preview-actions">
                <button 
                  className="generate-btn" 
                  onClick={handleGenerateCard}
                  disabled={!selectedPatient}
                >
                  <FaDownload /> Generate Card
                </button>
                <button 
                  className="print-btn"
                  onClick={handlePrintCard}
                  disabled={!selectedPatient}
                >
                  <FaPrint />
                </button>
              </div>
            </div>
          </div>
        </main>
      </div>
    </div>
  );
};

export default HealthCardGenerator;
