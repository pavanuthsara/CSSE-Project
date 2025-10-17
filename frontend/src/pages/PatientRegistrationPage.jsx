import React, { useState } from 'react';
import { registerPatient } from '../services/api';

const PatientRegistrationPage = () => {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    email: '',
    firstName: '',
    lastName: '',
    phoneNumber: '',
    dateOfBirth: '',
    gender: '',
    address: '',
    emergencyContact: '',
    medicalHistory: '',
    allergies: '',
    insuranceProvider: '',
    insuranceNumber: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      await registerPatient(formData);
      setSuccess(true);
      // Redirect to login after 3 seconds
      setTimeout(() => {
        window.location.href = '/login';
      }, 3000);
    } catch (error) {
      setError('Registration failed. Please try again.');
      console.error('Registration failed', error);
    } finally {
      setLoading(false);
    }
  };

  if (success) {
    return (
      <div className="app">
        <header className="header">
          <h1>Registration Successful</h1>
        </header>
        <div className="container">
          <div className="success-message">
            <h2>Welcome to our Healthcare System!</h2>
            <p>Your patient account has been created successfully.</p>
            <p>You will be redirected to the login page in a few seconds...</p>
            <a href="/login" className="btn btn-primary">Go to Login</a>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="app">
      <header className="header">
        <h1>Patient Registration</h1>
        <div className="header-actions">
          <a href="/login" className="btn btn-secondary">Back to Login</a>
        </div>
      </header>
      
      <div className="container">
        {error && (
          <div className="error-message" style={{ color: 'red', marginBottom: '20px' }}>
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="registration-form">
          <div className="form-section">
            <h3>Account Information</h3>
            
            <div className="form-group">
              <label htmlFor="username">Username: *</label>
              <input
                type="text"
                id="username"
                name="username"
                value={formData.username}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="password">Password: *</label>
              <input
                type="password"
                id="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="email">Email: *</label>
              <input
                type="email"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                required
              />
            </div>
          </div>

          <div className="form-section">
            <h3>Personal Information</h3>
            
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="firstName">First Name: *</label>
                <input
                  type="text"
                  id="firstName"
                  name="firstName"
                  value={formData.firstName}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="lastName">Last Name: *</label>
                <input
                  type="text"
                  id="lastName"
                  name="lastName"
                  value={formData.lastName}
                  onChange={handleChange}
                  required
                />
              </div>
            </div>

            <div className="form-group">
              <label htmlFor="phoneNumber">Phone Number: *</label>
              <input
                type="tel"
                id="phoneNumber"
                name="phoneNumber"
                value={formData.phoneNumber}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-row">
              <div className="form-group">
                <label htmlFor="dateOfBirth">Date of Birth: *</label>
                <input
                  type="date"
                  id="dateOfBirth"
                  name="dateOfBirth"
                  value={formData.dateOfBirth}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="gender">Gender: *</label>
                <select
                  id="gender"
                  name="gender"
                  value={formData.gender}
                  onChange={handleChange}
                  required
                >
                  <option value="">Select Gender</option>
                  <option value="Male">Male</option>
                  <option value="Female">Female</option>
                  <option value="Other">Other</option>
                </select>
              </div>
            </div>

            <div className="form-group">
              <label htmlFor="address">Address: *</label>
              <textarea
                id="address"
                name="address"
                value={formData.address}
                onChange={handleChange}
                required
                rows="2"
              />
            </div>

            <div className="form-group">
              <label htmlFor="emergencyContact">Emergency Contact: *</label>
              <input
                type="tel"
                id="emergencyContact"
                name="emergencyContact"
                value={formData.emergencyContact}
                onChange={handleChange}
                required
              />
            </div>
          </div>

          <div className="form-section">
            <h3>Medical Information</h3>
            
            <div className="form-group">
              <label htmlFor="medicalHistory">Medical History:</label>
              <textarea
                id="medicalHistory"
                name="medicalHistory"
                value={formData.medicalHistory}
                onChange={handleChange}
                rows="3"
                placeholder="Please describe any relevant medical history..."
              />
            </div>

            <div className="form-group">
              <label htmlFor="allergies">Allergies:</label>
              <textarea
                id="allergies"
                name="allergies"
                value={formData.allergies}
                onChange={handleChange}
                rows="2"
                placeholder="Please list any known allergies..."
              />
            </div>
          </div>

          <div className="form-section">
            <h3>Insurance Information</h3>
            
            <div className="form-group">
              <label htmlFor="insuranceProvider">Insurance Provider:</label>
              <input
                type="text"
                id="insuranceProvider"
                name="insuranceProvider"
                value={formData.insuranceProvider}
                onChange={handleChange}
              />
            </div>

            <div className="form-group">
              <label htmlFor="insuranceNumber">Insurance Number:</label>
              <input
                type="text"
                id="insuranceNumber"
                name="insuranceNumber"
                value={formData.insuranceNumber}
                onChange={handleChange}
              />
            </div>
          </div>

          <div className="form-actions">
            <button type="submit" disabled={loading} className="btn btn-primary">
              {loading ? 'Registering...' : 'Register'}
            </button>
            <a href="/login" className="btn btn-secondary">Cancel</a>
          </div>
        </form>
      </div>
    </div>
  );
};

export default PatientRegistrationPage;
