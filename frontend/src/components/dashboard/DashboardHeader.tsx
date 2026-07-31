import { Box, Typography, Avatar } from "@mui/material";
import WavingHandIcon from "@mui/icons-material/WavingHand";

export default function DashboardHeader() {
  const today = new Date().toLocaleDateString("en-IN", {
    weekday: "long",
    day: "numeric",
    month: "long",
    year: "numeric",
  });

  return (
    <Box
      sx={{
        bgcolor: "#fff",
        borderRadius: 4,
        p: 3,
        mb: 4,
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        boxShadow: "0 8px 25px rgba(0,0,0,.06)",
      }}
    >
      <Box>
        <Typography
          variant="h4"
          fontWeight={700}
          color="#1E3A8A"
        >
          Welcome Back
          <WavingHandIcon
            sx={{
              ml: 1,
              color: "#FFC107",
              verticalAlign: "middle",
            }}
          />
        </Typography>

        <Typography
          mt={1}
          color="text.secondary"
        >
          Here's what's happening in your hospital today.
        </Typography>
      </Box>

      <Box display="flex" alignItems="center" gap={2}>
        <Avatar
          sx={{
            width: 56,
            height: 56,
            bgcolor: "#1976D2",
          }}
        >
          A
        </Avatar>

        <Box>
          <Typography fontWeight={600}>
            Administrator
          </Typography>

          <Typography variant="body2" color="text.secondary">
            {today}
          </Typography>
        </Box>
      </Box>
    </Box>
  );
}