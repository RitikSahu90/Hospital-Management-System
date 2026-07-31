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

const patients = [
  {
    id: 1,
    name: "Rahul Sharma",
    disease: "Fever",
    status: "Admitted",
  },
  {
    id: 2,
    name: "Priya Patel",
    disease: "Diabetes",
    status: "Stable",
  },
  {
    id: 3,
    name: "Amit Kumar",
    disease: "Fracture",
    status: "Critical",
  },
  {
    id: 4,
    name: "Neha Singh",
    disease: "Migraine",
    status: "Recovered",
  },
];

const getChipColor = (
  status: string
): "success" | "warning" | "error" | "info" => {
  switch (status) {
    case "Admitted":
      return "warning";
    case "Stable":
      return "success";
    case "Critical":
      return "error";
    default:
      return "info";
  }
};

export default function RecentPatients() {
  return (
    <Card
      sx={{
        borderRadius: 4,
        boxShadow: "0 8px 20px rgba(0,0,0,.08)",
        height: "100%",
      }}
    >
      <CardContent>
        <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
          Recent Patients
        </Typography>

        <List>
          {patients.map((patient) => (
            <ListItem key={patient.id} divider>
              <ListItemAvatar>
                <Avatar>{patient.name.charAt(0)}</Avatar>
              </ListItemAvatar>

              <ListItemText
                primary={patient.name}
                secondary={patient.disease}
              />

              <Chip
                label={patient.status}
                color={getChipColor(patient.status)}
                size="small"
              />
            </ListItem>
          ))}
        </List>
      </CardContent>
    </Card>
  );
}