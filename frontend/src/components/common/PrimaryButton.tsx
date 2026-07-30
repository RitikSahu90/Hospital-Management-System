import Button from "@mui/material/Button";
import type { ButtonProps } from "@mui/material/Button";
import type { ReactNode } from "react";

interface PrimaryButtonProps extends ButtonProps {
  children: ReactNode;
}

export default function PrimaryButton({
  children,
  sx,
  ...props
}: PrimaryButtonProps) {
  return (
    <Button
      fullWidth
      variant="contained"
      {...props}
      sx={{
        py: 1.5,
        borderRadius: "14px",
        fontSize: "16px",
        fontWeight: 600,
        textTransform: "none",
        background: "linear-gradient(135deg, #1565C0, #42A5F5)",
        boxShadow: "0 10px 20px rgba(21,101,192,0.30)",
        transition: "all 0.3s ease",

        "&:hover": {
          background: "linear-gradient(135deg, #0D47A1, #1976D2)",
          transform: "translateY(-2px)",
          boxShadow: "0 14px 24px rgba(21,101,192,0.40)",
        },

        "&:active": {
          transform: "scale(0.98)",
        },

        ...sx,
      }}
    >
      {children}
    </Button>
  );
}