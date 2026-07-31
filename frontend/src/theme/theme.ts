import { createTheme } from "@mui/material/styles";
import "@fontsource/poppins";

const theme = createTheme({
  palette: {
    mode: "light",

    primary: {
      main: "#1565C0",
    },

    secondary: {
      main: "#42A5F5",
    },

    background: {
      default: "#F4F8FC",
      paper: "#FFFFFF",
    },

    text: {
      primary: "#1A237E",
      secondary: "#5F6368",
    },
  },

  typography: {
    fontFamily: "Poppins, sans-serif",

    h1: {
      fontWeight: 700,
    },

    h2: {
      fontWeight: 700,
    },

    h3: {
      fontWeight: 700,
    },

    h4: {
      fontWeight: 600,
    },

    h5: {
      fontWeight: 600,
    },

    h6: {
      fontWeight: 600,
    },

    button: {
      textTransform: "none",
      fontWeight: 600,
    },
  },

  shape: {
    borderRadius: 18,
  },

  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: 14,
          padding: "12px 24px",
          boxShadow: "none",
        },
      },
    },

    MuiTextField: {
      defaultProps: {
        variant: "outlined",
        fullWidth: true,
      },
    },

    MuiOutlinedInput: {
      styleOverrides: {
        root: {
          borderRadius: 14,
        },
      },
    },
  },
});

export default theme;