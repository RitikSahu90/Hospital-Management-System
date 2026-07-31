import { Card, CardContent, Typography, Stack } from "@mui/material";

export default function EmergencyCard() {
  return (
    <Card sx={{ borderRadius: 4 }}>
      <CardContent>
        <Typography variant="h6" fontWeight={600}>
          Emergency Cases
        </Typography>

        <Stack spacing={2} mt={3}>
          <Typography>🚑 ICU Patients : 08</Typography>
          <Typography>🩸 Critical Cases : 03</Typography>
          <Typography>🛏 Beds Available : 21</Typography>
          <Typography>❤️ Ambulances Ready : 05</Typography>
        </Stack>
      </CardContent>
    </Card>
  );
}