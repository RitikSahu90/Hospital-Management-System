import {
  Box,
  Button,
  TextField,
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";

interface Props {
  search: string;
  setSearch: (value: string) => void;
  onAdd: () => void;
}

export default function PatientToolbar({
  search,
  setSearch,
  onAdd,
}: Props) {
  return (
    <Box
      display="flex"
      justifyContent="space-between"
      alignItems="center"
      mb={2}
    >
      <TextField
        label="Search Patient"
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
        Add Patient
      </Button>
    </Box>
  );
}