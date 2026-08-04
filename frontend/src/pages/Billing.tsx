import { useEffect, useMemo, useState } from "react";
import {
  Alert, Box, Button, Chip, CircularProgress, Dialog, DialogActions, DialogContent,
  DialogTitle, Paper, Table, TableBody, TableCell, TableHead, TableRow, TextField, Typography,
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import PictureAsPdfOutlinedIcon from "@mui/icons-material/PictureAsPdfOutlined";
import { createBilling, deleteBilling, getBillings, updateBilling } from "../services/billingService";
import { useAuth } from "../contexts/AuthContext";
import type { Billing, BillingRequest } from "../types/clinical";

const currency = (value: number) => `₹${Number(value || 0).toLocaleString("en-IN", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
const initialForm = { patientId: "", appointmentId: "", consultationFee: "0", medicineCharges: "0", otherCharges: "0" };

function downloadInvoice(bill: Billing) {
  const invoiceWindow = window.open("", "_blank");
  if (!invoiceWindow) return;
  const line = (label: string, value: number) => `<tr><td>${label}</td><td>${currency(value)}</td></tr>`;
  invoiceWindow.document.write(`<!doctype html><html><head><title>Invoice #${bill.id}</title><style>body{font-family:Arial,sans-serif;color:#172554;margin:48px}header{display:flex;justify-content:space-between;border-bottom:3px solid #1565c0;padding-bottom:24px}h1{margin:0;color:#1565c0}table{width:100%;border-collapse:collapse;margin-top:32px}td{padding:14px;border-bottom:1px solid #dbeafe}td:last-child{text-align:right}.total td{font-size:18px;font-weight:bold;background:#eff6ff}.meta{color:#475569;margin-top:6px}@media print{body{margin:24px}}</style></head><body><header><div><h1>Multicare HMS</h1><div class="meta">Hospital Management System</div></div><div><strong>INVOICE #${bill.id}</strong><div class="meta">Generated ${new Date().toLocaleDateString("en-IN")}</div></div></header><h2>Billing summary</h2><p class="meta">Patient ID: ${bill.patientId}${bill.appointmentId ? ` &nbsp;·&nbsp; Appointment ID: ${bill.appointmentId}` : ""}</p><table>${line("Consultation fee", bill.consultationFee)}${line("Medicine charges", bill.medicineCharges)}${line("Other charges", bill.otherCharges)}<tr class="total"><td>Total amount</td><td>${currency(bill.totalAmount)}</td></tr>${line("Amount paid", bill.paidAmount)}<tr class="total"><td>Balance due</td><td>${currency(bill.dueAmount)}</td></tr></table><p class="meta">Status: ${bill.status.replaceAll("_", " ")}</p></body></html>`);
  invoiceWindow.document.close();
  invoiceWindow.focus();
  invoiceWindow.print();
}

export default function Billing() {
  const { user } = useAuth();
  const canWrite = user?.role === "ADMIN" || user?.role === "PHARMACIST";
  const [bills, setBills] = useState<Billing[]>([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [open, setOpen] = useState(false);
  const [editingBill, setEditingBill] = useState<Billing | null>(null);
  const [form, setForm] = useState(initialForm);
  const previewTotal = useMemo(() => Number(form.consultationFee || 0) + Number(form.medicineCharges || 0) + Number(form.otherCharges || 0), [form]);

  const loadBills = async () => {
    try { setLoading(true); setError(""); setBills(await getBillings()); }
    catch { setError("Unable to load billings from the backend."); }
    finally { setLoading(false); }
  };
  useEffect(() => { void loadBills(); }, []);

  const save = async () => {
    const patientId = Number(form.patientId);
    if (!Number.isInteger(patientId) || patientId <= 0) { setError("Enter a valid patient ID before creating a bill."); return; }
    const request: BillingRequest = {
      patientId,
      appointmentId: form.appointmentId ? Number(form.appointmentId) : undefined,
      consultationFee: Number(form.consultationFee), medicineCharges: Number(form.medicineCharges), otherCharges: Number(form.otherCharges),
    };
    if (Object.values(request).some((value) => typeof value === "number" && value < 0)) { setError("Charges cannot be negative."); return; }
    try {
      setSaving(true); setError("");
      const bill = editingBill ? await updateBilling(editingBill.id, request) : await createBilling(request);
      setBills((current) => editingBill ? current.map((currentBill) => currentBill.id === bill.id ? bill : currentBill) : [bill, ...current]);
      setOpen(false); setEditingBill(null); setForm(initialForm);
    }
    catch { setError("Unable to create the bill. Check the patient and appointment IDs, then try again."); }
    finally { setSaving(false); }
  };

  const openCreate = () => { setEditingBill(null); setForm(initialForm); setOpen(true); };
  const openEdit = (bill: Billing) => { setEditingBill(bill); setForm({ patientId: String(bill.patientId), appointmentId: bill.appointmentId ? String(bill.appointmentId) : "", consultationFee: String(bill.consultationFee), medicineCharges: String(bill.medicineCharges), otherCharges: String(bill.otherCharges) }); setOpen(true); };
  const remove = async (id: number) => {
    if (!window.confirm("Delete this bill? This cannot be undone.")) return;
    try { setError(""); await deleteBilling(id); setBills((current) => current.filter((bill) => bill.id !== id)); }
    catch { setError("Unable to delete this bill."); }
  };

  return <Box sx={{ p: 3 }}>
    <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", gap: 2, flexWrap: "wrap", mb: 1 }}>
      <Typography variant="h4" sx={{ fontWeight: "bold" }}>Billing & invoices</Typography>
      {canWrite && <Button variant="contained" startIcon={<AddIcon />} onClick={openCreate}>Create bill</Button>}
    </Box>
    <Typography color="text.secondary" sx={{ mb: 3 }}>Create accurate charge summaries and export any invoice as a PDF from the print dialog.</Typography>
    {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
    {loading ? <Box sx={{ display: "flex", justifyContent: "center", py: 8 }}><CircularProgress /></Box> : bills.length === 0 ? <Paper sx={{ p: 5, textAlign: "center" }}><Typography color="text.secondary">No bills have been created yet.</Typography></Paper> : <Paper sx={{ overflowX: "auto", borderRadius: 3 }}><Table><TableHead><TableRow sx={{ bgcolor: "grey.50" }}><TableCell>Invoice</TableCell><TableCell>Patient</TableCell><TableCell>Total</TableCell><TableCell>Paid</TableCell><TableCell>Due</TableCell><TableCell>Status</TableCell><TableCell>Actions</TableCell></TableRow></TableHead><TableBody>{bills.map((bill) => <TableRow key={bill.id} hover><TableCell>#{bill.id}</TableCell><TableCell>Patient #{bill.patientId}</TableCell><TableCell>{currency(bill.totalAmount)}</TableCell><TableCell>{currency(bill.paidAmount)}</TableCell><TableCell>{currency(bill.dueAmount)}</TableCell><TableCell><Chip size="small" label={bill.status.replaceAll("_", " ")} color={bill.status === "PAID" ? "success" : bill.status === "PENDING" ? "warning" : "info"} /></TableCell><TableCell><Button size="small" startIcon={<PictureAsPdfOutlinedIcon />} onClick={() => downloadInvoice(bill)}>PDF</Button>{canWrite && <Button size="small" onClick={() => openEdit(bill)}>Edit</Button>}{user?.role === "ADMIN" && <Button size="small" color="error" onClick={() => void remove(bill.id)}>Delete</Button>}</TableCell></TableRow>)}</TableBody></Table></Paper>}
    <Dialog open={open} onClose={() => !saving && setOpen(false)} fullWidth maxWidth="sm"><DialogTitle>{editingBill ? "Edit bill" : "Create a bill"}</DialogTitle><DialogContent><Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>Add the charges below. The total is calculated automatically.</Typography><Box sx={{ display: "grid", gap: 2, pt: 1 }}><TextField required label="Patient ID" type="number" value={form.patientId} onChange={(event) => setForm({ ...form, patientId: event.target.value })} /><TextField label="Appointment ID (optional)" type="number" value={form.appointmentId} onChange={(event) => setForm({ ...form, appointmentId: event.target.value })} />{(["consultationFee", "medicineCharges", "otherCharges"] as const).map((key) => <TextField key={key} label={key === "consultationFee" ? "Consultation fee" : key === "medicineCharges" ? "Medicine charges" : "Other charges"} type="number" slotProps={{ htmlInput: { min: 0, step: "0.01" } }} value={form[key]} onChange={(event) => setForm({ ...form, [key]: event.target.value })} />)}<Paper variant="outlined" sx={{ p: 2, bgcolor: "primary.50" }}><Typography variant="body2" color="text.secondary">Invoice total</Typography><Typography variant="h6">{currency(previewTotal)}</Typography></Paper></Box></DialogContent><DialogActions><Button onClick={() => setOpen(false)} disabled={saving}>Cancel</Button><Button variant="contained" onClick={() => void save()} disabled={saving}>{saving ? "Saving..." : editingBill ? "Save changes" : "Create bill"}</Button></DialogActions></Dialog>
  </Box>;
}
