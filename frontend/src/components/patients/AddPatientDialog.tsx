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
  patientNumber: "",
  firstName: "",
  lastName: "",
  email: "",
  phone: "",
  dateOfBirth: "",
  gender: "OTHER",
  address: "",
  bloodGroup: "",
  diagnosis: "Pending",
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
          <Grid size={{ xs: 12, md: 6 }}><TextField fullWidth label="Patient Number" name="patientNumber" value={patient.patientNumber} onChange={handleChange} required /></Grid>
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

          <Grid size={{ xs: 12, md: 6 }}><TextField fullWidth label="Date of Birth" name="dateOfBirth" type="date" value={patient.dateOfBirth} onChange={handleChange} slotProps={{ inputLabel: { shrink: true } }} required /></Grid>
          <Grid size={{ xs: 12, md: 6 }}><TextField fullWidth select label="Gender" name="gender" value={patient.gender} onChange={handleChange} slotProps={{ select: { native: true } }}><option value="MALE">Male</option><option value="FEMALE">Female</option><option value="OTHER">Other</option></TextField></Grid>
          <Grid size={{ xs: 12, md: 6 }}><TextField fullWidth label="Blood Group" name="bloodGroup" value={patient.bloodGroup} onChange={handleChange} /></Grid>
          <Grid size={{ xs: 12 }}><TextField fullWidth label="Address" name="address" value={patient.address} onChange={handleChange} /></Grid>

          <Grid size={{ xs: 12 }}>
            <TextField
              fullWidth
              select
              label="Diagnosis"
              name="diagnosis"
              value={patient.diagnosis}
              onChange={handleChange}
              slotProps={{ select: { native: true } }}
            >
              <option value="Pending">Pending</option>
              <option value="Ongoing">Ongoing</option>
              <option value="Completed">Completed</option>
              <option value="Discharged">Discharged</option>
              <option value="Cancelled">Cancelled</option>
            </TextField>
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