import { useState, useEffect, useCallback } from 'react';
import api from '../api/axios';
import { useNavigate } from 'react-router-dom';
import {
  PieChart, Pie, Cell, Tooltip, Legend,
  BarChart, Bar, XAxis, YAxis, CartesianGrid,
  ResponsiveContainer
} from 'recharts';

const COLORS = ['#2E75B6', '#2E9E5B', '#C56432'];

export default function DashboardPage() {
  const [roomTypeData, setRoomTypeData] = useState([]);
  const [monthlyData, setMonthlyData] = useState([]);
  const [year, setYear] = useState(2025);
  const [month, setMonth] = useState(8);
  const navigate = useNavigate();

  const fetchReports = useCallback(async () => {
    try {
      // Pie Chart data — room-type revenue
      const rtRes = await api.get('/reservations/report/room-types');
      const rtArr = Object.entries(rtRes.data).map(([name, value]) => ({ name, value }));
      setRoomTypeData(rtArr);

      // Bar Chart data — monthly summary
      const monRes = await api.get(`/reservations/report/monthly?year=${year}&month=${month}`);
      const summary = Array.isArray(monRes.data)
        ? monRes.data.find(item => item.year === year && item.month === month)
        : null;
      setMonthlyData([
        { name: 'Reservations', count: summary?.totalReservations || 0 },
        { name: 'Nights',       count: summary?.totalNightsBooked || 0 },
      ]);
    } catch {
      navigate('/');
    }
  }, [year, month, navigate]);

  useEffect(() => {
    fetchReports();
  }, [fetchReports]);

  const logout = () => {
    localStorage.removeItem('token');
    navigate('/');
  };

  return (
    <div style={{ maxWidth: 900, margin: '40px auto', padding: 16 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
        <h2>📊 Occupancy Dashboard</h2>
        <div>
          <button onClick={() => navigate('/rooms')}>Rooms</button>{' '}
          <button onClick={() => navigate('/reservations')}>Reservations</button>{' '}
          <button onClick={logout}>Logout</button>
        </div>
      </div>

      <div style={{ display: 'flex', gap: 12, margin: '16px 0' }}>
        <label>
          Year:
          <input
            type="number"
            value={year}
            onChange={(e) => setYear(parseInt(e.target.value))}
            style={{ marginLeft: 6, width: 80 }}
          />
        </label>
        <label>
          Month:
          <input
            type="number"
            min="1" max="12"
            value={month}
            onChange={(e) => setMonth(parseInt(e.target.value))}
            style={{ marginLeft: 6, width: 60 }}
          />
        </label>
      </div>

      <div style={{ display: 'flex', gap: 24, flexWrap: 'wrap' }}>
        <div style={{ flex: 1, minWidth: 320, height: 300 }}>
          <h3>Monthly Summary</h3>
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={monthlyData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Bar dataKey="count" fill="#2E75B6" />
            </BarChart>
          </ResponsiveContainer>
        </div>

        <div style={{ flex: 1, minWidth: 320, height: 300 }}>
          <h3>Room Type Revenue</h3>
          <ResponsiveContainer width="100%" height="100%">
            <PieChart>
              <Pie data={roomTypeData} dataKey="value" nameKey="name" outerRadius={90} label>
                {roomTypeData.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                ))}
              </Pie>
              <Tooltip />
              <Legend />
            </PieChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
}