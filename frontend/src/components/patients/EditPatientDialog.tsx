import { useEffect, useState } from "react";
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Grid,
  TextField,
  CircularProgress,
} from "@mui/material";
import type { Patient, PatientCreateRequest } from "../../types/patient";

interface Props {
  open: boolean;
  patient: Patient | null;
  saving: boolean;
  onClose: () => void;
  onSave: (patient: PatientCreateRequest) => void;
}

export default function EditPatientDialog({ open, patient, saving, onClose, onSave }: Props) {
  const [values, setValues] = useState<PatientCreateRequest>({
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
  });

  useEffect(() => {
    if (patient) {
      setValues({
        patientNumber: patient.patientNumber,
        firstName: patient.firstName,
        lastName: patient.lastName,
        email: patient.email || "",
        phone: patient.phone,
        dateOfBirth: patient.dateOfBirth,
        gender: patient.gender,
        address: patient.address ?? "",
        bloodGroup: patient.bloodGroup ?? "",
        diagnosis: patient.diagnosis ?? "Pending",
      });
    }
  }, [patient]);

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setValues((current) => ({ ...current, [event.target.name]: event.target.value }));
  };

  return (
    <Dialog open={open} onClose={() => !saving && onClose()} fullWidth maxWidth="md">
      <DialogTitle>Edit Patient</DialogTitle>
      <DialogContent>
        <Grid container spacing={2} sx={{ mt: 1 }}>
          <Grid size={{ xs: 12, md: 6 }}>
            <TextField
              fullWidth
              label="Patient Number"
              name="patientNumber"
              value={values.patientNumber}
              onChange={handleChange}
              required
            />
          </Grid>
          <Grid size={{ xs: 12, md: 6 }}>
            <TextField
              fullWidth
              label="First Name"
              name="firstName"
              value={values.firstName}
              onChange={handleChange}
            />
          </Grid>
          <Grid size={{ xs: 12, md: 6 }}>
            <TextField
              fullWidth
              label="Last Name"
              name="lastName"
              value={values.lastName}
              onChange={handleChange}
            />
          </Grid>
          <Grid size={{ xs: 12, md: 6 }}>
            <TextField
              fullWidth
              label="Phone"
              name="phone"
              value={values.phone}
              onChange={handleChange}
            />
          </Grid>
          <Grid size={{ xs: 12, md: 6 }}>
            <TextField
              fullWidth
              label="Email"
              name="email"
              value={values.email}
              onChange={handleChange}
            />
          </Grid>
          <Grid size={{ xs: 12, md: 6 }}>
            <TextField
              fullWidth
              label="Date of Birth"
              name="dateOfBirth"
              type="date"
              value={values.dateOfBirth}
              onChange={handleChange}
              slotProps={{ inputLabel: { shrink: true } }}
              required
            />
          </Grid>
          <Grid size={{ xs: 12, md: 6 }}>
            <TextField
              fullWidth
              select
              label="Gender"
              name="gender"
              value={values.gender}
              onChange={handleChange}
              slotProps={{ select: { native: true } }}
            >
              <option value="MALE">Male</option>
              <option value="FEMALE">Female</option>
              <option value="OTHER">Other</option>
            </TextField>
          </Grid>
          <Grid size={{ xs: 12, md: 6 }}>
            <TextField
              fullWidth
              label="Blood Group"
              name="bloodGroup"
              value={values.bloodGroup}
              onChange={handleChange}
            />
          </Grid>
          <Grid size={{ xs: 12 }}>
            <TextField
              fullWidth
              label="Address"
              name="address"
              value={values.address}
              onChange={handleChange}
            />
          </Grid>
          <Grid size={{ xs: 12 }}>
            <TextField
              fullWidth
              select
              label="Diagnosis"
              name="diagnosis"
              value={values.diagnosis}
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
        <Button onClick={onClose} disabled={saving}>
          Cancel
        </Button>
        <Button variant="contained" onClick={() => onSave(values)} disabled={saving}>
          {saving ? <CircularProgress size={20} /> : "Save"}
        </Button>
      </DialogActions>
    </Dialog>
  );
}
