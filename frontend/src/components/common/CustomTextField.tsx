import type { ChangeEvent, ReactNode } from "react";
import TextField from "@mui/material/TextField";
import InputAdornment from "@mui/material/InputAdornment";

interface CustomTextFieldProps {
  label: string;
  type?: string;
  value?: string;
  onChange?: (event: ChangeEvent<HTMLInputElement>) => void;
  startIcon?: ReactNode;
  endIcon?: ReactNode;
}

export default function CustomTextField({
  label,
  type = "text",
  value,
  onChange,
  startIcon,
  endIcon,
}: CustomTextFieldProps) {
  return (
    <TextField
      fullWidth
      variant="outlined"
      margin="normal"
      label={label}
      type={type}
      value={value}
      onChange={onChange}
      slotProps={{
        input: {
          startAdornment: startIcon ? (
            <InputAdornment position="start">
              {startIcon}
            </InputAdornment>
          ) : undefined,

          endAdornment: endIcon ? (
            <InputAdornment position="end">
              {endIcon}
            </InputAdornment>
          ) : undefined,
        },
      }}
      sx={{
        "& .MuiOutlinedInput-root": {
          borderRadius: "14px",
          backgroundColor: "#FFFFFF",
          transition: "all 0.3s ease",

          "& fieldset": {
            borderColor: "#D7E3F4",
          },

          "&:hover fieldset": {
            borderColor: "#1565C0",
          },

          "&.Mui-focused fieldset": {
            borderColor: "#1565C0",
            borderWidth: "2px",
          },
        },

        "& .MuiInputLabel-root": {
          color: "#5F6368",
        },

        "& .MuiInputLabel-root.Mui-focused": {
          color: "#1565C0",
        },

        "& .MuiInputAdornment-root svg": {
          color: "#6B7280",
        },
      }}
    />
  );
}