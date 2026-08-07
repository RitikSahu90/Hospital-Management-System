import { useEffect, useState } from "react";
import {
  Alert,
  Box,
  CircularProgress,
  Grid,
  Paper,
  Typography,
  Chip,
  Avatar,
  List,
  ListItem,
  ListItemAvatar,
  ListItemText,
  Divider,
  Button,
} from "@mui/material";
import {
  People as PeopleIcon,
  LocalHospital as DoctorIcon,
  EventNote as AppointmentIcon,
  Payments as RevenueIcon,
  TrendingUp,
} from "@mui/icons-material";
import {
  BarChart,
  Bar,
  PieChart,
  Pie,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Legend,
} from "recharts";
import { getDashboardSummary } from "../services/dashboardService";
import type { DashboardSummary } from "../services/dashboardService";
import { useAuth } from "../contexts/AuthContext";
import { getPatientNotifications } from "../services/notificationService";
import type { NotificationResponse } from "../services/notificationService";
import { getPatientDocuments } from "../services/documentService";
import type { DocumentMetadataResponse } from "../services/documentService";
import { Download as DownloadIcon } from "@mui/icons-material";

const STATUS_COLORS: Record<string, string> = {
  SCHEDULED: "#1565C0",
  CONFIRMED: "#42A5F5",
  COMPLETED: "#2E7D32",
  CANCELLED: "#C62828",
  NO_SHOW: "#F57C00",
};

const METRIC_ICONS: Record<string, React.ReactNode> = {
  Patients: <PeopleIcon />,
  Doctors: <DoctorIcon />,
  Appointments: <AppointmentIcon />,
  Revenue: <RevenueIcon />,
  "Number of Trusted Patients": <PeopleIcon />,
  "Recovery Rating of Facilities": <DoctorIcon />,
  "My Scheduled Appointments": <AppointmentIcon />,
  "My Patients": <PeopleIcon />,
  "My Appointments": <AppointmentIcon />,
};

const METRIC_COLORS: Record<string, string> = {
  Patients: "#1565C0",
  Doctors: "#00897B",
  Appointments: "#7B1FA2",
  Revenue: "#EF6C00",
  "Number of Trusted Patients": "#1565C0",
  "Recovery Rating of Facilities": "#00897B",
  "My Scheduled Appointments": "#7B1FA2",
  "My Patients": "#1565C0",
  "My Appointments": "#7B1FA2",
};

import StaffRegistrationModal from "../components/admin/StaffRegistrationModal";

export default function Dashboard() {
  const { user } = useAuth();
  const [summary, setSummary] = useState<DashboardSummary | null>(null);
  const [notifications, setNotifications] = useState<NotificationResponse[]>([]);
  const [documents, setDocuments] = useState<DocumentMetadataResponse[]>([]);
  const [error, setError] = useState("");
  const [openStaffModal, setOpenStaffModal] = useState(false);

  useEffect(() => {
    getDashboardSummary()
      .then(setSummary)
      .catch(() => setError("Unable to load dashboard data."));

    if (user?.role === "PATIENT") {
      getPatientNotifications()
        .then(setNotifications)
        .catch(() => console.error("Unable to load notifications."));
      getPatientDocuments()
        .then(setDocuments)
        .catch(() => console.error("Unable to load documents."));
    }
  }, [user]);

  if (error)
    return (
      <Box sx={{ p: 3 }}>
        <Alert severity="error">{error}</Alert>
      </Box>
    );

  if (!summary)
    return (
      <Box sx={{ display: "flex", justifyContent: "center", p: 8 }}>
        <CircularProgress />
      </Box>
    );

  const metrics: [string, number | string][] = user?.role === "PATIENT"
    ? [
        ["Number of Trusted Patients", "1,250"],
        ["Recovery Rating of Facilities", "98.6%"],
        ["My Scheduled Appointments", summary.appointmentCount],
      ]
    : user?.role === "DOCTOR"
    ? [
        ["My Patients", summary.patientCount],
        ["My Appointments", summary.appointmentCount],
      ]
    : [
        ["Patients", summary.patientCount],
        ["Appointments", summary.appointmentCount],
        ["Revenue", summary.revenue],
      ];

  const chartData = Object.entries(summary.appointmentsByStatus).map(([status, count]) => ({
    name: status,
    value: count,
    color: STATUS_COLORS[status] || "#999",
  }));

  return (
    <Box sx={{ p: 3 }}>
      <Box sx={{ display: "flex", alignItems: "center", gap: 1, mb: 3 }}>
        <TrendingUp color="primary" />
        <Typography variant="h4" sx={{ fontWeight: "bold", flexGrow: 1 }}>
          Dashboard
        </Typography>
        {user?.role === "ADMIN" && (
          <Button variant="contained" onClick={() => setOpenStaffModal(true)}>
            + Register Staff
          </Button>
        )}
      </Box>
      <StaffRegistrationModal 
        open={openStaffModal} 
        onClose={() => setOpenStaffModal(false)} 
        onSuccess={() => alert("Staff registered successfully! Refreshing dashboard...")} 
      />

      {/* Patient Notifications Widget */}
      {user?.role === "PATIENT" && notifications.length > 0 && (
        <Box sx={{ mb: 3 }}>
          <Typography variant="h6" sx={{ mb: 1.5, fontWeight: 600, display: "flex", alignItems: "center", gap: 1 }}>
            📢 Notifications
          </Typography>
          <Box sx={{ display: "grid", gap: 1.5 }}>
            {notifications.map((notif) => (
              <Alert 
                key={notif.id} 
                severity="info" 
                sx={{ 
                  borderRadius: 2, 
                  boxShadow: "0 2px 8px rgba(0,0,0,0.04)", 
                  borderLeft: "5px solid #29B6F6" 
                }}
              >
                <Typography variant="subtitle2" sx={{ fontWeight: "bold" }}>
                  {notif.title}
                </Typography>
                <Typography variant="body2">{notif.message}</Typography>
                <Typography variant="caption" color="text.secondary" sx={{ display: "block", mt: 0.5 }}>
                  {new Date(notif.createdAt).toLocaleString("en-IN")}
                </Typography>
              </Alert>
            ))}
          </Box>
        </Box>
      )}

      {/* Patient Documents Widget */}
      {user?.role === "PATIENT" && documents.length > 0 && (
        <Box sx={{ mb: 3 }}>
          <Typography variant="h6" sx={{ mb: 1.5, fontWeight: 600, display: "flex", alignItems: "center", gap: 1 }}>
            📄 My Medical Documents
          </Typography>
          <Box sx={{ display: "grid", gap: 1.5 }}>
            {documents.map((doc) => (
              <Paper 
                key={doc.id} 
                sx={{ 
                  p: 2, 
                  display: "flex", 
                  alignItems: "center", 
                  justifyContent: "space-between",
                  borderRadius: 2,
                  boxShadow: "0 2px 8px rgba(0,0,0,0.04)"
                }}
              >
                <Box>
                  <Typography variant="subtitle2" sx={{ fontWeight: "bold" }}>
                    {doc.documentName}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {doc.fileType} • Uploaded by {doc.uploadedBy} on {new Date(doc.timestamp).toLocaleDateString("en-IN")}
                  </Typography>
                </Box>
                <Button 
                  variant="outlined" 
                  size="small" 
                  startIcon={<DownloadIcon />} 
                  href={doc.downloadUrl}
                  target="_blank"
                >
                  Download
                </Button>
              </Paper>
            ))}
          </Box>
        </Box>
      )}

      {/* Metric Cards */}
      <Grid container spacing={3} sx={{ mb: 3 }}>
        {metrics.map(([label, value]) => (
          <Grid key={String(label)} size={{ xs: 12, sm: 6, lg: 4 }}>
            <Paper
              sx={{
                p: 3,
                display: "flex",
                alignItems: "center",
                gap: 2,
                transition: "transform 0.2s, box-shadow 0.2s",
                "&:hover": {
                  transform: "translateY(-4px)",
                  boxShadow: "0 8px 24px rgba(0,0,0,0.12)",
                },
              }}
            >
              <Avatar
                sx={{
                  bgcolor: `${METRIC_COLORS[label]}15`,
                  color: METRIC_COLORS[label],
                  width: 56,
                  height: 56,
                }}
              >
                {METRIC_ICONS[label]}
              </Avatar>
              <Box>
                <Typography color="text.secondary" variant="body2">
                  {label}
                </Typography>
                <Typography variant="h4" sx={{ fontWeight: 700 }}>
                  {label === "Revenue"
                    ? `₹${Number(value).toLocaleString("en-IN", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`
                    : value}
                </Typography>
              </Box>
            </Paper>
          </Grid>
        ))}
      </Grid>

      {/* Charts */}
      <Grid container spacing={3}>
        {/* Bar Chart */}
        <Grid size={{ xs: 12, lg: 7 }}>
          <Paper sx={{ p: 3, height: 380 }}>
            <Typography variant="h6" sx={{ mb: 3, fontWeight: 600 }}>
              Appointments by Status
            </Typography>
            {chartData.length > 0 ? (
              <ResponsiveContainer width="100%" height={300}>
                <BarChart data={chartData}>
                  <CartesianGrid strokeDasharray="3 3" stroke="#E0E0E0" />
                  <XAxis dataKey="name" tick={{ fontSize: 12 }} />
                  <YAxis allowDecimals={false} tick={{ fontSize: 12 }} />
                  <Tooltip
                    contentStyle={{
                      borderRadius: 12,
                      border: "1px solid #E0E0E0",
                      fontSize: 13,
                    }}
                  />
                  <Bar dataKey="value" radius={[8, 8, 0, 0]}>
                    {chartData.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={entry.color} />
                    ))}
                  </Bar>
                </BarChart>
              </ResponsiveContainer>
            ) : (
              <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", height: 300 }}>
                <Typography color="text.secondary">No appointment data available</Typography>
              </Box>
            )}
          </Paper>
        </Grid>

        {/* Pie Chart */}
        <Grid size={{ xs: 12, lg: 5 }}>
          <Paper sx={{ p: 3, height: 380 }}>
            <Typography variant="h6" sx={{ mb: 3, fontWeight: 600 }}>
              Status Distribution
            </Typography>
            {chartData.length > 0 ? (
              <ResponsiveContainer width="100%" height={300}>
                <PieChart>
                  <Pie
                    data={chartData}
                    dataKey="value"
                    nameKey="name"
                    cx="50%"
                    cy="50%"
                    outerRadius={100}
                    innerRadius={50}
                    paddingAngle={3}
                  >
                    {chartData.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={entry.color} />
                    ))}
                  </Pie>
                  <Tooltip
                    contentStyle={{
                      borderRadius: 12,
                      border: "1px solid #E0E0E0",
                      fontSize: 13,
                    }}
                  />
                  <Legend wrapperStyle={{ fontSize: 12 }} />
                </PieChart>
              </ResponsiveContainer>
            ) : (
              <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", height: 300 }}>
                <Typography color="text.secondary">No data available</Typography>
              </Box>
            )}
          </Paper>
        </Grid>

        {/* Status Summary List */}
        <Grid size={{ xs: 12 }}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>
              Appointment Status Breakdown
            </Typography>
            <List>
              {Object.entries(summary.appointmentsByStatus).map(([status, count], index, arr) => (
                <Box key={status}>
                  <ListItem
                    sx={{ px: 0 }}
                    secondaryAction={
                      <Chip
                        label={count}
                        sx={{
                          bgcolor: `${STATUS_COLORS[status] || "#999"}15`,
                          color: STATUS_COLORS[status] || "#999",
                          fontWeight: 700,
                          fontSize: 16,
                          px: 1,
                        }}
                      />
                    }
                  >
                    <ListItemAvatar>
                      <Avatar sx={{ bgcolor: `${STATUS_COLORS[status] || "#999"}20`, color: STATUS_COLORS[status] || "#999" }}>
                        <AppointmentIcon />
                      </Avatar>
                    </ListItemAvatar>
                    <ListItemText
                      primary={status}
                      slotProps={{ primary: { sx: { fontWeight: 600, fontSize: 15 } } }}
                    />
                  </ListItem>
                  {index < arr.length - 1 && <Divider />}
                </Box>
              ))}
            </List>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
}