import { useState } from 'react';
import UserList from './UserList';
import UserForm from './UserForm';
import UserDetails from './UserDetails';
import './AdminDashboard.css';

const AdminDashboard = ({ user }) => {
  const [activeTab, setActiveTab] = useState('users');
  const [selectedUser, setSelectedUser] = useState(null);
  const [refreshUsers, setRefreshUsers] = useState(false);

  const handleUserSelect = (selectedUser) => {
    setSelectedUser(selectedUser);
    setActiveTab('details');
  };

  const handleUserCreated = () => {
    setRefreshUsers(!refreshUsers);
    setActiveTab('users');
  };

  const handleUserUpdated = () => {
    setRefreshUsers(!refreshUsers);
    setActiveTab('users');
    setSelectedUser(null);
  };

  return (
    <div className="admin-dashboard">
      <div className="admin-header">
        <h2>Admin Dashboard</h2>
        <p>Manage all user accounts and transactions</p>
      </div>

      <div className="dashboard-tabs">
        <button
          className={activeTab === 'users' ? 'active' : ''}
          onClick={() => setActiveTab('users')}
        >
          👥 All Users
        </button>
        <button
          className={activeTab === 'create' ? 'active' : ''}
          onClick={() => setActiveTab('create')}
        >
          ➕ Add User
        </button>
        {selectedUser && (
          <button
            className={activeTab === 'details' ? 'active' : ''}
            onClick={() => setActiveTab('details')}
          >
            👤 User Details
          </button>
        )}
      </div>

      <div className="admin-content">
        {activeTab === 'users' && (
          <UserList
            onUserSelect={handleUserSelect}
            refresh={refreshUsers}
            isAdmin={true}
          />
        )}
        {activeTab === 'create' && (
          <UserForm
            onUserCreated={handleUserCreated}
            isAdmin={true}
          />
        )}
        {activeTab === 'details' && selectedUser && (
          <UserDetails
            user={selectedUser}
            onUserUpdated={handleUserUpdated}
            onBack={() => setActiveTab('users')}
            isAdmin={true}
          />
        )}
      </div>
    </div>
  );
};

export default AdminDashboard;