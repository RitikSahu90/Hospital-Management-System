import { Box, Button, Chip, Typography } from "@mui/material";
import { DataGrid } from "@mui/x-data-grid";
import type { GridColDef } from "@mui/x-data-grid";

import type { Patient } from "../../types/patient";

interface Props {
  patients: Patient[];
  onEdit: (patient: Patient) => void;
  onDelete: (patient: Patient) => void;
}

export default function PatientTable({ patients, onEdit, onDelete }: Props) {
  const columns: GridColDef[] = [
    {
      field: "id",
      headerName: "Patient ID",
      width: 120,
      valueGetter: (_, row) => row.id,
    },
    {
      field: "name",
      headerName: "Patient Name",
      width: 220,
      valueGetter: (_, row) => `${row.firstName} ${row.lastName}`,
    },
    {
      field: "email",
      headerName: "Email",
      width: 220,
    },
    {
      field: "phone",
      headerName: "Phone",
      width: 160,
    },
    {
      field: "diagnosis",
      headerName: "Diagnosis",
      width: 260,
      renderCell: (params) => {
        const value = params.value as string | undefined;

        return (
          <Chip
            label={value || "—"}
            color={value ? "primary" : "default"}
            size="small"
          />
        );
      },
    },
    {
      field: "actions",
      headerName: "Actions",
      width: 180,
      sortable: false,
      renderCell: (params) => <Box sx={{ display: "flex", gap: 1, pt: 0.8 }}><Button size="small" onClick={() => onEdit(params.row as Patient)}>Edit</Button><Button size="small" color="error" onClick={() => onDelete(params.row as Patient)}>Delete</Button></Box>,
    },
  ];

  return (
    <Box
      sx={{
        height: 650,
        width: "100%",
        bgcolor: "background.paper",
        borderRadius: 2,
        p: 2,
      }}
    >
      {patients.length === 0 ? (
        <Box sx={{ height: "100%", display: "flex", alignItems: "center", justifyContent: "center" }}>
          <Typography color="text.secondary">No patients found.</Typography>
        </Box>
      ) : (
        <DataGrid
          rows={patients}
          columns={columns}
          getRowId={(row) => row.id}
          pageSizeOptions={[5, 10, 20]}
          initialState={{
            pagination: {
              paginationModel: {
                page: 0,
                pageSize: 10,
              },
            },
          }}
          disableRowSelectionOnClick
        />
      )}
    </Box>
  );
}
