import {
  Card,
  CardContent,
  LinearProgress,
  Typography,
  Box,
} from "@mui/material";

const medicines = [
  { name: "Paracetamol", value: 80 },
  { name: "Insulin", value: 40 },
  { name: "Amoxicillin", value: 65 },
  { name: "Vitamin C", value: 95 },
];

export default function MedicineStock() {
  return (
    <Card sx={{ borderRadius: 4 }}>
      <CardContent>
        <Typography variant="h6" fontWeight={600} mb={3}>
          Medicine Stock
        </Typography>

        {medicines.map((item) => (
          <Box key={item.name} mb={2}>
            <Typography variant="body2">{item.name}</Typography>

            <LinearProgress
              variant="determinate"
              value={item.value}
              sx={{
                mt: 1,
                height: 8,
                borderRadius: 5,
              }}
            />
          </Box>
        ))}
      </CardContent>
    </Card>
  );
}