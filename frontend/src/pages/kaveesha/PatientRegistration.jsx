import React, { useState } from 'react';
import './PatientRegistration.css';
import { FaUsers, FaCalendarCheck, FaUserPlus, FaIdCard, FaChartBar } from 'react-icons/fa';

const PatientRegistration = () => {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    dateOfBirth: '',
    gender: '',
    phoneNumber: '',
    email: '',
    address: '',
    emergencyContactName: '',
    emergencyContactPhone: '',
    relationship: '',
    bloodType: '',
    knownAllergies: ''
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Form submitted:', formData);
    // Add your form submission logic here
  };

  const handleCancel = () => {
    setFormData({
      firstName: '',
      lastName: '',
      dateOfBirth: '',
      gender: '',
      phoneNumber: '',
      email: '',
      address: '',
      emergencyContactName: '',
      emergencyContactPhone: '',
      relationship: '',
      bloodType: '',
      knownAllergies: ''
    });
  };

  return (
    <div className="patient-registration">
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

      <div className="registration-container">
        {/* Sidebar */}
        <aside className="sidebar">
          <nav className="sidebar-nav">
            <a href="/hospital" className="nav-item">
              <FaChartBar className="nav-icon" />
              <span>Dashboard</span>
            </a>
            <a href="/hospital/register" className="nav-item active">
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
            <h2>New Patient Registration</h2>
            <button className="register-new-btn">
              <FaUserPlus /> Register New Patient
            </button>
          </div>

          <form onSubmit={handleSubmit} className="registration-form">
            <div className="form-sections">
              {/* Personal Information Section */}
              <div className="form-section">
                <h3 className="section-title">Personal Information</h3>
                
                <div className="form-row">
                  <div className="form-group">
                    <label htmlFor="firstName">First Name</label>
                    <input
                      type="text"
                      id="firstName"
                      name="firstName"
                      placeholder="Enter first name"
                      value={formData.firstName}
                      onChange={handleInputChange}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="lastName">Last Name</label>
                    <input
                      type="text"
                      id="lastName"
                      name="lastName"
                      placeholder="Enter last name"
                      value={formData.lastName}
                      onChange={handleInputChange}
                      required
                    />
                  </div>
                </div>

                <div className="form-row">
                  <div className="form-group">
                    <label htmlFor="dateOfBirth">Date of Birth</label>
                    <input
                      type="date"
                      id="dateOfBirth"
                      name="dateOfBirth"
                      placeholder="mm/dd/yyyy"
                      value={formData.dateOfBirth}
                      onChange={handleInputChange}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="gender">Gender</label>
                    <select
                      id="gender"
                      name="gender"
                      value={formData.gender}
                      onChange={handleInputChange}
                      required
                    >
                      <option value="">Select Gender</option>
                      <option value="male">Male</option>
                      <option value="female">Female</option>
                      <option value="other">Other</option>
                    </select>
                  </div>
                </div>

                <div className="form-row">
                  <div className="form-group full-width">
                    <label htmlFor="phoneNumber">Phone Number</label>
                    <input
                      type="tel"
                      id="phoneNumber"
                      name="phoneNumber"
                      placeholder="Enter phone number"
                      value={formData.phoneNumber}
                      onChange={handleInputChange}
                      required
                    />
                  </div>
                </div>

                <div className="form-row">
                  <div className="form-group full-width">
                    <label htmlFor="email">Email Address</label>
                    <input
                      type="email"
                      id="email"
                      name="email"
                      placeholder="Enter email address"
                      value={formData.email}
                      onChange={handleInputChange}
                      required
                    />
                  </div>
                </div>

                <div className="form-row">
                  <div className="form-group full-width">
                    <label htmlFor="address">Address</label>
                    <textarea
                      id="address"
                      name="address"
                      placeholder="Enter full address"
                      value={formData.address}
                      onChange={handleInputChange}
                      rows="3"
                      required
                    />
                  </div>
                </div>
              </div>

              {/* Emergency Contact & Medical Info Section */}
              <div className="form-section">
                <h3 className="section-title">Emergency Contact & Medical Info</h3>
                
                <div className="form-row">
                  <div className="form-group full-width">
                    <label htmlFor="emergencyContactName">Emergency Contact Name</label>
                    <input
                      type="text"
                      id="emergencyContactName"
                      name="emergencyContactName"
                      placeholder="Enter emergency contact name"
                      value={formData.emergencyContactName}
                      onChange={handleInputChange}
                      required
                    />
                  </div>
                </div>

                <div className="form-row">
                  <div className="form-group full-width">
                    <label htmlFor="emergencyContactPhone">Emergency Contact Phone</label>
                    <input
                      type="tel"
                      id="emergencyContactPhone"
                      name="emergencyContactPhone"
                      placeholder="Enter emergency contact phone"
                      value={formData.emergencyContactPhone}
                      onChange={handleInputChange}
                      required
                    />
                  </div>
                </div>

                <div className="form-row">
                  <div className="form-group full-width">
                    <label htmlFor="relationship">Relationship</label>
                    <select
                      id="relationship"
                      name="relationship"
                      value={formData.relationship}
                      onChange={handleInputChange}
                      required
                    >
                      <option value="">Select Relationship</option>
                      <option value="spouse">Spouse</option>
                      <option value="parent">Parent</option>
                      <option value="child">Child</option>
                      <option value="sibling">Sibling</option>
                      <option value="friend">Friend</option>
                      <option value="other">Other</option>
                    </select>
                  </div>
                </div>

                <div className="form-row">
                  <div className="form-group full-width">
                    <label htmlFor="bloodType">Blood Type</label>
                    <select
                      id="bloodType"
                      name="bloodType"
                      value={formData.bloodType}
                      onChange={handleInputChange}
                      required
                    >
                      <option value="">Select Blood Type</option>
                      <option value="A+">A+</option>
                      <option value="A-">A-</option>
                      <option value="B+">B+</option>
                      <option value="B-">B-</option>
                      <option value="AB+">AB+</option>
                      <option value="AB-">AB-</option>
                      <option value="O+">O+</option>
                      <option value="O-">O-</option>
                    </select>
                  </div>
                </div>

                <div className="form-row">
                  <div className="form-group full-width">
                    <label htmlFor="knownAllergies">Known Allergies</label>
                    <textarea
                      id="knownAllergies"
                      name="knownAllergies"
                      placeholder="Enter known allergies (if any)"
                      value={formData.knownAllergies}
                      onChange={handleInputChange}
                      rows="3"
                    />
                  </div>
                </div>
              </div>
            </div>

            {/* Form Actions */}
            <div className="form-actions">
              <button type="button" className="cancel-btn" onClick={handleCancel}>
                Cancel
              </button>
              <button type="submit" className="submit-btn">
                Register Patient
              </button>
            </div>
          </form>
        </main>
      </div>
    </div>
  );
};

export default PatientRegistration;
