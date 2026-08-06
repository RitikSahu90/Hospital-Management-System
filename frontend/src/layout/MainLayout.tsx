import { Box } from "@mui/material";
import { Outlet } from "react-router-dom";

import Sidebar from "./Sidebar";
import Topbar from "./Topbar";

export default function MainLayout() {
  return (
    <Box sx={{ display: "flex", minHeight: "100vh", bgcolor: "#F5F7FB" }}>
      <Sidebar />

      <Box
        sx={{
          flexGrow: 1,
          display: "flex",
          flexDirection: "column",
          minWidth: 0,
        }}
      >
        <Topbar />

        <Box
          sx={{
            p: 3,
            flexGrow: 1,
            minWidth: 0,
            overflow: "auto",
          }}
        >
          <Outlet />
        </Box>
      </Box>
    </Box>
  );
}