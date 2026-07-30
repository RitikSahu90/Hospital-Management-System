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
        <Typography variant="h5" fontWeight={700}>
          Dashboard
        </Typography>

        <Box display="flex" alignItems="center" gap={2}>
          <Typography fontWeight={500}>
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