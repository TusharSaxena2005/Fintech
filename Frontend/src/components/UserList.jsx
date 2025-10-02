import { useState, useEffect } from 'react';
import { adminService } from '../services/api';
import './UserList.css';

const UserList = ({ onUserSelect, refresh }) => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    fetchUsers();
  }, [refresh]);

  const fetchUsers = async () => {
    try {
      setLoading(true);
      const response = await adminService.getAllUsers();
      setUsers(response.data);
      setError(null);
    } catch (err) {
      setError('Failed to fetch users: ' + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id, fullName) => {
    if (window.confirm(`Are you sure you want to delete ${fullName}?`)) {
      try {
        await adminService.deleteUser(id);
        fetchUsers(); // Refresh the list
      } catch (err) {
        setError('Failed to delete user: ' + (err.response?.data?.message || err.message));
      }
    }
  };

  const filteredUsers = users.filter(user =>
    user.fullName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    user.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
    user.role.toLowerCase().includes(searchTerm.toLowerCase())
  );

  if (loading) return <div className="loading">Loading users...</div>;
  if (error) return <div className="error">{error}</div>;

  return (
    <div className="user-list">
      <div className="user-list-header">
        <h2>Users</h2>
        <div className="search-box">
          <input
            type="text"
            placeholder="Search users..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
      </div>

      {filteredUsers.length === 0 ? (
        <div className="no-users">
          {searchTerm ? 'No users match your search.' : 'No users found.'}
        </div>
      ) : (
        <div className="users-grid">
          {filteredUsers
            .filter(user => user.role !== 'ADMIN')
            .map(user => (
              <div key={user.id} className="user-card">
                <div className="user-card-header">
                  <h3>{user.fullName}</h3>
                  <span className={`role-badge ${user.role.toLowerCase()}`}>
                    {user.role}
                  </span>
                </div>
                <div className="user-card-body">
                  <p><strong>Email:</strong> {user.email}</p>
                  <p><strong>Account:</strong> {user.accountNumber}</p>
                  <p><strong>Balance:</strong> ${user.balance?.toFixed(2) || '0.00'}</p>
                </div>
                <div className="user-card-actions">
                  <button 
                    onClick={() => onUserSelect(user)}
                    className="btn btn-primary"
                  >
                    View Details
                  </button>
                  <button 
                    onClick={() => handleDelete(user.id, user.fullName)}
                    className="btn btn-danger"
                  >
                    Delete
                  </button>
                </div>
              </div>
            ))}
        </div>
      )}
    </div>
  );
};

export default UserList;