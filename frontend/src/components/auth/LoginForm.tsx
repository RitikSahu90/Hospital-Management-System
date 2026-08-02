import { useState } from "react";
import { useNavigate } from "react-router-dom";

import {
  Alert,
  Box,
  Checkbox,
  Chip,
  FormControlLabel,
  IconButton,
  Link,
  Paper,
  Typography,
} from "@mui/material";

import EmailOutlinedIcon from "@mui/icons-material/EmailOutlined";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";

import CustomTextField from "../common/CustomTextField";
import PrimaryButton from "../common/PrimaryButton";
import { login as loginRequest } from "../../services/authService";
import { useAuth } from "../../contexts/AuthContext";

export default function LoginForm() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setError("");
    setLoading(true);

    try {
      const response = await loginRequest({ username, password });
      login(response.token, response.username, response.role || "PATIENT");
      navigate("/dashboard");
    } catch (err) {
      setError("Invalid username or password.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      {error ? (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      ) : null}

      <CustomTextField
        label="Username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        startIcon={<EmailOutlinedIcon color="action" />}
      />

      <CustomTextField
        label="Password"
        type={showPassword ? "text" : "password"}
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        startIcon={<LockOutlinedIcon color="action" />}
        endIcon={
          <IconButton
            onClick={() => setShowPassword(!showPassword)}
            edge="end"
          >
            {showPassword ? <VisibilityOff /> : <Visibility />}
          </IconButton>
        }
      />

      <Box
        sx={{
          mt: 2,
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
        }}
      >
        <FormControlLabel
          control={<Checkbox color="primary" />}
          label="Remember Me"
        />

        <Link
          href="#"
          underline="hover"
          sx={{
            fontWeight: 500,
            color: "primary.main",
          }}
        >
          Forgot Password?
        </Link>
      </Box>

      <Box sx={{ mt: 3 }}>
        <PrimaryButton type="submit" disabled={loading}>
          {loading ? "Signing In..." : "SIGN IN"}
        </PrimaryButton>
      </Box>

      <Paper
        sx={{
          mt: 3,
          p: 2,
          bgcolor: "primary.50",
          border: "1px dashed",
          borderColor: "primary.200",
          borderRadius: 3,
        }}
      >
        <Typography variant="caption" sx={{ fontWeight: 600, color: "primary.main", display: "block", mb: 1 }}>
          Demo Credentials
        </Typography>
        <Box sx={{ display: "flex", flexWrap: "wrap", gap: 1 }}>
          <Chip
            label="admin / admin123"
            size="small"
            color="primary"
            variant="outlined"
            onClick={() => { setUsername("admin"); setPassword("admin123"); }}
            sx={{ cursor: "pointer" }}
          />
          <Chip
            label="doctor / doctor123"
            size="small"
            color="secondary"
            variant="outlined"
            onClick={() => { setUsername("doctor"); setPassword("doctor123"); }}
            sx={{ cursor: "pointer" }}
          />
        </Box>
      </Paper>
    </form>
  );
}
