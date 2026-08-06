import { AppBar, Toolbar, Typography, Box, Avatar, Chip, IconButton, Tooltip } from "@mui/material";
import { useLocation } from "react-router-dom";
import NotificationsOutlinedIcon from "@mui/icons-material/NotificationsOutlined";
import { useAuth } from "../contexts/AuthContext";

const ROUTE_TITLES: Record<string, string> = {
  "/dashboard": "Dashboard",
  "/patients": "Patients",
  "/doctors": "Doctors",
  "/departments": "Departments",
  "/appointments": "Appointments",
  "/medical-records": "Medical Records",
  "/laboratory": "Laboratory",
  "/prescriptions": "Prescriptions",
  "/billing": "Billing",
  "/payments": "Payments",
  "/pharmacy": "Pharmacy",
  "/suppliers": "Suppliers",
};

export default function Topbar() {
  const location = useLocation();
  const { user } = useAuth();

  const pageTitle = ROUTE_TITLES[location.pathname] || "Dashboard";
  const initials = user?.username ? user.username.charAt(0).toUpperCase() : "A";

  return (
    <AppBar
      position="static"
      elevation={0}
      sx={{
        bgcolor: "#fff",
        color: "#1E293B",
        borderBottom: "1px solid #E5E7EB",
      }}
    >
      <Toolbar
        sx={{
          display: "flex",
          justifyContent: "space-between",
        }}
      >
        <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
          <Typography variant="h5" sx={{ fontWeight: 700 }}>
            {pageTitle}
          </Typography>
        </Box>

        <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
          <Tooltip title="Notifications">
            <IconButton size="small" sx={{ color: "#5F6368" }}>
              <NotificationsOutlinedIcon fontSize="small" />
            </IconButton>
          </Tooltip>

          <Chip
            label={user?.role || "GUEST"}
            size="small"
            color="primary"
            variant="outlined"
            sx={{ fontWeight: 600, fontSize: 12 }}
          />

          <Typography sx={{ fontWeight: 500, display: { xs: "none", sm: "block" } }}>
            {user?.username || "Admin"}
          </Typography>

          <Avatar sx={{ bgcolor: "#1565C0", width: 36, height: 36, fontSize: 16 }}>
            {initials}
          </Avatar>
        </Box>
      </Toolbar>
    </AppBar>
  );
}
