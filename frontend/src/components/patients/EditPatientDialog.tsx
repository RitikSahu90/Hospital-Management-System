import { useEffect, useState } from "react";
import { Button, Dialog, DialogActions, DialogContent, DialogTitle, Grid, TextField } from "@mui/material";
import type { Patient, PatientCreateRequest } from "../../types/patient";

interface Props { open: boolean; patient: Patient | null; saving: boolean; onClose: () => void; onSave: (patient: PatientCreateRequest) => void; }

export default function EditPatientDialog({ open, patient, saving, onClose, onSave }: Props) {
  const [values, setValues] = useState<PatientCreateRequest>({ patientNumber: "", firstName: "", lastName: "", email: "", phone: "", dateOfBirth: "", gender: "OTHER", address: "", bloodGroup: "", diagnosis: "" });
  useEffect(() => { if (patient) setValues({ patientNumber: patient.patientNumber, firstName: patient.firstName, lastName: patient.lastName, email: patient.email, phone: patient.phone, dateOfBirth: patient.dateOfBirth, gender: patient.gender, address: patient.address ?? "", bloodGroup: patient.bloodGroup ?? "", diagnosis: patient.diagnosis ?? "" }); }, [patient]);
  const change = (event: React.ChangeEvent<HTMLInputElement>) => setValues((current) => ({ ...current, [event.target.name]: event.target.value }));
  return <Dialog open={open} onClose={() => !saving && onClose()} fullWidth maxWidth="md"><DialogTitle>Edit Patient</DialogTitle><DialogContent><Grid container spacing={2} sx={{ mt: 1 }}>{Object.entries(values).map(([name, value]) => <Grid key={name} size={{ xs: 12, md: name === "diagnosis" ? 12 : 6 }}><TextField name={name} label={name.replace(/([A-Z])/g, " $1")} value={value} onChange={change} fullWidth /></Grid>)}</Grid></DialogContent><DialogActions><Button onClick={onClose} disabled={saving}>Cancel</Button><Button variant="contained" onClick={() => onSave(values)} disabled={saving}>{saving ? "Saving..." : "Save"}</Button></DialogActions></Dialog>;
}
