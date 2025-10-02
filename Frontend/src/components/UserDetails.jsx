import { useState } from 'react';
import { adminService } from '../services/api';
import UserForm from './UserForm';
import './UserDetails.css';

const UserDetails = ({ user, onUserUpdated, onBack }) => {
  const [editing, setEditing] = useState(false);
  const [updatingBalance, setUpdatingBalance] = useState(false);
  const [newBalance, setNewBalance] = useState(user.balance || 0);
  const [error, setError] = useState(null);

  const handleBalanceUpdate = async () => {
    setUpdatingBalance(true);
    setError(null);
    
    try {
      await adminService.updateBalance(user.id, newBalance);
      onUserUpdated();
    } catch (err) {
      setError('Failed to update balance: ' + (err.response?.data?.message || err.message));
    } finally {
      setUpdatingBalance(false);
    }
  };

  const handleDelete = async () => {
    if (window.confirm(`Are you sure you want to delete ${user.fullName}?`)) {
      try {
        await adminService.deleteUser(user.id);
        onUserUpdated(); // This will refresh and go back to list
      } catch (err) {
        setError('Failed to delete user: ' + (err.response?.data?.message || err.message));
      }
    }
  };

  if (editing) {
    return (
      <UserForm
        user={user}
        onUserCreated={onUserUpdated}
        onCancel={() => setEditing(false)}
      />
    );
  }

  return (
    <div className="user-details">
      <div className="user-details-header">
        <button onClick={onBack} className="back-btn">
          ← Back to Users
        </button>
        <h2>User Details</h2>
      </div>

      {error && <div className="error">{error}</div>}

      <div className="user-details-content">
        <div className="user-info-section">
          <h3>Personal Information</h3>
          <div className="info-grid">
            <div className="info-item">
              <label>Full Name</label>
              <span>{user.fullName}</span>
            </div>
            <div className="info-item">
              <label>Email</label>
              <span>{user.email}</span>
            </div>
            <div className="info-item">
              <label>Role</label>
              <span className={`role-badge ${user.role.toLowerCase()}`}>
                {user.role}
              </span>
            </div>
            <div className="info-item">
              <label>User ID</label>
              <span>{user.id}</span>
            </div>
          </div>
        </div>

        <div className="account-info-section">
          <h3>Account Information</h3>
          <div className="info-grid">
            <div className="info-item">
              <label>Account Number</label>
              <span>{user.accountNumber}</span>
            </div>
            <div className="info-item">
              <label>Current Balance</label>
              <span className="balance">${user.balance?.toFixed(2) || '0.00'}</span>
            </div>
          </div>

          <div className="balance-update">
            <h4>Update Balance</h4>
            <div className="balance-update-controls">
              <input
                type="number"
                value={newBalance}
                onChange={(e) => setNewBalance(parseFloat(e.target.value) || 0)}
                step="0.01"
                min="0"
              />
              <button
                onClick={handleBalanceUpdate}
                disabled={updatingBalance}
                className="btn btn-primary"
              >
                {updatingBalance ? 'Updating...' : 'Update Balance'}
              </button>
            </div>
          </div>
        </div>

        <div className="user-actions">
          <button
            onClick={() => setEditing(true)}
            className="btn btn-primary"
          >
            Edit User
          </button>
          <button
            onClick={handleDelete}
            className="btn btn-danger"
          >
            Delete User
          </button>
        </div>
      </div>
    </div>
  );
};

export default UserDetails;