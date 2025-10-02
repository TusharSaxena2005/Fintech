import { useState } from 'react';
import { authService } from '../services/api';
import SignupForm from './SignupForm';
import './LoginPage.css';

const LoginPage = ({ onLogin }) => {
  const [isLogin, setIsLogin] = useState(true);
  const [loginForm, setLoginForm] = useState({
    email: '',
    password: '',
    role: 'USER'
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setLoginForm(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      const response = await authService.login(loginForm);
      onLogin(response.data);
    } catch (err) {
      setError(err.response?.data?.message || 'Login failed. Please check your credentials.');
    } finally {
      setLoading(false);
    }
  };

  const handleSignupSuccess = (userData) => {
    onLogin(userData);
  };

  if (!isLogin) {
    return (
      <SignupForm 
        onSignupSuccess={handleSignupSuccess}
        onBackToLogin={() => setIsLogin(true)}
      />
    );
  }

  return (
    <div className="login-page">
      <div className="login-container">
        <div className="login-header">
          <h1>🏦 Fintech Account Services</h1>
          <p>Secure Banking for Everyone</p>
        </div>

        <div className="login-form-container">
          <h2>Login to Your Account</h2>
          
          {error && <div className="error">{error}</div>}
          
          <form onSubmit={handleSubmit} className="login-form">
            <div className="form-group">
              <label htmlFor="email">Email Address</label>
              <input
                type="email"
                id="email"
                name="email"
                value={loginForm.email}
                onChange={handleChange}
                required
                placeholder="Enter your email"
              />
            </div>

            <div className="form-group">
              <label htmlFor="password">Password</label>
              <input
                type="password"
                id="password"
                name="password"
                value={loginForm.password}
                onChange={handleChange}
                required
                placeholder="Enter your password"
              />
            </div>

            <div className="form-group">
              <label htmlFor="role">Login as</label>
              <select
                id="role"
                name="role"
                value={loginForm.role}
                onChange={handleChange}
              >
                <option value="USER">User</option>
                <option value="ADMIN">Admin</option>
              </select>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="btn btn-primary login-btn"
            >
              {loading ? 'Logging in...' : 'Login'}
            </button>
          </form>

          <div className="login-footer">
            <p>Don't have an account?</p>
            <button
              onClick={() => setIsLogin(false)}
              className="btn btn-link"
            >
              Create New Account
            </button>
          </div>
        </div>

        <div className="demo-accounts">
          <h3>Demo Accounts</h3>
          <div className="demo-buttons">
            <button
              onClick={() => setLoginForm({ email: 'admin@fintech.com', password: 'admin123', role: 'ADMIN' })}
              className="btn btn-outline"
            >
              Demo Admin
            </button>
            <button
              onClick={() => setLoginForm({ email: 'user@fintech.com', password: 'user123', role: 'USER' })}
              className="btn btn-outline"
            >
              Demo User
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;