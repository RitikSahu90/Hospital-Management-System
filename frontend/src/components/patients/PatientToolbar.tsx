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
  showAdd?: boolean;
}

export default function PatientToolbar({
  search,
  setSearch,
  onAdd,
  showAdd = true,
}: Props) {
  return (
    <Box
      sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 2 }}
    >
      <TextField
        label="Search Patient"
        size="small"
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        sx={{ width: 300 }}
      />

      {showAdd && (
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={onAdd}
        >
          Add Patient
        </Button>
      )}
    </Box>
  );
}