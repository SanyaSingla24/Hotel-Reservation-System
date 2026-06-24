import { useState, useEffect, useCallback } from 'react';
import api from '../api/axios';
import { useNavigate } from 'react-router-dom';

export default function ReservationPage() {
  const [rooms, setRooms] = useState([]);
  const [reservations, setReservations] = useState([]);
  const [guests, setGuests] = useState([]);
  const [guestId, setGuestId] = useState('');
  const [roomId, setRoomId] = useState('');
  const [checkInDate, setCheckInDate] = useState('');
  const [checkOutDate, setCheckOutDate] = useState('');
  const [paymentMethod, setPaymentMethod] = useState('CARD');
  const [message, setMessage] = useState('');

  const navigate = useNavigate();

  const fetchData = useCallback(async () => {
    try {
      const [roomsRes, reservationsRes, guestsRes] = await Promise.all([
        api.get('/rooms'),
        api.get('/reservations'),
        api.get('/guests')
      ]);

      console.log('ROOMS RESPONSE:', roomsRes.data);
      console.log('RESERVATIONS RESPONSE:', reservationsRes.data);

      const roomsData = Array.isArray(roomsRes.data)
        ? roomsRes.data
        : roomsRes.data?.content ||
        roomsRes.data?.rooms ||
        [];

      const reservationsData = Array.isArray(reservationsRes.data)
        ? reservationsRes.data
        : reservationsRes.data?.content ||
        reservationsRes.data?.reservations ||
        [];

      const guestsData = Array.isArray(guestsRes.data)
        ? guestsRes.data
        : guestsRes.data?.content ||
        [];

      setRooms(roomsData);
      setReservations(reservationsData);
      setGuests(guestsData);
    } catch (err) {
      console.error(err);
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
      const res = await api.post('/reservations', {
        guestId: parseInt(guestId),
        roomId: parseInt(roomId),
        checkInDate,
        checkOutDate,
        paymentMethod
      });

      const newReservationId = res.data?.id;
      if (newReservationId) {
        await api.post(`/payments?reservationId=${newReservationId}&paymentMethod=${paymentMethod}`);
      }

      setMessage('✅ Reservation created and paid successfully!');

      setGuestId('');
      setRoomId('');
      setCheckInDate('');
      setCheckOutDate('');

      fetchData();
    } catch (err) {
      setMessage(
        '❌ ' +
        (err.response?.data?.error ||
          err.response?.data ||
          'Failed to create reservation')
      );
    }
  };

  const cancelReservation = async (id) => {
    try {
      await api.delete(`/reservations/${id}`);
      fetchData();
    } catch (err) {
      console.error(err);
    }
  };

  const logout = () => {
    localStorage.removeItem('token');
    navigate('/');
  };

  const availableRooms = Array.isArray(rooms)
    ? rooms.filter((room) => room.available)
    : [];

  return (
    <div style={{ maxWidth: 900, margin: '40px auto', padding: 16 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
        <h2>📅 Reservations</h2>

        <div>
          <button onClick={() => navigate('/rooms')}>Rooms</button>{' '}
          <button onClick={() => navigate('/guests')}>Guests</button>{' '}
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
        <h3>Create New Reservation</h3>

        <div
          style={{
            display: 'flex',
            gap: 8,
            flexWrap: 'wrap',
            marginBottom: 8
          }}
        >
          <select
            value={guestId}
            onChange={(e) => setGuestId(e.target.value)}
          >
            <option value="">Select Guest</option>
            {guests.map((guest) => (
              <option key={guest.id} value={guest.id}>
                {guest.fullName} (ID: {guest.id})
              </option>
            ))}
          </select>

          <select
            value={roomId}
            onChange={(e) => setRoomId(e.target.value)}
          >
            <option value="">Select Available Room</option>

            {availableRooms.map((room) => (
              <option key={room.id} value={room.id}>
                Room {room.roomNumber} — {room.roomType} (₹
                {room.pricePerNight}/night)
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

          <select
            value={paymentMethod}
            onChange={(e) => setPaymentMethod(e.target.value)}
          >
            <option value="CARD">CARD</option>
            <option value="CASH">CASH</option>
            <option value="UPI">UPI</option>
            <option value="NET_BANKING">NET_BANKING</option>
          </select>

          <button onClick={createReservation}>
            Book Now
          </button>
        </div>

        {message && <p>{message}</p>}
      </div>

      <table
        border="1"
        cellPadding="8"
        style={{
          width: '100%',
          borderCollapse: 'collapse'
        }}
      >
        <thead>
          <tr>
            <th>ID</th>
            <th>Guest ID</th>
            <th>Room</th>
            <th>Check-In</th>
            <th>Check-Out</th>
            <th>Total Cost</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>

        <tbody>
          {Array.isArray(reservations) &&
            reservations.map((reservation) => (
              <tr key={reservation.id}>
                <td>{reservation.id}</td>
                <td>{reservation.guest?.id}</td>
                <td>{reservation.room?.roomNumber}</td>
                <td>{reservation.checkInDate}</td>
                <td>{reservation.checkOutDate}</td>
                <td>₹{reservation.totalCost}</td>
                <td>{reservation.status}</td>

                <td>
                  <button
                    onClick={() =>
                      cancelReservation(reservation.id)
                    }
                  >
                    Cancel
                  </button>
                </td>
              </tr>
            ))}
        </tbody>
      </table>
    </div>
  );
}