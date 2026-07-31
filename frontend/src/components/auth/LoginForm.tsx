import { useState } from "react";

import {
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

export default function LoginForm() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);

  return (
    <>
      {/* Email Field */}
      <CustomTextField
        label="Email Address"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        startIcon={<EmailOutlinedIcon color="action" />}
      />

      {/* Password Field */}
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

      {/* Remember Me & Forgot Password */}
      <Box
        mt={2}
        display="flex"
        justifyContent="space-between"
        alignItems="center"
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

      {/* Sign In Button */}
      <Box mt={3}>
        <PrimaryButton type="submit">
          SIGN IN
        </PrimaryButton>
      </Box>
    </>
  );
}