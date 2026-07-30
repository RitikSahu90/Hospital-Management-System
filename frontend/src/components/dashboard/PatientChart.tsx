import {
  ResponsiveContainer,
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
} from "recharts";
import { Card, CardContent, Typography } from "@mui/material";

const data = [
  { month: "Jan", patients: 120 },
  { month: "Feb", patients: 180 },
  { month: "Mar", patients: 240 },
  { month: "Apr", patients: 310 },
  { month: "May", patients: 390 },
  { month: "Jun", patients: 470 },
];

export default function PatientChart() {
  return (
    <Card
      sx={{
        borderRadius: 4,
        boxShadow: "0 8px 20px rgba(0,0,0,.08)",
        height: 400,
      }}
    >
      <CardContent>
        <Typography variant="h6" fontWeight={600} mb={2}>
          Patient Statistics
        </Typography>

        <ResponsiveContainer width="100%" height={300}>
          <LineChart data={data}>
            <CartesianGrid strokeDasharray="3 3" />

            <XAxis dataKey="month" />

            <YAxis />

            <Tooltip />

            <Line
              type="monotone"
              dataKey="patients"
              stroke="#1976D2"
              strokeWidth={3}
            />
          </LineChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  );
}