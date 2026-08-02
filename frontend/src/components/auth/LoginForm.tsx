import { useState } from "react";
import { useNavigate } from "react-router-dom";

import {
  Alert,
  Box,
  Checkbox,
  FormControlLabel,
  IconButton,
  Link,
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
    </form>
  );
}