import { useState } from "react";
import { Alert, Box, Button, Paper, TextField, Typography } from "@mui/material";
import { createPayment, getPayments } from "../services/paymentService";
import type { Payment, PaymentMethod } from "../services/paymentService";

export default function Payments() {
  const [billId, setBillId] = useState("");
  const [amount, setAmount] = useState("");
  const [method, setMethod] = useState<PaymentMethod>("CASH");
  const [payments, setPayments] = useState<Payment[]>([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const load = async () => { try { setLoading(true); setError(""); setPayments(await getPayments(Number(billId))); } catch { setError("Unable to load payments."); } finally { setLoading(false); } };
  const save = async () => { if (!Number(billId) || Number(amount) <= 0) { setError("Bill ID and a positive amount are required."); return; } try { setLoading(true); setError(""); const payment = await createPayment(Number(billId), { amount: Number(amount), paymentMethod: method }); setPayments((current) => [...current, payment]); setAmount(""); } catch { setError("Payment failed or exceeds the bill total."); } finally { setLoading(false); } };
  return <Box sx={{ p: 3 }}><Typography variant="h4" sx={{ fontWeight: "bold", mb: 3 }}>Payments</Typography>{error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}<Paper sx={{ p: 3, display: "grid", gap: 2, maxWidth: 640 }}><TextField label="Bill ID" type="number" value={billId} onChange={(event) => setBillId(event.target.value)} /><Button variant="outlined" onClick={() => void load()}>Load payments</Button><TextField label="Amount" type="number" value={amount} onChange={(event) => setAmount(event.target.value)} /><TextField select slotProps={{ select: { native: true } }} label="Payment method" value={method} onChange={(event) => setMethod(event.target.value as PaymentMethod)}><option value="CASH">Cash</option><option value="CARD">Card</option><option value="UPI">UPI</option><option value="INSURANCE">Insurance</option></TextField><Button variant="contained" disabled={loading} onClick={() => void save()}>Record payment</Button>{payments.map((payment) => <Typography key={payment.id}>{payment.amount.toFixed(2)} - {payment.paymentMethod}</Typography>)}</Paper></Box>;
}