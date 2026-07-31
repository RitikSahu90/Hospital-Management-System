import {
  ResponsiveContainer,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
} from "recharts";

import { Card, CardContent, Typography } from "@mui/material";

const data = [
  { month: "Jan", revenue: 2.4 },
  { month: "Feb", revenue: 3.2 },
  { month: "Mar", revenue: 4.1 },
  { month: "Apr", revenue: 5.3 },
  { month: "May", revenue: 6.5 },
  { month: "Jun", revenue: 7.2 },
];

export default function RevenueChart() {
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
          Revenue Overview
        </Typography>

        <ResponsiveContainer width="100%" height={300}>
          <BarChart data={data}>
            <CartesianGrid strokeDasharray="3 3" />

            <XAxis dataKey="month" />

            <YAxis />

            <Tooltip />

            <Bar
              dataKey="revenue"
              fill="#2E7D32"
              radius={[8, 8, 0, 0]}
            />
          </BarChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  );
}