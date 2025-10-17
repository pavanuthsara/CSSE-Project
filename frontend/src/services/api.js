
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

export default api;
