import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import AuthLayout from './layouts/AuthLayout'
import DashboardLayout from './layouts/DashboardLayout'
import Landing from './pages/Landing'
import Login from './pages/Login'
import Register from './pages/Register'
import Dashboard from './pages/Dashboard'
import HealthProfile from './pages/HealthProfile'
import Reports from './pages/Reports'
import SymptomAnalyzer from './pages/SymptomAnalyzer'
import AIChat from './pages/AIChat'
import Nutrition from './pages/Nutrition'
import Fitness from './pages/Fitness'
import Wellness from './pages/Wellness'
import DigitalHealthTwin from './pages/DigitalHealthTwin'
import Settings from './pages/Settings'
import { AuthProvider } from './context/AuthContext'

function App() {
  return (
    <AuthProvider>
      <Router>
      <Routes>
        <Route path="/" element={<Landing />} />
        
        <Route element={<AuthLayout />}>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
        </Route>

        <Route element={<DashboardLayout />}>
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/profile" element={<HealthProfile />} />
          <Route path="/reports" element={<Reports />} />
          <Route path="/symptoms" element={<SymptomAnalyzer />} />
          <Route path="/chat" element={<AIChat />} />
          <Route path="/nutrition" element={<Nutrition />} />
          <Route path="/fitness" element={<Fitness />} />
          <Route path="/wellness" element={<Wellness />} />
          <Route path="/twin" element={<DigitalHealthTwin />} />
          <Route path="/settings" element={<Settings />} />
        </Route>

        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </Router>
    </AuthProvider>
  )
}

export default App
