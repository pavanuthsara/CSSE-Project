
import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_URL,
});

export const login = (credentials) => {
  return api.post('/auth/login', credentials);
};

export const register = (userData) => {
  return api.post('/auth/register', userData);
};

export const getDoctorDashboard = (token) => {
  return api.get('/doctor/dashboard', { headers: { Authorization: `Bearer ${token}` } });
};

export const getStaffDashboard = (token) => {
  return api.get('/staff/dashboard', { headers: { Authorization: `Bearer ${token}` } });
};

// Patient registration
export const registerPatient = (patientData) => {
  return api.post('/patients/register', patientData);
};

// Appointment services
export const getAvailableTimeSlots = (doctorId, date) => {
  return api.get(`/appointments/doctor/${doctorId}/available-slots?date=${date}`);
};

export const bookAppointment = (bookingData) => {
  const token = localStorage.getItem('token');
  return api.post('/appointments/book', bookingData, {
    headers: { Authorization: `Bearer ${token}` }
  });
};

export const getPatientAppointments = (token) => {
  return api.get('/appointments/patient', {
    headers: { Authorization: `Bearer ${token}` }
  });
};

export const getUpcomingAppointments = (token) => {
  return api.get('/appointments/patient/upcoming', {
    headers: { Authorization: `Bearer ${token}` }
  });
};

export const getDoctorAppointments = (doctorId, date) => {
  const token = localStorage.getItem('token');
  return api.get(`/appointments/doctor/${doctorId}?date=${date}`, {
    headers: { Authorization: `Bearer ${token}` }
  });
};

export const updateAppointmentStatus = (appointmentId, status) => {
  const token = localStorage.getItem('token');
  return api.put(`/appointments/${appointmentId}/status?status=${status}`, {}, {
    headers: { Authorization: `Bearer ${token}` }
  });
};

export const cancelAppointment = (appointmentId) => {
  const token = localStorage.getItem('token');
  return api.delete(`/appointments/${appointmentId}`, {
    headers: { Authorization: `Bearer ${token}` }
  });
};

// Doctor services
export const getDoctors = () => {
  return api.get('/doctor/all');
};

export default api;
