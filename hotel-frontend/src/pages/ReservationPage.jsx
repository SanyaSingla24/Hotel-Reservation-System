import { useState, useEffect, useCallback } from 'react';
import api from '../api/axios';
import { useNavigate } from 'react-router-dom';

export default function ReservationPage() {
  const [rooms, setRooms] = useState([]);
  const [reservations, setReservations] = useState([]);
  const [guestId, setGuestId] = useState('');
  const [roomId, setRoomId] = useState('');
  const [checkInDate, setCheckInDate] = useState('');
  const [checkOutDate, setCheckOutDate] = useState('');
  const [paymentMethod, setPaymentMethod] = useState('CARD');
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  const fetchData = useCallback(async () => {
    try {
      const [roomsRes, reservationsRes] = await Promise.all([
        api.get('/rooms'),
        api.get('/reservations')
      ]);
      setRooms(roomsRes.data);
      setReservations(reservationsRes.data);
    } catch {
      navigate('/');
    }
  }, [navigate]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const createReservation = async () => {
    if (!guestId || !roomId || !checkInDate || !checkOutDate) {
      setMessage('❌ Please fill all fields');
      return;
    }
    try {
      await api.post('/reservations', {
        guestId: parseInt(guestId),
        roomId: parseInt(roomId),
        checkInDate,
        checkOutDate,
        paymentMethod
      });
      setMessage('✅ Reservation created successfully!');
      setGuestId('');
      setRoomId('');
      setCheckInDate('');
      setCheckOutDate('');
      fetchData();
    } catch (err) {
      setMessage('❌ ' + (err.response?.data?.error || err.response?.data || 'Failed to create reservation'));
    }
  };

  const cancelReservation = async (id) => {
    await api.delete(`/reservations/${id}`);
    fetchData();
  };

  const logout = () => {
    localStorage.removeItem('token');
    navigate('/');
  };

  const availableRooms = rooms.filter(r => r.available);

  return (
    <div style={{ maxWidth: 900, margin: '40px auto', padding: 16 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
        <h2>📅 Reservations</h2>
        <div>
          <button onClick={() => navigate('/rooms')}>Rooms</button>{' '}
          <button onClick={() => navigate('/dashboard')}>Dashboard</button>{' '}
          <button onClick={logout}>Logout</button>
        </div>
      </div>

      <div style={{ border: '1px solid #ccc', borderRadius: 8, padding: 16, margin: '16px 0' }}>
        <h3>Create New Reservation</h3>
        <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap', marginBottom: 8 }}>
          <input
            placeholder="Guest ID"
            value={guestId}
            onChange={(e) => setGuestId(e.target.value)}
            style={{ width: 90 }}
          />
          <select value={roomId} onChange={(e) => setRoomId(e.target.value)}>
            <option value="">Select Available Room</option>
            {availableRooms.map(r => (
              <option key={r.id} value={r.id}>
                Room {r.roomNumber} — {r.roomType} (₹{r.pricePerNight}/night)
              </option>
            ))}
          </select>
          <input
            type="date"
            value={checkInDate}
            onChange={(e) => setCheckInDate(e.target.value)}
          />
          <input
            type="date"
            value={checkOutDate}
            onChange={(e) => setCheckOutDate(e.target.value)}
          />
          <select value={paymentMethod} onChange={(e) => setPaymentMethod(e.target.value)}>
            <option value="CARD">CARD</option>
            <option value="CASH">CASH</option>
            <option value="UPI">UPI</option>
            <option value="NET_BANKING">NET_BANKING</option>
          </select>
          <button onClick={createReservation}>Book Now</button>
        </div>
        {message && <p>{message}</p>}
      </div>

      <table border="1" cellPadding="8" style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr>
            <th>ID</th><th>Guest ID</th><th>Room</th><th>Check-In</th><th>Check-Out</th><th>Total Cost</th><th>Status</th><th>Action</th>
          </tr>
        </thead>
        <tbody>
          {reservations.map(r => (
            <tr key={r.id}>
              <td>{r.id}</td>
              <td>{r.guest?.id}</td>
              <td>{r.room?.roomNumber}</td>
              <td>{r.checkInDate}</td>
              <td>{r.checkOutDate}</td>
              <td>₹{r.totalCost}</td>
              <td>{r.status}</td>
              <td><button onClick={() => cancelReservation(r.id)}>Cancel</button></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}