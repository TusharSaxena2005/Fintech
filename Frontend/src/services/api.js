import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/v1';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Authentication Services
export const authService = {
  login: (loginData) => api.post('/auth/login', loginData),
  signup: (userData) => api.post('/users/signup', userData),
};

// User Account Services
export const accountService = {
  getAccount: (userId) => api.get(`/account/${userId}`),
  depositMoney: (userId, transactionData) => api.post(`/account/${userId}/deposit`, transactionData),
  withdrawMoney: (userId, transactionData) => api.post(`/account/${userId}/withdraw`, transactionData),
  getTransactions: (userId) => api.get(`/account/${userId}/transactions`),
};

// Admin Services
export const adminService = {
  getAllUsers: () => api.get('/admin/users'),
  getUserById: (id) => api.get(`/admin/users/${id}`),
  getUserByEmail: (email) => api.get(`/admin/users/email/${email}`),
  createUser: (userData) => api.post('/admin/users/signup', userData),
  updateUser: (id, userData) => api.put(`/admin/users/${id}`, userData),
  deleteUser: (id) => api.delete(`/admin/users/${id}`),
  updateBalance: (id, balance) => api.put(`/admin/users/${id}/balance?balance=${balance}`),
};

export default api;