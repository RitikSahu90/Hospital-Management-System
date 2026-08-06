import { useEffect, useMemo, useState } from "react";
import { Alert, Box, CircularProgress, Typography } from "@mui/material";

import PatientToolbar from "../components/patients/PatientToolbar";
import PatientTable from "../components/patients/PatientTable";
import AddPatientDialog from "../components/patients/AddPatientDialog";
import EditPatientDialog from "../components/patients/EditPatientDialog";
import DeletePatientDialog from "../components/patients/DeletePatientDialog";
import AssignDoctorDialog from "../components/patients/AssignDoctorDialog";

import { createPatient, deletePatient, getPatients, updatePatient } from "../services/patientService";
import { getDoctors } from "../services/doctorService";
import { getAppointments } from "../services/appointmentService";
import { useAuth } from "../contexts/AuthContext";
import type { Patient, PatientCreateRequest } from "../types/patient";
import type { Doctor, Appointment } from "../types/clinical";

export default function Patients() {
  const { user } = useAuth();
  const [patients, setPatients] = useState<Patient[]>([]);
  const [doctors, setDoctors] = useState<Doctor[]>([]);
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  
  const [search, setSearch] = useState("");
  const [openAdd, setOpenAdd] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [editingPatient, setEditingPatient] = useState<Patient | null>(null);
  const [deletingPatient, setDeletingPatient] = useState<Patient | null>(null);
  const [assigningPatient, setAssigningPatient] = useState<Patient | null>(null);
  const [saving, setSaving] = useState(false);

  const canEdit = Boolean(user && ["ADMIN", "DOCTOR", "RECEPTIONIST"].includes(user.role));
  const canDelete = Boolean(user && user.role === "ADMIN");

  const loadData = async () => {
    try {
      setLoading(true);
      setError(null);
      const [patientsData, doctorsData, appointmentsData] = await Promise.all([
        getPatients(),
        getDoctors(),
        getAppointments(),
      ]);
      setPatients(patientsData);
      setDoctors(doctorsData);
      setAppointments(appointmentsData);
    } catch (err) {
      console.error(err);
      setError("Unable to load patients, doctors, or appointments from the backend.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void loadData();
  }, []);

  const refreshAppointments = async () => {
    try {
      const appointmentsData = await getAppointments();
      setAppointments(appointmentsData);
    } catch (err) {
      console.error("Failed to refresh appointments:", err);
    }
  };

  const filteredPatients = useMemo(() => {
    const keyword = search.toLowerCase();

    return patients.filter((patient) =>
      [patient.firstName, patient.lastName, patient.phone, patient.email, patient.diagnosis]
        .join(" ")
        .toLowerCase()
        .includes(keyword)
    );
  }, [patients, search]);

  const patientsWithDoctorInfo = useMemo(() => {
    return filteredPatients.map((patient) => {
      const patientApps = appointments.filter((app) => app.patientId === patient.id && app.status !== "CANCELLED");
      
      const latestApp = patientApps.reduce((latest, current) => {
        if (!latest) return current;
        const latestTime = new Date(`${latest.appointmentDate}T${latest.appointmentTime}`).getTime();
        const currentTime = new Date(`${current.appointmentDate}T${current.appointmentTime}`).getTime();
        return currentTime > latestTime ? current : latest;
      }, null as Appointment | null);

      let assignedDoctorName = "";
      let assignDate = "";

      if (latestApp) {
        const doc = doctors.find((d) => d.id === latestApp.doctorId);
        assignedDoctorName = doc ? `${doc.firstName} ${doc.lastName}` : `Doctor ID: ${latestApp.doctorId}`;
        assignDate = latestApp.appointmentDate;
      }

      return {
        ...patient,
        assignedDoctorName,
        assignDate,
      };
    });
  }, [filteredPatients, appointments, doctors]);

  const handleAddPatient = async (patient: PatientCreateRequest) => {
    try {
      const created = await createPatient(patient);
      setPatients((prev) => [created, ...prev]);
      setOpenAdd(false);
    } catch (err) {
      console.error(err);
      setError("Unable to create patient. Please check the form values.");
    }
  };

  const handleEditPatient = async (patient: PatientCreateRequest) => {
    if (!editingPatient) return;
    try {
      setSaving(true);
      setError(null);
      const updated = await updatePatient(editingPatient.id, patient);
      setPatients((current) => current.map((item) => item.id === updated.id ? updated : item));
      setEditingPatient(null);
    } catch (err) {
      console.error(err);
      setError("Unable to update patient. Please check the form values.");
    } finally {
      setSaving(false);
    }
  };

  const handleDeletePatient = async () => {
    if (!deletingPatient) return;
    try {
      setSaving(true);
      setError(null);
      await deletePatient(deletingPatient.id);
      setPatients((current) => current.filter((item) => item.id !== deletingPatient.id));
      setDeletingPatient(null);
    } catch (err) {
      console.error(err);
      setError("Unable to delete patient.");
    } finally {
      setSaving(false);
    }
  };

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h4" sx={{ fontWeight: "bold", mb: 3 }}>
        Patients
      </Typography>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      <PatientToolbar search={search} setSearch={setSearch} onAdd={() => setOpenAdd(true)} showAdd={canEdit} />

      {loading ? (
        <Box sx={{ display: "flex", justifyContent: "center", py: 8 }}>
          <CircularProgress />
        </Box>
      ) : (
        <PatientTable
          patients={patientsWithDoctorInfo}
          onEdit={setEditingPatient}
          onDelete={setDeletingPatient}
          onAssignDoctor={setAssigningPatient}
          canEdit={canEdit}
          canDelete={canDelete}
        />
      )}

      <AddPatientDialog open={openAdd} onClose={() => setOpenAdd(false)} onSave={handleAddPatient} />
      <EditPatientDialog open={Boolean(editingPatient)} patient={editingPatient} saving={saving} onClose={() => setEditingPatient(null)} onSave={handleEditPatient} />
      <DeletePatientDialog open={Boolean(deletingPatient)} patient={deletingPatient} deleting={saving} onClose={() => setDeletingPatient(null)} onConfirm={() => void handleDeletePatient()} />
      <AssignDoctorDialog
        open={Boolean(assigningPatient)}
        patient={assigningPatient}
        onClose={() => setAssigningPatient(null)}
        onSuccess={async () => {
          setAssigningPatient(null);
          await refreshAppointments();
        }}
      />
    </Box>
  );
}
