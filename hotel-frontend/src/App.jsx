import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import RoomPage from './pages/RoomPage';
import ReservationPage from './pages/ReservationPage';
import DashboardPage from './pages/DashboardPage';
import GuestPage from './pages/GuestPage';

// PrivateRoute — redirects to / if no JWT token found in localStorage
function PrivateRoute({ children }) {
  return localStorage.getItem('token') ? children : <Navigate to="/" />;
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/rooms" element={<PrivateRoute><RoomPage /></PrivateRoute>} />
        <Route path="/reservations" element={<PrivateRoute><ReservationPage /></PrivateRoute>} />
        <Route path="/guests" element={<PrivateRoute><GuestPage /></PrivateRoute>} />
        <Route path="/dashboard" element={<PrivateRoute><DashboardPage /></PrivateRoute>} />
      </Routes>
    </BrowserRouter>
  );
}