import React, { useEffect, useState } from 'react';
import { getPatientInvoices } from '../services/api';

const PatientPayments = ({ patientId }) => {
  const [invoices, setInvoices] = useState([]);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token || !patientId) return;
    getPatientInvoices(patientId, token)
      .then(res => setInvoices(res.data))
      .catch(err => console.error(err));
  }, [patientId]);

  return (
    <div>
      <h3>Outstanding Invoices</h3>
      <table>
        <thead>
          <tr><th>Select</th><th>Invoice</th><th>Amount</th><th>Status</th></tr>
        </thead>
        <tbody>
          {invoices.map(inv => (
            <tr key={inv.id}>
              <td><input type="checkbox" value={inv.id} /></td>
              <td>{inv.id}</td>
              <td>{inv.amount}</td>
              <td>{inv.status}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default PatientPayments;
