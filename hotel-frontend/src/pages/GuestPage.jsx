import { useState, useEffect, useCallback } from 'react';
import api from '../api/axios';
import { useNavigate } from 'react-router-dom';

export default function GuestPage() {
  const [guests, setGuests] = useState([]);
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [password, setPassword] = useState('pass123'); // Default password for guest accounts
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  const fetchGuests = useCallback(async () => {
    try {
      const res = await api.get('/guests');
      setGuests(Array.isArray(res.data) ? res.data : []);
    } catch {
      navigate('/');
    }
  }, [navigate]);

  useEffect(() => {
    fetchGuests();
  }, [fetchGuests]);

  const addGuest = async () => {
    if (!fullName || !email || !phone) {
      setMessage('❌ Please fill all fields');
      return;
    }
    try {
      await api.post('/guests/register', {
        fullName,
        email,
        phone,
        password
      });
      setMessage('✅ Guest registered successfully!');
      setFullName('');
      setEmail('');
      setPhone('');
      setPassword('pass123');
      fetchGuests();
    } catch (err) {
      setMessage('❌ ' + (err.response?.data?.error || err.response?.data || 'Failed to register guest'));
    }
  };

  const deleteGuest = async (id) => {
    try {
      await api.delete(`/guests/${id}`);
      fetchGuests();
    } catch (err) {
      console.error(err);
    }
  };

  const logout = () => {
    localStorage.removeItem('token');
    navigate('/');
  };

  return (
    <div style={{ maxWidth: 800, margin: '40px auto', padding: 16 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
        <h2>👥 Guest Management</h2>
        <div>
          <button onClick={() => navigate('/rooms')}>Rooms</button>{' '}
          <button onClick={() => navigate('/reservations')}>Reservations</button>{' '}
          <button onClick={() => navigate('/dashboard')}>Dashboard</button>{' '}
          <button onClick={logout}>Logout</button>
        </div>
      </div>

      <div
        style={{
          border: '1px solid #ccc',
          borderRadius: 8,
          padding: 16,
          margin: '16px 0'
        }}
      >
        <h3>Register New Guest</h3>
        <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap', marginBottom: 8 }}>
          <input
            placeholder="Full Name"
            value={fullName}
            onChange={(e) => setFullName(e.target.value)}
          />
          <input
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <input
            placeholder="Phone"
            value={phone}
            onChange={(e) => setPhone(e.target.value)}
          />
          <input
            type="text"
            placeholder="Password (Default: pass123)"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            style={{ width: 150 }}
          />
          <button onClick={addGuest}>+ Add Guest</button>
        </div>
        {message && <p>{message}</p>}
      </div>

      <table border="1" cellPadding="8" style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr>
            <th>ID</th>
            <th>Full Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {guests.map((g) => (
            <tr key={g.id}>
              <td>{g.id}</td>
              <td>{g.fullName}</td>
              <td>{g.email}</td>
              <td>{g.phone}</td>
              <td>
                <button onClick={() => deleteGuest(g.id)}>🗑 Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
