import {
  Avatar,
  Card,
  CardContent,
  Chip,
  List,
  ListItem,
  ListItemAvatar,
  ListItemText,
  Typography,
} from "@mui/material";

const doctors = [
  { id: 1, name: "Dr. Mehta", department: "Cardiology", status: "Available" },
  { id: 2, name: "Dr. Gupta", department: "Neurology", status: "Busy" },
  { id: 3, name: "Dr. Roy", department: "Orthopedics", status: "Available" },
  { id: 4, name: "Dr. Singh", department: "ENT", status: "On Leave" },
];

export default function DoctorAvailability() {
  return (
    <Card sx={{ borderRadius: 4 }}>
      <CardContent>
        <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
          Doctor Availability
        </Typography>

        <List>
          {doctors.map((doctor) => (
            <ListItem key={doctor.id} divider>
              <ListItemAvatar>
                <Avatar>{doctor.name.charAt(3)}</Avatar>
              </ListItemAvatar>

              <ListItemText
                primary={doctor.name}
                secondary={doctor.department}
              />

              <Chip
                label={doctor.status}
                color={
                  doctor.status === "Available"
                    ? "success"
                    : doctor.status === "Busy"
                    ? "warning"
                    : "default"
                }
              />
            </ListItem>
          ))}
        </List>
      </CardContent>
    </Card>
  );
}