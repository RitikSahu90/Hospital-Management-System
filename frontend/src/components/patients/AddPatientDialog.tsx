import { useState } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  Grid,
  TextField,
  MenuItem,
} from "@mui/material";

import type { Patient } from "../../types/patient";

interface Props {
  open: boolean;
  onClose: () => void;
  onSave: (patient: Patient) => void;
}

const emptyPatient: Patient = {
  id: 0,
  patientId: "",
  firstName: "",
  lastName: "",
  age: 0,
  gender: "Male",
  bloodGroup: "O+",
  phone: "",
  email: "",
  address: "",
  doctor: "",
  disease: "",
  status: "Admitted",
};

export default function AddPatientDialog({
  open,
  onClose,
  onSave,
}: Props) {
  const [patient, setPatient] = useState<Patient>(emptyPatient);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    setPatient({
      ...patient,
      [e.target.name]:
        e.target.name === "age"
          ? Number(e.target.value)
          : e.target.value,
    });
  };

  const handleSave = () => {
    onSave({
      ...patient,
      id: Date.now(),
      patientId: `PAT-${Math.floor(
        1000 + Math.random() * 9000
      )}`,
    });

    setPatient(emptyPatient);
    onClose();
  };

  return (
    <Dialog
      open={open}
      onClose={onClose}
      maxWidth="md"
      fullWidth
    >
      <DialogTitle>Add Patient</DialogTitle>

      <DialogContent>
        <Grid container spacing={2} sx={{ mt: 1 }}>
          <Grid size={{ xs: 12, md: 6 }}>
            <TextField
              fullWidth
              label="First Name"
              name="firstName"
              value={patient.firstName}
              onChange={handleChange}
            />
          </Grid>

          <Grid size={{ xs: 12, md: 6 }}>
            <TextField
              fullWidth
              label="Last Name"
              name="lastName"
              value={patient.lastName}
              onChange={handleChange}
            />
          </Grid>

          <Grid size={{ xs: 12, md: 4 }}>
            <TextField
              fullWidth
              label="Age"
              type="number"
              name="age"
              value={patient.age}
              onChange={handleChange}
            />
          </Grid>

          <Grid size={{ xs: 12, md: 4 }}>
            <TextField
              select
              fullWidth
              label="Gender"
              name="gender"
              value={patient.gender}
              onChange={handleChange}
            >
              <MenuItem value="Male">Male</MenuItem>
              <MenuItem value="Female">Female</MenuItem>
              <MenuItem value="Other">Other</MenuItem>
            </TextField>
          </Grid>

          <Grid size={{ xs: 12, md: 4 }}>
            <TextField
              select
              fullWidth
              label="Blood Group"
              name="bloodGroup"
              value={patient.bloodGroup}
              onChange={handleChange}
            >
              <MenuItem value="A+">A+</MenuItem>
              <MenuItem value="A-">A-</MenuItem>
              <MenuItem value="B+">B+</MenuItem>
              <MenuItem value="B-">B-</MenuItem>
              <MenuItem value="AB+">AB+</MenuItem>
              <MenuItem value="AB-">AB-</MenuItem>
              <MenuItem value="O+">O+</MenuItem>
              <MenuItem value="O-">O-</MenuItem>
            </TextField>
          </Grid>

          <Grid size={{ xs: 12, md: 6 }}>
            <TextField
              fullWidth
              label="Phone"
              name="phone"
              value={patient.phone}
              onChange={handleChange}
            />
          </Grid>

          <Grid size={{ xs: 12, md: 6 }}>
            <TextField
              fullWidth
              label="Email"
              name="email"
              value={patient.email}
              onChange={handleChange}
            />
          </Grid>

          <Grid size={{ xs: 12 }}>
            <TextField
              fullWidth
              label="Address"
              name="address"
              value={patient.address}
              onChange={handleChange}
            />
          </Grid>

          <Grid size={{ xs: 12, md: 6 }}>
            <TextField
              fullWidth
              label="Doctor"
              name="doctor"
              value={patient.doctor}
              onChange={handleChange}
            />
          </Grid>

          <Grid size={{ xs: 12, md: 6 }}>
            <TextField
              fullWidth
              label="Disease"
              name="disease"
              value={patient.disease}
              onChange={handleChange}
            />
          </Grid>

          <Grid size={{ xs: 12 }}>
            <TextField
              select
              fullWidth
              label="Status"
              name="status"
              value={patient.status}
              onChange={handleChange}
            >
              <MenuItem value="Admitted">Admitted</MenuItem>
              <MenuItem value="Under Treatment">
                Under Treatment
              </MenuItem>
              <MenuItem value="Discharged">
                Discharged
              </MenuItem>
            </TextField>
          </Grid>
        </Grid>
      </DialogContent>

      <DialogActions>
        <Button onClick={onClose}>Cancel</Button>

        <Button
          variant="contained"
          onClick={handleSave}
        >
          Save
        </Button>
      </DialogActions>
    </Dialog>
  );
}