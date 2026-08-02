import { AppBar, Toolbar, Typography, Box, Avatar } from "@mui/material";

export default function Topbar() {
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
        <Typography variant="h5" sx={{ fontWeight: 700 }}>
          Dashboard
        </Typography>

        <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
          <Typography sx={{ fontWeight: 500 }}>
            Admin
          </Typography>

          <Avatar sx={{ bgcolor: "#1565C0" }}>
            A
          </Avatar>
        </Box>
      </Toolbar>
    </AppBar>
  );
}