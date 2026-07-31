import { Box, Chip, IconButton, Tooltip } from "@mui/material";
import { DataGrid } from "@mui/x-data-grid";
import type { GridColDef } from "@mui/x-data-grid";

import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";

import type { Patient } from "../../types/patient";

interface Props {
  patients: Patient[];
  onEdit: (patient: Patient) => void;
  onDelete: (patient: Patient) => void;
}

export default function PatientTable({
  patients,
  onEdit,
  onDelete,
}: Props) {
  const columns: GridColDef[] = [
    {
      field: "patientId",
      headerName: "Patient ID",
      width: 120,
    },
    {
      field: "name",
      headerName: "Patient Name",
      width: 180,
      valueGetter: (_, row) =>
        `${row.firstName} ${row.lastName}`,
    },
    {
      field: "age",
      headerName: "Age",
      width: 80,
    },
    {
      field: "gender",
      headerName: "Gender",
      width: 100,
    },
    {
      field: "bloodGroup",
      headerName: "Blood",
      width: 100,
    },
    {
      field: "doctor",
      headerName: "Doctor",
      width: 170,
    },
    {
      field: "disease",
      headerName: "Disease",
      width: 170,
    },
    {
      field: "phone",
      headerName: "Phone",
      width: 140,
    },
    {
      field: "status",
      headerName: "Status",
      width: 170,
      renderCell: (params) => {
        const value = params.value;

        let color:
          | "success"
          | "error"
          | "warning"
          | "default" = "default";

        if (value === "Admitted") color = "success";
        if (value === "Under Treatment") color = "warning";
        if (value === "Discharged") color = "error";

        return (
          <Chip
            label={value}
            color={color}
            size="small"
          />
        );
      },
    },
    {
      field: "actions",
      headerName: "Actions",
      width: 120,
      sortable: false,
      filterable: false,
      renderCell: (params) => (
        <>
          <Tooltip title="Edit">
            <IconButton
              color="primary"
              onClick={() => onEdit(params.row)}
            >
              <EditIcon />
            </IconButton>
          </Tooltip>

          <Tooltip title="Delete">
            <IconButton
              color="error"
              onClick={() => onDelete(params.row)}
            >
              <DeleteIcon />
            </IconButton>
          </Tooltip>
        </>
      ),
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
    </Box>
  );
}