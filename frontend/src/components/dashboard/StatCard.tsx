import { Card, CardContent, Box, Typography } from "@mui/material";
import type { ReactNode } from "react";

interface Props {
  title: string;
  value: string;
  icon: ReactNode;
  color: string;
}

export default function StatCard({
  title,
  value,
  icon,
  color,
}: Props) {
  return (
    <Card
      sx={{
        borderRadius: 4,
        boxShadow: "0 10px 25px rgba(0,0,0,.08)",
        transition: ".3s",

        "&:hover": {
          transform: "translateY(-6px)",
        },
      }}
    >
      <CardContent>
        <Box
          display="flex"
          justifyContent="space-between"
          alignItems="center"
        >
          <Box>
            <Typography color="text.secondary">
              {title}
            </Typography>

            <Typography
              mt={1}
              variant="h4"
              fontWeight={700}
            >
              {value}
            </Typography>
          </Box>

          <Box
            sx={{
              bgcolor: color,
              width: 60,
              height: 60,
              borderRadius: "50%",
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              color: "#fff",
            }}
          >
            {icon}
          </Box>
        </Box>
      </CardContent>
    </Card>
  );
}