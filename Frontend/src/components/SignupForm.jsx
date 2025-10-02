import { useState } from 'react';
import { authService } from '../services/api';
import './SignupForm.css';

const SignupForm = ({ onSignupSuccess, onBackToLogin }) => {
  const [formData, setFormData] = useState({
    fullName: '',
    email: '',
    password: '',
    confirmPassword: '',
    role: 'USER'
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const generateAccountNumber = () => {
    return Math.floor(Math.random() * 9000000000) + 1000000000;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    if (formData.password !== formData.confirmPassword) {
      setError('Passwords do not match');
      setLoading(false);
      return;
    }

    try {
      const signupData = {
        fullName: formData.fullName,
        email: formData.email,
        password: formData.password,
        role: formData.role,
        accountNumber: generateAccountNumber().toString(),
        balance: 0
      };

      const response = await authService.signup(signupData);
      onSignupSuccess(response.data);
    } catch (err) {
      setError(err.response?.data?.message || 'Signup failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="signup-page">
      <div className="signup-container">
        <div className="signup-header">
          <h1>🏦 Create New Account</h1>
          <p>Join Fintech Account Services today</p>
        </div>

        <div className="signup-form-container">
          {error && <div className="error">{error}</div>}
          
          <form onSubmit={handleSubmit} className="signup-form">
            <div className="form-group">
              <label htmlFor="fullName">Full Name *</label>
              <input
                type="text"
                id="fullName"
                name="fullName"
                value={formData.fullName}
                onChange={handleChange}
                required
                placeholder="Enter your full name"
              />
            </div>

            <div className="form-group">
              <label htmlFor="email">Email Address *</label>
              <input
                type="email"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                required
                placeholder="Enter your email"
              />
            </div>

            <div className="form-row">
              <div className="form-group">
                <label htmlFor="password">Password *</label>
                <input
                  type="password"
                  id="password"
                  name="password"
                  value={formData.password}
                  onChange={handleChange}
                  required
                  placeholder="Enter password"
                  minLength="6"
                />
              </div>

              <div className="form-group">
                <label htmlFor="confirmPassword">Confirm Password *</label>
                <input
                  type="password"
                  id="confirmPassword"
                  name="confirmPassword"
                  value={formData.confirmPassword}
                  onChange={handleChange}
                  required
                  placeholder="Confirm password"
                  minLength="6"
                />
              </div>
            </div>

            <div className="form-group">
              <label htmlFor="role">Account Type</label>
              <select
                id="role"
                name="role"
                value={formData.role}
                onChange={handleChange}
              >
                <option value="USER">Regular User Account</option>
                <option value="ADMIN">Admin Account</option>
              </select>
            </div>

            <div className="form-actions">
              <button
                type="submit"
                disabled={loading}
                className="btn btn-primary signup-btn"
              >
                {loading ? 'Creating Account...' : 'Create Account'}
              </button>
              
              <button
                type="button"
                onClick={onBackToLogin}
                className="btn btn-secondary"
              >
                Back to Login
              </button>
            </div>
          </form>

          <div className="signup-info">
            <h3>What you get:</h3>
            <ul>
              <li>✅ Secure account with instant setup</li>
              <li>✅ Add and withdraw money anytime</li>
              <li>✅ Track all your transactions</li>
              <li>✅ Real-time balance updates</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SignupForm;