import { useState } from "react";
import { NavLink, useLocation, useNavigate } from "react-router-dom";

import {
  Box,
  Drawer,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Toolbar,
  Typography,
  Divider,
  IconButton,
  Avatar,
  ListSubheader,
} from "@mui/material";

import DashboardIcon from "@mui/icons-material/Dashboard";
import PeopleIcon from "@mui/icons-material/People";
import LocalHospitalIcon from "@mui/icons-material/LocalHospital";
import EventNoteIcon from "@mui/icons-material/EventNote";
import ReceiptLongIcon from "@mui/icons-material/ReceiptLong";
import MedicationIcon from "@mui/icons-material/Medication";
import ScienceIcon from "@mui/icons-material/Science";
import MenuOpenIcon from "@mui/icons-material/MenuOpen";
import MenuIcon from "@mui/icons-material/Menu";
import LogoutIcon from "@mui/icons-material/Logout";
import DescriptionIcon from "@mui/icons-material/Description";
import InventoryIcon from "@mui/icons-material/Inventory";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";

import { useAuth } from "../contexts/AuthContext";
import type { UserRole } from "../types/auth";

const drawerWidth = 260;
const collapsedWidth = 80;

type MenuItem = {
  text: string;
  path: string;
  icon: React.ReactNode;
  roles?: UserRole[]; // undefined = visible to all
};

type MenuSection = {
  label: string;
  items: MenuItem[];
};

const menuSections: MenuSection[] = [
  {
    label: "Overview",
    items: [
      { text: "Dashboard", path: "/dashboard", icon: <DashboardIcon />, roles: ["ADMIN", "DOCTOR", "RECEPTIONIST", "PATIENT"] },
      { text: "Profile", path: "/profile", icon: <AccountCircleIcon /> },
    ],
  },
  {
    label: "Clinical",
    items: [
      { text: "Patients", path: "/patients", icon: <PeopleIcon />, roles: ["ADMIN", "DOCTOR", "RECEPTIONIST"] },
      { text: "Doctors", path: "/doctors", icon: <LocalHospitalIcon />, roles: ["ADMIN", "RECEPTIONIST", "PHARMACIST", "PATIENT"] },
      { text: "Departments", path: "/departments", icon: <LocalHospitalIcon /> },
      { text: "Appointments", path: "/appointments", icon: <EventNoteIcon />, roles: ["ADMIN", "DOCTOR", "RECEPTIONIST", "PATIENT"] },
      { text: "Medical Records", path: "/medical-records", icon: <DescriptionIcon />, roles: ["ADMIN", "DOCTOR", "RECEPTIONIST"] },
      { text: "Laboratory", path: "/laboratory", icon: <ScienceIcon />, roles: ["ADMIN", "DOCTOR", "RECEPTIONIST", "PHARMACIST", "PATIENT"] },
      { text: "Prescriptions", path: "/prescriptions", icon: <MedicationIcon />, roles: ["ADMIN", "DOCTOR", "PHARMACIST", "PATIENT"] },
    ],
  },
  {
    label: "Financial",
    items: [
      { text: "Billing", path: "/billing", icon: <ReceiptLongIcon />, roles: ["ADMIN", "PHARMACIST", "PATIENT"] },
      { text: "Payments", path: "/payments", icon: <ReceiptLongIcon />, roles: ["ADMIN", "PHARMACIST"] },
    ],
  },
  {
    label: "Inventory",
    items: [
      { text: "Pharmacy", path: "/pharmacy", icon: <MedicationIcon />, roles: ["ADMIN", "PHARMACIST"] },
      { text: "Suppliers", path: "/suppliers", icon: <InventoryIcon />, roles: ["ADMIN", "PHARMACIST"] },
    ],
  },
];

export default function Sidebar() {
  const [collapsed, setCollapsed] = useState(false);
  const location = useLocation();
  const navigate = useNavigate();
  const { user, logout } = useAuth();

  const userRole = user?.role || "PATIENT";

  const filteredSections = menuSections
    .map((section) => ({
      ...section,
      items: section.items
        .filter((item) => !item.roles || item.roles.includes(userRole))
        .map((item) => {
          if (item.path === "/laboratory" && (userRole === "PHARMACIST" || userRole === "PATIENT")) {
            return { ...item, text: "Reports" };
          }
          if (item.path === "/billing" && userRole === "PATIENT") {
            return { ...item, text: "My Bills" };
          }
          return item;
        }),
    }))
    .filter((section) => section.items.length > 0);

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  const initials = user?.username ? user.username.charAt(0).toUpperCase() : "U";

  return (
    <Drawer
      variant="permanent"
      sx={{
        width: collapsed ? collapsedWidth : drawerWidth,
        flexShrink: 0,

        "& .MuiDrawer-paper": {
          width: collapsed ? collapsedWidth : drawerWidth,
          boxSizing: "border-box",
          background: "#0D47A1",
          color: "#fff",
          transition: "0.3s",
          overflowX: "hidden",
          borderRight: "none",
          display: "flex",
          flexDirection: "column",
        },
      }}
    >
      <Toolbar
        sx={{
          display: "flex",
          justifyContent: collapsed ? "center" : "space-between",
          px: 2,
        }}
      >
        {!collapsed && (
          <Typography sx={{ fontWeight: 700, fontSize: 22 }}>
            Multicare HMS
          </Typography>
        )}

        <IconButton
          onClick={() => setCollapsed(!collapsed)}
          sx={{ color: "#fff" }}
        >
          {collapsed ? <MenuIcon /> : <MenuOpenIcon />}
        </IconButton>
      </Toolbar>

      <Divider sx={{ bgcolor: "rgba(255,255,255,.15)" }} />

      <List sx={{ mt: 1, flexGrow: 1, overflowY: "auto" }}>
        {filteredSections.map((section) => (
          <Box key={section.label}>
            {!collapsed && (
              <ListSubheader
                sx={{
                  bgcolor: "transparent",
                  color: "rgba(255,255,255,.5)",
                  fontSize: 11,
                  fontWeight: 700,
                  textTransform: "uppercase",
                  letterSpacing: 1,
                  lineHeight: "36px",
                  px: 3,
                }}
              >
                {section.label}
              </ListSubheader>
            )}
            {section.items.map((item) => (
              <ListItemButton
                key={item.text}
                component={NavLink}
                to={item.path}
                sx={{
                  mx: 1,
                  mb: 0.5,
                  borderRadius: 3,
                  color: "#fff",
                  textDecoration: "none",
                  py: 1.2,

                  bgcolor:
                    location.pathname === item.path
                      ? "#1976D2"
                      : "transparent",

                  "&:hover": {
                    bgcolor: "#1976D2",
                  },

                  "&.active": {
                    bgcolor: "#1976D2",
                  },
                }}
              >
                <ListItemIcon
                  sx={{
                    color: "#fff",
                    minWidth: 45,
                  }}
                >
                  {item.icon}
                </ListItemIcon>

                {!collapsed && (
                  <ListItemText
                    primary={item.text}
                    slotProps={{
                      primary: { sx: { fontSize: 14, fontWeight: 500 } },
                    }}
                  />
                )}
              </ListItemButton>
            ))}
          </Box>
        ))}
      </List>

      <Divider sx={{ bgcolor: "rgba(255,255,255,.15)" }} />

      {/* User profile + logout */}
      <Box sx={{ p: 2 }}>
        {!collapsed ? (
          <Box sx={{ display: "flex", alignItems: "center", gap: 1.5, mb: 1 }}>
            <Avatar sx={{ bgcolor: "#42A5F5", width: 36, height: 36, fontSize: 16 }}>
              {initials}
            </Avatar>
            <Box sx={{ flexGrow: 1, overflow: "hidden" }}>
              <Typography sx={{ fontSize: 14, fontWeight: 600, whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis" }}>
                {user?.username || "User"}
              </Typography>
              <Typography sx={{ fontSize: 11, color: "rgba(255,255,255,.6)" }}>
                {userRole}
              </Typography>
            </Box>
            <IconButton onClick={handleLogout} sx={{ color: "rgba(255,255,255,.7)" }} size="small">
              <LogoutIcon fontSize="small" />
            </IconButton>
          </Box>
        ) : (
          <Box sx={{ display: "flex", flexDirection: "column", alignItems: "center", gap: 1 }}>
            <Avatar sx={{ bgcolor: "#42A5F5", width: 36, height: 36, fontSize: 16 }}>
              {initials}
            </Avatar>
            <IconButton onClick={handleLogout} sx={{ color: "rgba(255,255,255,.7)" }} size="small">
              <LogoutIcon fontSize="small" />
            </IconButton>
          </Box>
        )}
      </Box>

      {!collapsed && (
        <Typography
          sx={{ textAlign: "center", mb: 2, fontSize: 12, color: "rgba(255,255,255,.7)" }}
        >
          Version 1.0
        </Typography>
      )}
    </Drawer>
  );
}
