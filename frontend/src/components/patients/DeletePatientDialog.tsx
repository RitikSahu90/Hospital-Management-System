import { Button, Dialog, DialogActions, DialogContent, DialogTitle, Typography } from "@mui/material";
import type { Patient } from "../../types/patient";

interface Props { open: boolean; patient: Patient | null; deleting: boolean; onClose: () => void; onConfirm: () => void; }
export default function DeletePatientDialog({ open, patient, deleting, onClose, onConfirm }: Props) {
  return <Dialog open={open} onClose={() => !deleting && onClose()}><DialogTitle>Delete Patient</DialogTitle><DialogContent><Typography>Delete {patient ? `${patient.firstName} ${patient.lastName}` : "this patient"}? This cannot be undone.</Typography></DialogContent><DialogActions><Button onClick={onClose} disabled={deleting}>Cancel</Button><Button color="error" variant="contained" onClick={onConfirm} disabled={deleting}>{deleting ? "Deleting..." : "Delete"}</Button></DialogActions></Dialog>;
}
