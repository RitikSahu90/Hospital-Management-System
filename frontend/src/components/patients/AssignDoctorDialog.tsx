import { useEffect, useState } from "react";
import {
  Alert,
  Box,
  Button,
  Chip,
  CircularProgress,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  MenuItem,
  TextField,
  Typography,
} from "@mui/material";
import { getDoctors, getAvailability } from "../../services/doctorService";
import { createAppointment } from "../../services/appointmentService";
import type { Doctor, Availability } from "../../types/clinical";
import type { Patient } from "../../types/patient";

interface Props {
  open: boolean;
  patient: Patient | null;
  onClose: () => void;
  onSuccess: () => void;
}

export default function AssignDoctorDialog({ open, patient, onClose, onSuccess }: Props) {
  const [doctors, setDoctors] = useState<Doctor[]>([]);
  const [selectedDoctorId, setSelectedDoctorId] = useState<string>("");
  const [availabilities, setAvailabilities] = useState<Availability[]>([]);
  const [loadingDoctors, setLoadingDoctors] = useState(false);
  const [loadingAvail, setLoadingAvail] = useState(false);
  
  const [date, setDate] = useState("");
  const [time, setTime] = useState("");
  const [reason, setReason] = useState("Regular Consultation");
  const [error, setError] = useState("");
  const [saving, setSaving] = useState(false);

  // Load doctors on mount
  useEffect(() => {
    if (open) {
      const loadDoctors = async () => {
        try {
          setLoadingDoctors(true);
          const data = await getDoctors();
          setDoctors(data);
          if (data.length > 0) {
            setSelectedDoctorId(String(data[0].id));
          }
        } catch {
          setError("Failed to load doctors list.");
        } finally {
          setLoadingDoctors(false);
        }
      };
      void loadDoctors();
      setDate("");
      setTime("");
      setReason("Regular Consultation");
      setError("");
    }
  }, [open]);

  // Load availability when selected doctor changes
  useEffect(() => {
    if (selectedDoctorId) {
      const loadAvail = async () => {
        try {
          setLoadingAvail(true);
          const data = await getAvailability(Number(selectedDoctorId));
          setAvailabilities(data);
        } catch {
          setError("Failed to load doctor availability.");
        } finally {
          setLoadingAvail(false);
        }
      };
      void loadAvail();
    } else {
      setAvailabilities([]);
    }
  }, [selectedDoctorId]);

  const handleSave = async () => {
    if (!patient || !selectedDoctorId || !date || !time) {
      setError("Please select a doctor, date, and time.");
      return;
    }

    try {
      setSaving(true);
      setError("");
      await createAppointment({
        patientId: patient.id,
        doctorId: Number(selectedDoctorId),
        appointmentDate: date,
        appointmentTime: time,
        reason,
      });
      onSuccess();
    } catch (err: any) {
      const msg = err.response?.data?.error || "Unable to assign doctor/create appointment. Please check the values.";
      setError(msg);
    } finally {
      setSaving(false);
    }
  };

  return (
    <Dialog open={open} onClose={() => !saving && onClose()} fullWidth maxWidth="sm">
      <DialogTitle sx={{ fontWeight: "bold" }}>
        Assign Doctor to {patient ? `${patient.firstName} ${patient.lastName}` : ""}
      </DialogTitle>
      <DialogContent dividers>
        <Box sx={{ display: "grid", gap: 3, pt: 1 }}>
          {error && <Alert severity="error">{error}</Alert>}

          {loadingDoctors ? (
            <Box sx={{ display: "flex", justifyContent: "center", py: 2 }}>
              <CircularProgress size={24} />
            </Box>
          ) : (
            <TextField
              select
              label="Select Doctor"
              value={selectedDoctorId}
              onChange={(e) => setSelectedDoctorId(e.target.value)}
              fullWidth
            >
              {doctors.map((doc) => (
                <MenuItem key={doc.id} value={doc.id}>
                  {doc.firstName} {doc.lastName} ({doc.specialization})
                </MenuItem>
              ))}
            </TextField>
          )}

          <Box>
            <Typography variant="subtitle2" color="text.secondary" sx={{ mb: 1, fontWeight: "semibold" }}>
              Doctor's Available Hours:
            </Typography>
            {loadingAvail ? (
              <CircularProgress size={16} />
            ) : availabilities.length === 0 ? (
              <Typography variant="body2" sx={{ fontStyle: "italic", color: "text.secondary" }}>
                No availability defined for this doctor.
              </Typography>
            ) : (
              <Box sx={{ display: "flex", flexWrap: "wrap", gap: 1 }}>
                {availabilities.map((av) => (
                  <Chip
                    key={av.id}
                    label={`${av.dayOfWeek}: ${av.startTime} - ${av.endTime}`}
                    color="primary"
                    variant="outlined"
                    size="small"
                  />
                ))}
              </Box>
            )}
          </Box>

          <TextField
            type="date"
            label="Appointment Date"
            slotProps={{ inputLabel: { shrink: true } }}
            value={date}
            onChange={(e) => setDate(e.target.value)}
            fullWidth
          />

          <TextField
            type="time"
            label="Appointment Time"
            slotProps={{ inputLabel: { shrink: true } }}
            value={time}
            onChange={(e) => setTime(e.target.value)}
            fullWidth
          />

          <TextField
            label="Reason"
            value={reason}
            onChange={(e) => setReason(e.target.value)}
            fullWidth
          />
        </Box>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} disabled={saving}>
          Cancel
        </Button>
        <Button onClick={() => void handleSave()} variant="contained" disabled={saving || loadingDoctors}>
          {saving ? <CircularProgress size={20} /> : "Assign Doctor"}
        </Button>
      </DialogActions>
    </Dialog>
  );
}
