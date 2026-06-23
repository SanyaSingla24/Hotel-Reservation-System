import { useState, useEffect, useCallback } from 'react';
import api from '../api/axios';
import { useNavigate } from 'react-router-dom';

export default function RoomPage() {
  const [rooms, setRooms] = useState([]);
  const [roomNumber, setRoomNumber] = useState('');
  const [roomType, setRoomType] = useState('SINGLE');
  const [price, setPrice] = useState('');
  const [floor, setFloor] = useState('');
  const navigate = useNavigate();

  const fetchRooms = useCallback(async () => {
    try {
     const res = await api.get('/rooms');
     setRooms(Array.isArray(res.data) ? res.data : []);
    } catch {
      navigate('/');
    }
  }, [navigate]);

  useEffect(() => {
    fetchRooms();
  }, [fetchRooms]);

  const addRoom = async () => {
    await api.post('/rooms', {
      roomNumber,
      roomType,
      pricePerNight: parseFloat(price),
      floor: parseInt(floor)
    });
    setRoomNumber('');
    setPrice('');
    setFloor('');
    fetchRooms();
  };

  const deleteRoom = async (id) => {
    await api.delete(`/rooms/${id}`);
    fetchRooms();
  };

  const logout = () => {
    localStorage.removeItem('token');
    navigate('/');
  };

  return (
    <div style={{ maxWidth: 800, margin: '40px auto', padding: 16 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
        <h2>🏨 Rooms</h2>
        <div>
         <button onClick={() => navigate('/reservations')}>Reservations</button>{' '}
         <button onClick={() => navigate('/dashboard')}>Dashboard</button>{' '}
          <button onClick={logout}>Logout</button>
        </div>
      </div>

      <div style={{ display: 'flex', gap: 8, margin: '16px 0' }}>
        <input placeholder="Room Number" value={roomNumber} onChange={e => setRoomNumber(e.target.value)} />
        <select value={roomType} onChange={e => setRoomType(e.target.value)}>
          <option value="SINGLE">SINGLE</option>
          <option value="DOUBLE">DOUBLE</option>
          <option value="SUITE">SUITE</option>
        </select>
        <input placeholder="Price/Night" value={price} onChange={e => setPrice(e.target.value)} />
        <input placeholder="Floor" value={floor} onChange={e => setFloor(e.target.value)} />
        <button onClick={addRoom}>+ Add Room</button>
      </div>

      <table border="1" cellPadding="8" style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr>
            <th>ID</th><th>Room No.</th><th>Type</th><th>Price</th><th>Floor</th><th>Available</th><th>Action</th>
          </tr>
        </thead>
        <tbody>
          {rooms.map(r => (
            <tr key={r.id}>
              <td>{r.id}</td>
              <td>{r.roomNumber}</td>
              <td>{r.roomType}</td>
              <td>₹{r.pricePerNight}</td>
              <td>{r.floor}</td>
              <td>{r.available ? '✅ Yes' : '❌ No'}</td>
              <td><button onClick={() => deleteRoom(r.id)}>🗑 Delete</button></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}