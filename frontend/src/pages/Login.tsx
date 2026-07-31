import { Box } from "@mui/material";

import LeftBanner from "../components/auth/LeftBanner";
import LoginCard from "../components/auth/LoginCard";

export default function Login() {
  return (
    <Box
      sx={{
        display: "flex",
        minHeight: "100vh",
         background: "#EEF5FF",
      }}
    >
      <LeftBanner />
      <LoginCard />
    </Box>
  );
}