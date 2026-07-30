import { Box, Typography } from "@mui/material";

import logo from "../../assets/images/logo.png";
import hero from "../../assets/images/hero.png";

export default function LeftBanner() {
  return (
    <Box
      sx={{
        width: { xs: 0, md: "50%" },
        display: { xs: "none", md: "flex" },
        flexDirection: "column",
        justifyContent: "space-between",
        background: "linear-gradient(135deg,#0D47A1 0%,#1976D2 45%,#42A5F5 100%)",
        color: "#fff",
        position: "relative",
        overflow: "hidden",
        p: 6,
      }}
    >
      <Box display="flex" alignItems="center" gap={2}>
        <img src={logo} alt="Logo" width={60} />
        <Box>
          <Typography variant="h5" fontWeight={700}>
            Multicare HMS
          </Typography>
          <Typography variant="body2">
            Hospital Management System
          </Typography>
        </Box>
      </Box>

      <Box>
        <Typography variant="h2" fontWeight={700}>
          Better Care,
          <br />
          Better Life
        </Typography>

        <Typography mt={3}>
          Manage patients, appointments, doctors, pharmacy,
          laboratory and billing from one secure platform.
        </Typography>
      </Box>

      <Box
        component="img"
        src={hero}
        alt="Hospital"
        sx={{
          width: "100%",
          maxWidth: 620,
          mx: "auto",
        }}
      />
    </Box>
  );
}