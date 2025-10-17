import React, { useState, useEffect } from 'react';
import { getAvailableTimeSlots, bookAppointment, getDoctors } from '../services/api';

const AppointmentBookingPage = () => {
  const [doctors, setDoctors] = useState([]);
  const [selectedDoctor, setSelectedDoctor] = useState('');
  const [selectedDate, setSelectedDate] = useState('');
  const [availableSlots, setAvailableSlots] = useState([]);
  const [selectedSlot, setSelectedSlot] = useState('');
  const [reasonForVisit, setReasonForVisit] = useState('');
  const [notes, setNotes] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchDoctors();
  }, []);

  const fetchDoctors = async () => {
    try {
      const response = await getDoctors();
      setDoctors(response.data);
    } catch (error) {
      setError('Failed to fetch doctors');
      console.error('Failed to fetch doctors', error);
    }
  };

  const handleDateChange = async (date) => {
    if (selectedDoctor && date) {
      setLoading(true);
      setError(null);
      try {
        const slots = await getAvailableTimeSlots(selectedDoctor, date);
        setAvailableSlots(slots.data);
        setSelectedSlot(''); // Reset selected slot when date changes
      } catch (error) {
        setError('Failed to fetch available slots');
        console.error('Failed to fetch available slots', error);
      } finally {
        setLoading(false);
      }
    }
  };

  const handleBooking = async (e) => {
    e.preventDefault();
    if (!selectedDoctor || !selectedDate || !selectedSlot || !reasonForVisit) {
      alert('Please fill in all required fields');
      return;
    }

    setLoading(true);
    setError(null);
    try {
      const [startTime] = selectedSlot.split('-');
      const bookingData = {
        doctorId: selectedDoctor,
        appointmentDate: selectedDate,
        appointmentTime: startTime,
        reasonForVisit,
        notes
      };

      await bookAppointment(bookingData);
      alert('Appointment booked successfully!');
      // Redirect to patient dashboard
      window.location.href = '/patient/dashboard';
    } catch (error) {
      setError('Failed to book appointment. Please try again.');
      console.error('Failed to book appointment', error);
    } finally {
      setLoading(false);
    }
  };

  const getMinDate = () => {
    const today = new Date();
    return today.toISOString().split('T')[0];
  };

  const getMaxDate = () => {
    const maxDate = new Date();
    maxDate.setDate(maxDate.getDate() + 30); // Allow booking up to 30 days in advance
    return maxDate.toISOString().split('T')[0];
  };

  return (
    <div className="app">
      <header className="header">
        <h1>Book an Appointment</h1>
        <div className="header-actions">
          <a href="/patient/dashboard" className="btn btn-secondary">Back to Dashboard</a>
        </div>
      </header>
      
      <div className="container">
        {error && (
          <div className="error-message" style={{ color: 'red', marginBottom: '20px' }}>
            {error}
          </div>
        )}

        <form onSubmit={handleBooking} className="appointment-form">
          <div className="form-group">
            <label htmlFor="doctor">Select Doctor: *</label>
            <select 
              id="doctor"
              value={selectedDoctor} 
              onChange={(e) => {
                setSelectedDoctor(e.target.value);
                setSelectedDate(''); // Reset date when doctor changes
                setAvailableSlots([]);
                setSelectedSlot('');
              }}
              required
            >
              <option value="">Choose a doctor</option>
              {doctors.map(doctor => (
                <option key={doctor.id} value={doctor.id}>
                  Dr. {doctor.user.firstName} {doctor.user.lastName} - {doctor.specialization}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="date">Select Date: *</label>
            <input
              id="date"
              type="date"
              value={selectedDate}
              onChange={(e) => {
                setSelectedDate(e.target.value);
                handleDateChange(e.target.value);
              }}
              min={getMinDate()}
              max={getMaxDate()}
              required
            />
          </div>

          <div className="form-group">
            <label>Available Time Slots: *</label>
            {loading ? (
              <p>Loading available slots...</p>
            ) : availableSlots.length === 0 && selectedDate ? (
              <p>No available slots for the selected date.</p>
            ) : (
              <div className="time-slots">
                {availableSlots.map((slot, index) => (
                  <button
                    key={index}
                    type="button"
                    className={`time-slot ${selectedSlot === `${slot.startTime}-${slot.endTime}` ? 'selected' : ''}`}
                    onClick={() => setSelectedSlot(`${slot.startTime}-${slot.endTime}`)}
                  >
                    {slot.startTime} - {slot.endTime}
                  </button>
                ))}
              </div>
            )}
          </div>

          <div className="form-group">
            <label htmlFor="reason">Reason for Visit: *</label>
            <textarea
              id="reason"
              value={reasonForVisit}
              onChange={(e) => setReasonForVisit(e.target.value)}
              required
              rows="3"
              placeholder="Please describe the reason for your visit..."
            />
          </div>

          <div className="form-group">
            <label htmlFor="notes">Additional Notes:</label>
            <textarea
              id="notes"
              value={notes}
              onChange={(e) => setNotes(e.target.value)}
              rows="2"
              placeholder="Any additional information you'd like to share..."
            />
          </div>

          <div className="form-actions">
            <button type="submit" disabled={loading} className="btn btn-primary">
              {loading ? 'Booking...' : 'Book Appointment'}
            </button>
            <a href="/patient/dashboard" className="btn btn-secondary">Cancel</a>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AppointmentBookingPage;
