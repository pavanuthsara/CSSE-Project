import React, { useEffect, useState } from 'react';
import api, { getPaymentsDashboard } from '../services/api';

const PaymentsDashboard = () => {
  const [data, setData] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) return;
    getPaymentsDashboard(token)
      .then(res => setData(res.data))
      .catch(err => console.error(err));
  }, []);

  return (
    <div className="container">
      <h2>Payment Dashboard</h2>
      {!data && <p>Loading...</p>}
      {data && (
        <div>
          <h3>Recent Payments</h3>
          <ul>
            {(data.recent || []).map(p => (
              <li key={p.id}>{p.patientName} - {p.amount} - {p.status}</li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
};

export default PaymentsDashboard;
