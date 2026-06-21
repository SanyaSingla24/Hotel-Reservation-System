import { useState } from 'react';
import api from '../api/axios';
import { useNavigate } from 'react-router-dom';

export default function LoginPage() {
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [password, setPassword] = useState('');
  const [isReg, setIsReg] = useState(false);
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async () => {
    try {
      if (isReg) {
        await api.post('/guests/register', { fullName, email, phone, password });
        setMessage('✅ Registered! Now login.');
        setIsReg(false);
      } else {
        const res = await api.post('/guests/login', { email, password });
        localStorage.setItem('token', res.data.token);
        navigate('/rooms');
      }
    } catch (err) {
      setMessage('❌ ' + (err.response?.data?.error || err.response?.data || 'Something went wrong'));
    }
  };

  return (
    <div style={{ maxWidth: 360, margin: '80px auto', padding: 24, border: '1px solid #ccc', borderRadius: 8 }}>
      <h2>🏨 Hotel System</h2>
      <h3>{isReg ? 'Register' : 'Login'}</h3>

      {isReg && (
        <input
          placeholder="Full Name"
          value={fullName}
          onChange={(e) => setFullName(e.target.value)}
          style={{ width: '100%', padding: 8, marginBottom: 8 }}
        />
      )}

      <input
        placeholder="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        style={{ width: '100%', padding: 8, marginBottom: 8 }}
      />

      {isReg && (
        <input
          placeholder="Phone"
          value={phone}
          onChange={(e) => setPhone(e.target.value)}
          style={{ width: '100%', padding: 8, marginBottom: 8 }}
        />
      )}

      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        style={{ width: '100%', padding: 8, marginBottom: 8 }}
      />

      <button onClick={handleSubmit} style={{ width: '100%', padding: 10 }}>
        {isReg ? 'Register' : 'Login'}
      </button>

      <p style={{ marginTop: 12 }}>
        {isReg ? 'Already have an account?' : "No account?"}{' '}
        <span
          style={{ color: 'blue', cursor: 'pointer' }}
          onClick={() => setIsReg(!isReg)}
        >
          {isReg ? 'Login' : 'Register'}
        </span>
      </p>

      {message && <p>{message}</p>}
    </div>
  );
}