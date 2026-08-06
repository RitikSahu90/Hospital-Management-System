import { useState, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { Alert, Box, Button, Paper, TextField, Typography, Chip, Grid, Divider } from "@mui/material";
import { createPayment, getPayments } from "../services/paymentService";
import type { Payment, PaymentMethod } from "../services/paymentService";
import { getBilling } from "../services/billingService";
import type { Billing } from "../types/clinical";

export default function Payments() {
  const [searchParams] = useSearchParams();
  const [billId, setBillId] = useState("");
  const [amount, setAmount] = useState("");
  const [method, setMethod] = useState<PaymentMethod>("CASH");
  const [bill, setBill] = useState<Billing | null>(null);
  const [payments, setPayments] = useState<Payment[]>([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const load = async (forcedId?: number) => {
    try {
      setLoading(true);
      setError("");
      const id = forcedId || Number(billId);
      if (!id) {
        setError("Please enter a valid Bill ID.");
        return;
      }
      const fetchedBill = await getBilling(id);
      setBill(fetchedBill);
      setPayments(await getPayments(id));
      setAmount(String(fetchedBill.dueAmount));
    } catch (err: any) {
      setBill(null);
      setPayments([]);
      const msg = err.response?.data?.error || "Unable to load bill or payments.";
      setError(msg);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const qId = searchParams.get("billId");
    if (qId) {
      setBillId(qId);
      void load(Number(qId));
    }
  }, [searchParams]);

  const save = async () => {
    const id = Number(billId);
    if (!id || Number(amount) <= 0) {
      setError("Bill ID and a positive amount are required.");
      return;
    }
    try {
      setLoading(true);
      setError("");
      const payment = await createPayment(id, { amount: Number(amount), paymentMethod: method });
      setPayments((current) => [...current, payment]);
      
      const updatedBill = await getBilling(id);
      setBill(updatedBill);
      setAmount(String(updatedBill.dueAmount));
    } catch (err: any) {
      const msg = err.response?.data?.error || "Payment failed or exceeds the bill total.";
      setError(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h4" sx={{ fontWeight: "bold", mb: 3 }}>
        Payments
      </Typography>
      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}
      <Paper sx={{ p: 3, display: "grid", gap: 2.5, maxWidth: 640, borderRadius: 3 }}>
        <Box sx={{ display: "flex", gap: 1.5 }}>
          <TextField
            label="Bill ID"
            type="number"
            value={billId}
            onChange={(event) => setBillId(event.target.value)}
            fullWidth
          />
          <Button variant="outlined" onClick={() => void load()} sx={{ minWidth: 150 }}>
            Load payments
          </Button>
        </Box>

        {bill && (
          <Box sx={{ p: 2, bgcolor: "grey.50", borderRadius: 2, border: "1px solid", borderColor: "grey.200" }}>
            <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 1.5 }}>
              <Typography variant="subtitle1" sx={{ fontWeight: "bold" }}>
                Bill Details (Invoice #{bill.id})
              </Typography>
              <Chip
                label={bill.status.replaceAll("_", " ")}
                size="small"
                color={
                  bill.status === "PAID"
                    ? "success"
                    : bill.status === "PARTIALLY_PAID"
                    ? "info"
                    : "warning"
                }
              />
            </Box>
            <Grid container spacing={2}>
              <Grid size={{ xs: 6 }}>
                <Typography variant="caption" color="text.secondary" sx={{ display: "block" }}>Patient ID</Typography>
                <Typography variant="body1" sx={{ fontWeight: 600 }}>#{bill.patientId}</Typography>
              </Grid>
              <Grid size={{ xs: 6 }}>
                <Typography variant="caption" color="text.secondary" sx={{ display: "block" }}>Total Amount</Typography>
                <Typography variant="body1" sx={{ fontWeight: 600 }}>₹{bill.totalAmount.toLocaleString("en-IN", { minimumFractionDigits: 2 })}</Typography>
              </Grid>
              <Grid size={{ xs: 6 }}>
                <Typography variant="caption" color="text.secondary" sx={{ display: "block" }}>Paid Amount</Typography>
                <Typography variant="body1" sx={{ color: "success.main", fontWeight: 600 }}>₹{bill.paidAmount.toLocaleString("en-IN", { minimumFractionDigits: 2 })}</Typography>
              </Grid>
              <Grid size={{ xs: 6 }}>
                <Typography variant="caption" color="text.secondary" sx={{ display: "block" }}>Due Amount</Typography>
                <Typography variant="body1" sx={{ color: "error.main", fontWeight: 600 }}>₹{bill.dueAmount.toLocaleString("en-IN", { minimumFractionDigits: 2 })}</Typography>
              </Grid>
            </Grid>
          </Box>
        )}

        <Divider />

        <TextField
          label="Amount to Pay"
          type="number"
          value={amount}
          onChange={(event) => setAmount(event.target.value)}
          fullWidth
        />
        
        <TextField
          select
          slotProps={{ select: { native: true } }}
          label="Payment method"
          value={method}
          onChange={(event) => setMethod(event.target.value as PaymentMethod)}
          fullWidth
        >
          <option value="CASH">Cash</option>
          <option value="CARD">Card</option>
          <option value="UPI">UPI</option>
          <option value="INSURANCE">Other / Insurance</option>
        </TextField>

        <Button variant="contained" size="large" disabled={loading || !bill} onClick={() => void save()}>
          Record payment
        </Button>

        {payments.length > 0 && (
          <Box sx={{ mt: 1 }}>
            <Typography variant="subtitle1" sx={{ fontWeight: "bold", mb: 1 }}>
              Payment History
            </Typography>
            {payments.map((payment) => (
              <Typography key={payment.id} sx={{ mb: 0.5 }}>
                ₹{payment.amount.toLocaleString("en-IN", { minimumFractionDigits: 2 })} — {payment.paymentMethod}
              </Typography>
            ))}
          </Box>
        )}
      </Paper>
    </Box>
  );
}