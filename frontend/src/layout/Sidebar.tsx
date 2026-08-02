import { useState } from "react";
import { NavLink, useLocation } from "react-router-dom";

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

const drawerWidth = 260;
const collapsedWidth = 80;

const menuItems = [
  {
    text: "Dashboard",
    path: "/dashboard",
    icon: <DashboardIcon />,
  },
  {
    text: "Patients",
    path: "/patients",
    icon: <PeopleIcon />,
  },
  {
    text: "Doctors",
    path: "/doctors",
    icon: <LocalHospitalIcon />,
  },
  { text: "Departments", path: "/departments", icon: <LocalHospitalIcon /> },
  { text: "Availability", path: "/availability", icon: <EventNoteIcon /> },
  {
    text: "Appointments",
    path: "/appointments",
    icon: <EventNoteIcon />,
  },
  {
    text: "Billing",
    path: "/billing",
    icon: <ReceiptLongIcon />,
  },
  {
    text: "Pharmacy",
    path: "/pharmacy",
    icon: <MedicationIcon />,
  },
  {
    text: "Laboratory",
    path: "/laboratory",
    icon: <ScienceIcon />,
  },
  { text: "Medical Records", path: "/medical-records", icon: <ScienceIcon /> },
  { text: "Suppliers", path: "/suppliers", icon: <MedicationIcon /> },
  { text: "Payments", path: "/payments", icon: <ReceiptLongIcon /> },
];

export default function Sidebar() {
  const [collapsed, setCollapsed] = useState(false);
  const location = useLocation();

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

      <List sx={{ mt: 2 }}>
        {menuItems.map((item) => (
          <ListItemButton
            key={item.text}
            component={NavLink}
            to={item.path}
            sx={{
              mx: 1,
              mb: 1,
              borderRadius: 3,
              color: "#fff",
              textDecoration: "none",

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
                sx={{
                  "& .MuiListItemText-primary": {
                    fontSize: 15,
                    fontWeight: 500,
                  },
                }}
              />
            )}
          </ListItemButton>
        ))}
      </List>

      <Box sx={{ flexGrow: 1 }} />

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