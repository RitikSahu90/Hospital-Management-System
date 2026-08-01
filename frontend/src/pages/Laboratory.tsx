import { useEffect, useState } from "react";
import { Alert, Box, Button, CircularProgress, Paper, TextField, Typography } from "@mui/material";
import { getPatientReports, uploadPatientReport } from "../services/reportService";
import type { PatientReport } from "../services/reportService";

export default function Laboratory() {
  const [patientId, setPatientId] = useState("");
  const [title, setTitle] = useState("");
  const [file, setFile] = useState<File | null>(null);
  const [reports, setReports] = useState<PatientReport[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const loadReports = async () => {
    const id = Number(patientId);
    if (!Number.isInteger(id) || id <= 0) return;
    try {
      setLoading(true);
      setError("");
      setReports(await getPatientReports(id));
    } catch {
      setError("Unable to load patient reports.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { void loadReports(); }, []);

  const upload = async () => {
    const id = Number(patientId);
    if (!Number.isInteger(id) || id <= 0 || !title.trim() || !file) {
      setError("Patient ID, report title, and file are required.");
      return;
    }
    try {
      setLoading(true);
      setError("");
      const report = await uploadPatientReport(id, title, file);
      setReports((current) => [report, ...current]);
      setTitle("");
      setFile(null);
    } catch {
      setError("Unable to upload report. Use a PDF, JPEG, or PNG file up to 10 MB.");
    } finally {
      setLoading(false);
    }
  };

  return <Box sx={{ p: 3 }}>
    <Typography variant="h4" sx={{ fontWeight: "bold", mb: 3 }}>Laboratory Reports</Typography>
    {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
    <Paper sx={{ p: 3, mb: 3, display: "grid", gap: 2, maxWidth: 640 }}>
      <TextField label="Patient ID" type="number" value={patientId} onChange={(event) => setPatientId(event.target.value)} />
      <Button variant="outlined" onClick={() => void loadReports()}>Load reports</Button>
      <TextField label="Report title" value={title} onChange={(event) => setTitle(event.target.value)} />
      <input type="file" accept="application/pdf,image/jpeg,image/png" onChange={(event) => setFile(event.target.files?.[0] ?? null)} />
      <Button variant="contained" onClick={() => void upload()} disabled={loading}>{loading ? "Uploading..." : "Upload report"}</Button>
    </Paper>
    {loading && reports.length === 0 ? <CircularProgress /> : reports.length === 0 ? <Typography color="text.secondary">No reports found.</Typography> : reports.map((report) => <Paper key={report.id} sx={{ p: 2, mb: 1 }}><Typography sx={{ fontWeight: "bold" }}>{report.title}</Typography><Typography variant="body2" color="text.secondary">{report.reportUrl}</Typography></Paper>)}
  </Box>;
}