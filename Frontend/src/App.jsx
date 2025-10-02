import { useState, useEffect } from 'react'
import './App.css'
import LoginPage from './components/LoginPage'
import UserDashboard from './components/UserDashboard'
import AdminDashboard from './components/AdminDashboard'

function App() {
  const [user, setUser] = useState(null)
  const [isAuthenticated, setIsAuthenticated] = useState(false)

  useEffect(() => {
    // Check if user is already logged in (from localStorage)
    const savedUser = localStorage.getItem('fintechUser')
    if (savedUser) {
      const userData = JSON.parse(savedUser)
      setUser(userData)
      setIsAuthenticated(true)
    }
  }, [])

  const handleLogin = (userData) => {
    setUser(userData)
    setIsAuthenticated(true)
    localStorage.setItem('fintechUser', JSON.stringify(userData))
  }

  const handleLogout = () => {
    setUser(null)
    setIsAuthenticated(false)
    localStorage.removeItem('fintechUser')
  }

  if (!isAuthenticated) {
    return <LoginPage onLogin={handleLogin} />
  }

  return (
    <div className="app">
      <header className="app-header">
        <h1>🏦 Fintech Account Services</h1>
        <div className="user-info">
          <span>Welcome, {user.fullName}</span>
          <span className={`role-badge ${user.role.toLowerCase()}`}>
            {user.role}
          </span>
          <button onClick={handleLogout} className="btn btn-secondary">
            Logout
          </button>
        </div>
      </header>

      <main className="app-main">
        {user.role === 'ADMIN' ? (
          <AdminDashboard user={user} />
        ) : (
          <UserDashboard user={user} />
        )}
      </main>
    </div>
  )
}

export default App
