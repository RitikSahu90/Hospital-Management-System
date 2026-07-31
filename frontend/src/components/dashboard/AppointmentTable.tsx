import {
  Card,
  CardContent,
  Typography,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Chip,
} from "@mui/material";

const appointments = [
  {
    id: 1,
    patient: "Rahul Sharma",
    doctor: "Dr. Mehta",
    time: "09:00 AM",
    status: "Confirmed",
  },
  {
    id: 2,
    patient: "Priya Patel",
    doctor: "Dr. Gupta",
    time: "10:30 AM",
    status: "Pending",
  },
  {
    id: 3,
    patient: "Amit Kumar",
    doctor: "Dr. Singh",
    time: "11:15 AM",
    status: "Completed",
  },
  {
    id: 4,
    patient: "Neha Singh",
    doctor: "Dr. Roy",
    time: "02:00 PM",
    status: "Confirmed",
  },
];

const getChipColor = (
  status: string
): "success" | "warning" | "info" => {
  switch (status) {
    case "Confirmed":
      return "success";
    case "Pending":
      return "warning";
    default:
      return "info";
  }
};

export default function AppointmentTable() {
  return (
    <Card
      sx={{
        borderRadius: 4,
        boxShadow: "0 8px 20px rgba(0,0,0,.08)",
      }}
    >
      <CardContent>
        <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
          Today's Appointments
        </Typography>

        <Table size="small">
          <TableHead>
            <TableRow>
              <TableCell>Patient</TableCell>
              <TableCell>Doctor</TableCell>
              <TableCell>Time</TableCell>
              <TableCell>Status</TableCell>
            </TableRow>
          </TableHead>

          <TableBody>
            {appointments.map((row) => (
              <TableRow key={row.id}>
                <TableCell>{row.patient}</TableCell>
                <TableCell>{row.doctor}</TableCell>
                <TableCell>{row.time}</TableCell>
                <TableCell>
                  <Chip
                    label={row.status}
                    color={getChipColor(row.status)}
                    size="small"
                  />
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </CardContent>
    </Card>
  );
}