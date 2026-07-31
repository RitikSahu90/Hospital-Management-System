import { useState } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  Grid,
  TextField,
} from "@mui/material";

import type { PatientCreateRequest } from "../../types/patient";

interface Props {
  open: boolean;
  onClose: () => void;
  onSave: (patient: PatientCreateRequest) => void;
}

const emptyPatient: PatientCreateRequest = {
  firstName: "",
  lastName: "",
  email: "",
  phone: "",
  diagnosis: "",
};

export default function AddPatientDialog({
  open,
  onClose,
  onSave,
}: Props) {
  const [patient, setPatient] = useState<PatientCreateRequest>(emptyPatient);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPatient({
      ...patient,
      [e.target.name]: e.target.value,
    });
  };

  const handleSave = () => {
    onSave(patient);
    setPatient(emptyPatient);
    onClose();
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="md" fullWidth>
      <DialogTitle>Add Patient</DialogTitle>

      <DialogContent>
        <Grid container spacing={2} sx={{ mt: 1 }}>
          <Grid size={{ xs: 12, md: 6 }}>
            <TextField fullWidth label="First Name" name="firstName" value={patient.firstName} onChange={handleChange} />
          </Grid>

          <Grid size={{ xs: 12, md: 6 }}>
            <TextField fullWidth label="Last Name" name="lastName" value={patient.lastName} onChange={handleChange} />
          </Grid>

          <Grid size={{ xs: 12, md: 6 }}>
            <TextField fullWidth label="Phone" name="phone" value={patient.phone} onChange={handleChange} />
          </Grid>

          <Grid size={{ xs: 12, md: 6 }}>
            <TextField fullWidth label="Email" name="email" value={patient.email} onChange={handleChange} />
          </Grid>

          <Grid size={{ xs: 12 }}>
            <TextField fullWidth label="Diagnosis" name="diagnosis" value={patient.diagnosis} onChange={handleChange} />
          </Grid>
        </Grid>
      </DialogContent>

      <DialogActions>
        <Button onClick={onClose}>Cancel</Button>
        <Button variant="contained" onClick={handleSave}>
          Save
        </Button>
      </DialogActions>
    </Dialog>
  );
}