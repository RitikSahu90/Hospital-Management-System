import { Box, Button, TextField } from "@mui/material";
import AddIcon from "@mui/icons-material/Add";

interface Props {
  search: string;
  setSearch: (value: string) => void;
  onAdd: () => void;
}

export default function DoctorToolbar({
  search,
  setSearch,
  onAdd,
}: Props) {
  return (
    <Box
      sx={{ display: "flex", justifyContent: "space-between", mb: 2 }}
    >
      <TextField
        label="Search Doctor"
        size="small"
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        sx={{ width: 300 }}
      />

      <Button
        variant="contained"
        startIcon={<AddIcon />}
        onClick={onAdd}
      >
        Add Doctor
      </Button>
    </Box>
  );
}