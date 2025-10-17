
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

export const getPaymentsDashboard = (token) => {
  return api.get('/payments/dashboard', { headers: { Authorization: `Bearer ${token}` } });
};

export const getPatientInvoices = (patientId, token) => {
  return api.get(`/patients/${patientId}/invoices`, { headers: { Authorization: `Bearer ${token}` } });
};

export const processPayment = (payload, token) => {
  return api.post('/payments/process', payload, { headers: { Authorization: `Bearer ${token}` } });
};

export const getReceipt = (paymentId, token) => {
  return api.get(`/payments/${paymentId}/receipt`, { headers: { Authorization: `Bearer ${token}` } });
};

export default api;
