import {
  Card,
  CardContent,
  Typography,
  List,
  ListItem,
  ListItemText,
} from "@mui/material";

const notifications = [
  {
    id: 1,
    title: "New patient registered",
    time: "5 mins ago",
  },
  {
    id: 2,
    title: "Lab report uploaded",
    time: "20 mins ago",
  },
  {
    id: 3,
    title: "Appointment cancelled",
    time: "45 mins ago",
  },
  {
    id: 4,
    title: "Medicine stock is low",
    time: "1 hour ago",
  },
];

export default function NotificationPanel() {
  return (
    <Card
      sx={{
        borderRadius: 4,
        boxShadow: "0 8px 20px rgba(0,0,0,.08)",
      }}
    >
      <CardContent>
        <Typography variant="h6" fontWeight={600} mb={2}>
          Notifications
        </Typography>

        <List>
          {notifications.map((item) => (
            <ListItem key={item.id} divider>
              <ListItemText
                primary={item.title}
                secondary={item.time}
              />
            </ListItem>
          ))}
        </List>
      </CardContent>
    </Card>
  );
}