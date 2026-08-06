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
