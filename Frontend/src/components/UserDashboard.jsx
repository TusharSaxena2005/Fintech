import { useState, useEffect } from 'react';
import { accountService } from '../services/api';
import './UserDashboard.css';

const UserDashboard = ({ user }) => {
  const [account, setAccount] = useState(user);
  const [transactions, setTransactions] = useState([]);
  const [activeTab, setActiveTab] = useState('overview');
  const [transactionForm, setTransactionForm] = useState({
    amount: '',
    description: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  useEffect(() => {
    fetchAccountData();
    fetchTransactions();
  }, []);

  const fetchAccountData = async () => {
    try {
      const response = await accountService.getAccount(user.id);
      setAccount(response.data);
    } catch (err) {
      setError('Failed to fetch account data');
    }
  };

  const fetchTransactions = async () => {
    try {
      const response = await accountService.getTransactions(user.id);
      setTransactions(response.data);
    } catch (err) {
      setError('Failed to fetch transactions');
    }
  };

  const handleTransactionSubmit = async (type) => {
    setLoading(true);
    setError(null);
    setSuccess(null);

    try {
      const transactionData = {
        type: type,
        amount: parseFloat(transactionForm.amount),
        description: transactionForm.description || `Money ${type.toLowerCase()}ed`
      };

      if (type === 'DEPOSIT') {
        await accountService.depositMoney(user.id, transactionData);
        setSuccess(`$${transactionForm.amount} deposited successfully!`);
      } else {
        await accountService.withdrawMoney(user.id, transactionData);
        setSuccess(`$${transactionForm.amount} withdrawn successfully!`);
      }

      setTransactionForm({ amount: '', description: '' });
      fetchAccountData();
      fetchTransactions();
    } catch (err) {
      setError(err.response?.data?.message || `Failed to ${type.toLowerCase()} money`);
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setTransactionForm(prev => ({
      ...prev,
      [name]: value
    }));
  };

  return (
    <div className="user-dashboard">
      <div className="dashboard-tabs">
        <button
          className={activeTab === 'overview' ? 'active' : ''}
          onClick={() => setActiveTab('overview')}
        >
          Account Overview
        </button>
        <button
          className={activeTab === 'transactions' ? 'active' : ''}
          onClick={() => setActiveTab('transactions')}
        >
          Add/Withdraw Money
        </button>
        <button
          className={activeTab === 'history' ? 'active' : ''}
          onClick={() => setActiveTab('history')}
        >
          Transaction History
        </button>
      </div>

      {error && <div className="error">{error}</div>}
      {success && <div className="success">{success}</div>}

      {activeTab === 'overview' && (
        <div className="overview-tab">
          <div className="account-card">
            <h2>Your Account</h2>
            <div className="account-info">
              <div className="balance-section">
                <h3>Current Balance</h3>
                <div className="balance-amount">${account.balance?.toFixed(2) || '0.00'}</div>
              </div>
              <div className="account-details">
                <p><strong>Account Number:</strong> {account.accountNumber}</p>
                <p><strong>Account Holder:</strong> {account.fullName}</p>
                <p><strong>Email:</strong> {account.email}</p>
              </div>
            </div>
          </div>

          <div className="quick-actions">
            <h3>Quick Actions</h3>
            <div className="action-buttons">
              <button
                onClick={() => setActiveTab('transactions')}
                className="btn btn-primary"
              >
                💰 Add Money
              </button>
              <button
                onClick={() => setActiveTab('transactions')}
                className="btn btn-secondary"
              >
                💸 Withdraw Money
              </button>
              <button
                onClick={() => setActiveTab('history')}
                className="btn btn-outline"
              >
                📊 View History
              </button>
            </div>
          </div>
        </div>
      )}

      {activeTab === 'transactions' && (
        <div className="transactions-tab">
          <div className="transaction-form">
            <h2>Add or Withdraw Money</h2>
            
            <div className="form-group">
              <label htmlFor="amount">Amount ($)</label>
              <input
                type="number"
                id="amount"
                name="amount"
                value={transactionForm.amount}
                onChange={handleInputChange}
                min="0.01"
                step="0.01"
                placeholder="Enter amount"
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="description">Description (Optional)</label>
              <input
                type="text"
                id="description"
                name="description"
                value={transactionForm.description}
                onChange={handleInputChange}
                placeholder="What's this transaction for?"
              />
            </div>

            <div className="transaction-buttons">
              <button
                onClick={() => handleTransactionSubmit('DEPOSIT')}
                disabled={loading || !transactionForm.amount}
                className="btn btn-success"
              >
                {loading ? 'Processing...' : '💰 Deposit Money'}
              </button>
              <button
                onClick={() => handleTransactionSubmit('WITHDRAWAL')}
                disabled={loading || !transactionForm.amount}
                className="btn btn-warning"
              >
                {loading ? 'Processing...' : '💸 Withdraw Money'}
              </button>
            </div>
          </div>
        </div>
      )}

      {activeTab === 'history' && (
        <div className="history-tab">
          <h2>Transaction History</h2>
          {transactions.length === 0 ? (
            <div className="no-transactions">
              <p>No transactions yet. Start by adding money to your account!</p>
            </div>
          ) : (
            <div className="transactions-list">
              {transactions.map(transaction => (
                <div key={transaction.id} className={`transaction-item ${transaction.type.toLowerCase()}`}>
                  <div className="transaction-details">
                    <div className="transaction-type">
                      {transaction.type === 'DEPOSIT' ? '💰' : '💸'} {transaction.type}
                    </div>
                    <div className="transaction-description">{transaction.description}</div>
                    <div className="transaction-date">
                      {new Date(transaction.transactionDate).toLocaleString()}
                    </div>
                  </div>
                  <div className="transaction-amount">
                    <span className={transaction.type === 'DEPOSIT' ? 'positive' : 'negative'}>
                      {transaction.type === 'DEPOSIT' ? '+' : '-'}${transaction.amount?.toFixed(2)}
                    </span>
                    <div className="balance-after">
                      Balance: ${transaction.balanceAfter?.toFixed(2)}
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default UserDashboard;