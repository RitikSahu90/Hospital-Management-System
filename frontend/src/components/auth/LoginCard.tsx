import {
  Avatar,
  Box,
  Card,
  CardContent,
  Divider,
  Typography,
} from "@mui/material";
import { motion } from "framer-motion";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";

import LoginForm from "./LoginForm";

export default function LoginCard() {
  return (
    <Box
      sx={{
        width: { xs: "100%", md: "50%" },
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        p: { xs: 2, md: 5 },
        background:
          "linear-gradient(to bottom right,#F5F9FF,#EEF5FF,#FFFFFF)",
      }}
    >
      <motion.div
        initial={{ opacity: 0, x: 80 }}
        animate={{ opacity: 1, x: 0 }}
        transition={{ duration: 0.8 }}
      >
        <Card
          sx={{
            width: "100%",
            maxWidth: 480,
            borderRadius: 6,

            backdropFilter: "blur(18px)",

            background: "rgba(255,255,255,.75)",

            border: "1px solid rgba(255,255,255,.6)",

            boxShadow:
              "0 25px 50px rgba(0,0,0,.12)",

            overflow: "hidden",
          }}
        >
          <CardContent sx={{ p: 5 }}>
            <Box
              sx={{ display: "flex", flexDirection: "column", alignItems: "center", mb: 4 }}
            >
              <Avatar
                sx={{
                  width: 72,
                  height: 72,
                  mb: 2,
                  background:
                    "linear-gradient(135deg,#1565C0,#42A5F5)",
                }}
              >
                <LockOutlinedIcon fontSize="large" />
              </Avatar>

              <Typography
                variant="h4"
                sx={{ fontWeight: 700 }}
              >
                Welcome Back
              </Typography>

              <Typography
                align="center"
                color="text.secondary"
                sx={{ mt: 1 }}
              >
                Sign in or create a patient account to access care information securely.
              </Typography>
            </Box>

            <LoginForm />

            <Box sx={{ mt: 3, p: 2, bgcolor: "rgba(21, 101, 192, 0.04)", borderRadius: 2, border: "1px dashed rgba(21, 101, 192, 0.2)" }}>
              <Typography variant="subtitle2" color="primary" sx={{ mb: 1, fontWeight: "bold" }}>
                Demo Credentials (Password: pass123)
              </Typography>
              <Box sx={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 1 }}>
                <Typography variant="body2" color="text.secondary"><b>Admin:</b> admin1</Typography>
                <Typography variant="body2" color="text.secondary"><b>Doctor:</b> doctor1</Typography>
                <Typography variant="body2" color="text.secondary"><b>Receptionist:</b> receptionist1</Typography>
                <Typography variant="body2" color="text.secondary"><b>Pharmacist:</b> pharmacist1</Typography>
                <Typography variant="body2" color="text.secondary"><b>Patient:</b> patient1</Typography>
              </Box>
            </Box>

            <Divider sx={{ my: 4 }} />

            <Typography
              align="center"
              color="text.secondary"
              variant="body2"
            >
              © 2026 Multicare HMS
              <br />
              Secure Healthcare Management Platform
            </Typography>
          </CardContent>
        </Card>
      </motion.div>
    </Box>
  );
}
