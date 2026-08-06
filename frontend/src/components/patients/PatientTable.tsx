import { Box, Button, Chip, Typography } from "@mui/material";
import { DataGrid } from "@mui/x-data-grid";
import type { GridColDef } from "@mui/x-data-grid";

import type { Patient } from "../../types/patient";

interface Props {
  patients: Patient[];
  onEdit: (patient: Patient) => void;
  onDelete: (patient: Patient) => void;
  onAssignDoctor: (patient: Patient) => void;
  canEdit?: boolean;
  canDelete?: boolean;
}

export default function PatientTable({
  patients,
  onEdit,
  onDelete,
  onAssignDoctor,
  canEdit = true,
  canDelete = true,
}: Props) {
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
    ...(canEdit
      ? [
          {
            field: "doctorAssign",
            headerName: "Doctor Assign",
            width: 220,
            sortable: false,
            renderCell: (params: any) => {
              const name = params.row.assignedDoctorName;
              return name ? (
                <Box sx={{ display: "flex", alignItems: "center", gap: 1, pt: 0.8 }}>
                  <Typography variant="body2" sx={{ fontWeight: "medium" }}>
                    {name}
                  </Typography>
                  <Button
                    size="small"
                    variant="text"
                    onClick={() => onAssignDoctor(params.row as Patient)}
                  >
                    Change
                  </Button>
                </Box>
              ) : (
                <Box sx={{ pt: 0.8 }}>
                  <Button
                    size="small"
                    variant="outlined"
                    onClick={() => onAssignDoctor(params.row as Patient)}
                  >
                    Assign Doctor
                  </Button>
                </Box>
              );
            },
          },
        ]
      : []),
    {
      field: "assignDate",
      headerName: "Assign Date",
      width: 150,
      valueGetter: (_, row) => (row as any).assignDate || "—",
    },
    ...(canEdit || canDelete
      ? [
          {
            field: "actions",
            headerName: "Actions",
            width: 180,
            sortable: false,
            renderCell: (params: any) => (
              <Box sx={{ display: "flex", gap: 1, pt: 0.8 }}>
                {canEdit && (
                  <Button size="small" onClick={() => onEdit(params.row as Patient)}>
                    Edit
                  </Button>
                )}
                {canDelete && (
                  <Button color="error" size="small" onClick={() => onDelete(params.row as Patient)}>
                    Delete
                  </Button>
                )}
              </Box>
            ),
          },
        ]
      : []),
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
