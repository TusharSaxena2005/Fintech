import { useState } from 'react';
import { adminService } from '../services/api';
import './UserForm.css';

const UserForm = ({ onUserCreated, user = null, onCancel = null }) => {
  const [formData, setFormData] = useState({
    fullName: user?.fullName || '',
    email: user?.email || '',
    password: '',
    role: user?.role || 'USER',
    cardNumber: user?.cardNumber || '',
    expiryDate: user?.expiryDate || '',
    cvv: user?.cvv || '',
    accountNumber: user?.accountNumber || '',
    balance: user?.balance || 0,
  });
  
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: name === 'balance' ? parseFloat(value) || 0 : value,
    }));
  };

  const generateAccountNumber = () => {
    const accountNumber = Math.floor(Math.random() * 9000000000) + 1000000000;
    setFormData(prev => ({ ...prev, accountNumber: accountNumber.toString() }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSuccess(false);

    try {
      if (user) {
        // Update existing user
        await adminService.updateUser(user.id, formData);
        setSuccess(true);
        setTimeout(() => {
          onUserCreated();
        }, 1500);
      } else {
        // Create new user
        await adminService.createUser(formData);
        setSuccess(true);
        setFormData({
          fullName: '',
          email: '',
          password: '',
          role: 'USER',
          cardNumber: '',
          expiryDate: '',
          cvv: '',
          accountNumber: '',
          balance: 0,
        });
        setTimeout(() => {
          onUserCreated();
        }, 1500);
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save user');
    } finally {
      setLoading(false);
    }
  };

  if (success) {
    return (
      <div className="success-message">
        <h2>✅ {user ? 'User Updated' : 'User Created'} Successfully!</h2>
        <p>Redirecting...</p>
      </div>
    );
  }

  return (
    <div className="user-form">
      <h2>{user ? 'Edit User' : 'Create New User'}</h2>
      
      {error && <div className="error">{error}</div>}
      
      <form onSubmit={handleSubmit}>
        <div className="form-row">
          <div className="form-group">
            <label htmlFor="fullName">Full Name *</label>
            <input
              type="text"
              id="fullName"
              name="fullName"
              value={formData.fullName}
              onChange={handleChange}
              required
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="email">Email *</label>
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

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="password">Password {!user && '*'}</label>
            <input
              type="password"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required={!user}
              placeholder={user ? 'Leave blank to keep current password' : ''}
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="role">Role</label>
            <select
              id="role"
              name="role"
              value={formData.role}
              onChange={handleChange}
            >
              <option value="USER">User</option>
              <option value="ADMIN">Admin</option>
              <option value="MANAGER">Manager</option>
            </select>
          </div>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="accountNumber">Account Number</label>
            <div className="account-number-group">
              <input
                type="text"
                id="accountNumber"
                name="accountNumber"
                value={formData.accountNumber}
                onChange={handleChange}
                placeholder="Auto-generated if empty"
              />
              <button
                type="button"
                onClick={generateAccountNumber}
                className="generate-btn"
              >
                Generate
              </button>
            </div>
          </div>
          
          <div className="form-group">
            <label htmlFor="balance">Initial Balance</label>
            <input
              type="number"
              id="balance"
              name="balance"
              value={formData.balance}
              onChange={handleChange}
              min="0"
              step="0.01"
            />
          </div>
        </div>

        <div className="form-actions">
          <button
            type="submit"
            disabled={loading}
            className="btn btn-primary"
          >
            {loading ? 'Saving...' : (user ? 'Update User' : 'Create User')}
          </button>
          
          {onCancel && (
            <button
              type="button"
              onClick={onCancel}
              className="btn btn-secondary"
            >
              Cancel
            </button>
          )}
        </div>
      </form>
    </div>
  );
};

export default UserForm;