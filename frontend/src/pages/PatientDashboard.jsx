import React, { useState, useEffect } from 'react';
import { getPatientAppointments, cancelAppointment } from '../services/api';

const PatientDashboard = () => {
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchAppointments();
  }, []);

  const fetchAppointments = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await getPatientAppointments(token);
      setAppointments(response.data);
    } catch (error) {
      setError('Failed to fetch appointments');
      console.error('Failed to fetch appointments', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCancelAppointment = async (appointmentId) => {
    if (window.confirm('Are you sure you want to cancel this appointment?')) {
      try {
        await cancelAppointment(appointmentId);
        alert('Appointment cancelled successfully');
        fetchAppointments(); // Refresh the list
      } catch (error) {
        alert('Failed to cancel appointment');
        console.error('Failed to cancel appointment', error);
      }
    }
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString();
  };

  const formatTime = (timeString) => {
    return new Date(`2000-01-01T${timeString}`).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'SCHEDULED': return '#007bff';
      case 'CONFIRMED': return '#28a745';
      case 'COMPLETED': return '#6c757d';
      case 'CANCELLED': return '#dc3545';
      case 'NO_SHOW': return '#ffc107';
      default: return '#000000';
    }
  };

  if (loading) {
    return (
      <div className="app">
        <header className="header">
          <h1>Patient Dashboard</h1>
        </header>
        <div className="container">
          <p>Loading appointments...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="app">
        <header className="header">
          <h1>Patient Dashboard</h1>
        </header>
        <div className="container">
          <p style={{ color: 'red' }}>{error}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="app">
      <header className="header">
        <h1>Patient Dashboard</h1>
        <div className="header-actions">
          <a href="/book-appointment" className="btn btn-primary">Book New Appointment</a>
          <button onClick={() => { localStorage.clear(); window.location.href = '/login'; }} className="btn btn-secondary">Logout</button>
        </div>
      </header>
      
      <div className="container">
        <h2>My Appointments</h2>
        
        <div className="appointments-list">
          {appointments.length === 0 ? (
            <div className="no-appointments">
              <p>No appointments found.</p>
              <a href="/book-appointment" className="btn btn-primary">Book your first appointment</a>
            </div>
          ) : (
            appointments.map(appointment => (
              <div key={appointment.id} className="appointment-card">
                <div className="appointment-header">
                  <h3>Dr. {appointment.doctor.firstName} {appointment.doctor.lastName}</h3>
                  <span 
                    className="status"
                    style={{ color: getStatusColor(appointment.status) }}
                  >
                    {appointment.status}
                  </span>
                </div>
                
                <div className="appointment-details">
                  <div className="detail-row">
                    <strong>Date:</strong> {formatDate(appointment.appointmentDate)}
                  </div>
                  <div className="detail-row">
                    <strong>Time:</strong> {formatTime(appointment.appointmentTime)}
                  </div>
                  <div className="detail-row">
                    <strong>Specialization:</strong> {appointment.doctor.specialization}
                  </div>
                  <div className="detail-row">
                    <strong>Department:</strong> {appointment.doctor.department}
                  </div>
                  <div className="detail-row">
                    <strong>Consultation Fee:</strong> ${appointment.doctor.consultationFee}
                  </div>
                  <div className="detail-row">
                    <strong>Reason:</strong> {appointment.reasonForVisit}
                  </div>
                  {appointment.notes && (
                    <div className="detail-row">
                      <strong>Notes:</strong> {appointment.notes}
                    </div>
                  )}
                </div>
                
                <div className="appointment-actions">
                  {appointment.status === 'SCHEDULED' && (
                    <button 
                      className="btn btn-danger"
                      onClick={() => handleCancelAppointment(appointment.id)}
                    >
                      Cancel Appointment
                    </button>
                  )}
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
};

export default PatientDashboard;
