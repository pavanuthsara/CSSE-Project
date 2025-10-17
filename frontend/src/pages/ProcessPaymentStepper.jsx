import React, { useState } from 'react';
import { processPayment } from '../services/api';

const ProcessPaymentStepper = ({ patientId, invoiceIds, onSuccess }) => {
  const [step, setStep] = useState(1);
  const [method, setMethod] = useState('CASH');
  const [amount, setAmount] = useState('');
  const [card, setCard] = useState({ number: '', exp: '', cvv: '' });

  const token = localStorage.getItem('token');

  const submit = () => {
    const payload = {
      patientId,
      invoiceIds,
      method,
      amount: parseFloat(amount),
      details: method === 'CARD' ? card : {}
    };

    processPayment(payload, token)
      .then(res => {
        onSuccess(res.data);
      })
      .catch(err => alert(err.response?.data || err.message));
  };

  return (
    <div>
      {step === 1 && (
        <div>
          <h4>Choose Method</h4>
          <button onClick={() => setMethod('CASH')}>Cash</button>
          <button onClick={() => setMethod('CARD')}>Card</button>
          <button onClick={() => setMethod('INSURANCE')}>Insurance</button>
          <div>Selected: {method}</div>
          <button onClick={() => setStep(2)}>Next</button>
        </div>
      )}

      {step === 2 && (
        <div>
          <h4>Enter Details</h4>
          <label>Amount</label>
          <input value={amount} onChange={e => setAmount(e.target.value)} />
          {method === 'CARD' && (
            <div>
              <input placeholder="Card number" value={card.number} onChange={e => setCard({...card, number: e.target.value})} />
              <input placeholder="MM/YY" value={card.exp} onChange={e => setCard({...card, exp: e.target.value})} />
              <input placeholder="CVV" value={card.cvv} onChange={e => setCard({...card, cvv: e.target.value})} />
            </div>
          )}
          <button onClick={() => setStep(1)}>Back</button>
          <button onClick={submit}>Process Payment</button>
        </div>
      )}
    </div>
  );
};

export default ProcessPaymentStepper;
