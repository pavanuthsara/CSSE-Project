import React, { useEffect, useState } from 'react';
import { getReceipt } from '../services/api';

const PaymentSuccess = ({ paymentId }) => {
  const [payment, setPayment] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token || !paymentId) return;
    getReceipt(paymentId, token).then(res => setPayment(res.data)).catch(err => console.error(err));
  }, [paymentId]);

  if (!payment) return <div>Loading receipt...</div>;

  return (
    <div>
      <h3>Payment Successful</h3>
      <div>Patient: {payment.patientName}</div>
      <div>Amount: {payment.amount}</div>
      <div>Status: {payment.status}</div>
      <button onClick={() => window.print()}>Download Receipt (Print to PDF)</button>
    </div>
  );
};

export default PaymentSuccess;
