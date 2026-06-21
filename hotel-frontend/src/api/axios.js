import axios from 'axios';

// Base URL points to your running Spring Boot backend
const api = axios.create({
  baseURL: 'http://localhost:8080'
});

// Interceptor — automatically adds JWT token to EVERY request
// So you never have to manually add Authorization header anywhere
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;