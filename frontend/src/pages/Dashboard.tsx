import { useEffect, useState } from "react";
import { Alert, Box, CircularProgress, Grid, Paper, Typography } from "@mui/material";
import { getDashboardSummary } from "../services/dashboardService";
import type { DashboardSummary } from "../services/dashboardService";

export default function Dashboard() {
  const [summary, setSummary] = useState<DashboardSummary | null>(null);
  const [error, setError] = useState("");
  useEffect(() => { getDashboardSummary().then(setSummary).catch(() => setError("Unable to load dashboard data.")); }, []);
  if (error) return <Box sx={{ p: 3 }}><Alert severity="error">{error}</Alert></Box>;
  if (!summary) return <Box sx={{ display: "flex", justifyContent: "center", p: 8 }}><CircularProgress /></Box>;
  const metrics = [["Patients", summary.patientCount], ["Doctors", summary.doctorCount], ["Appointments", summary.appointmentCount], ["Revenue", summary.revenue]];
  return <Box sx={{ p: 3 }}><Typography variant="h4" sx={{ fontWeight: "bold", mb: 3 }}>Dashboard</Typography><Grid container spacing={3}>{metrics.map(([label, value]) => <Grid key={String(label)} size={{ xs: 12, sm: 6, lg: 3 }}><Paper sx={{ p: 3 }}><Typography color="text.secondary">{label}</Typography><Typography variant="h4">{label === "Revenue" ? Number(value).toFixed(2) : value}</Typography></Paper></Grid>)}<Grid size={{ xs: 12 }}><Paper sx={{ p: 3 }}><Typography variant="h6" sx={{ mb: 2 }}>Appointments by status</Typography>{Object.entries(summary.appointmentsByStatus).map(([status, count]) => <Box key={status} sx={{ display: "flex", justifyContent: "space-between", py: 1 }}><Typography>{status}</Typography><Typography>{count}</Typography></Box>)}</Paper></Grid></Grid></Box>;
}