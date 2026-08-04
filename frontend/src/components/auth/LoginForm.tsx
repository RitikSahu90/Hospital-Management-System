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
import { login as loginRequest, register as registerRequest } from "../../services/authService";
import { useAuth } from "../../contexts/AuthContext";

export default function LoginForm() {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [isRegistering, setIsRegistering] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setError("");
    if (isRegistering && password !== confirmPassword) {
      setError("Passwords do not match.");
      return;
    }
    setLoading(true);

    try {
      const response = isRegistering
        ? await registerRequest({ username, email, password })
        : await loginRequest({ username, password });
      login(response.token, response.username, response.role || "PATIENT");
      navigate("/dashboard");
    } catch (err) {
      setError(isRegistering ? "Unable to create your account. The username or email may already be in use." : "Invalid username or password.");
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
        label={isRegistering ? "Choose a username" : "Username"}
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        startIcon={<EmailOutlinedIcon color="action" />}
      />

      {isRegistering && (
        <CustomTextField
          label="Email address"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          startIcon={<EmailOutlinedIcon color="action" />}
        />
      )}

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

      {isRegistering && (
        <CustomTextField
          label="Confirm password"
          type={showPassword ? "text" : "password"}
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
          startIcon={<LockOutlinedIcon color="action" />}
        />
      )}

      {!isRegistering && <Box
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
      </Box>}

      <Box sx={{ mt: 3 }}>
        <PrimaryButton type="submit" disabled={loading}>
          {loading ? (isRegistering ? "Creating account..." : "Signing in...") : (isRegistering ? "CREATE ACCOUNT" : "SIGN IN")}
        </PrimaryButton>
      </Box>

      <Typography align="center" variant="body2" sx={{ mt: 2 }}>
        {isRegistering ? "Already have an account? " : "New patient? "}
        <Link component="button" type="button" underline="hover" onClick={() => { setIsRegistering((value) => !value); setError(""); }} sx={{ fontWeight: 700 }}>
          {isRegistering ? "Sign in" : "Create an account"}
        </Link>
      </Typography>

      {!isRegistering && <Paper
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
      </Paper>}
    </form>
  );
}
