import { useEffect, useMemo, useState } from "react";
import { Alert, Box, CircularProgress, Typography } from "@mui/material";

import PatientToolbar from "../components/patients/PatientToolbar";
import PatientTable from "../components/patients/PatientTable";
import AddPatientDialog from "../components/patients/AddPatientDialog";
import EditPatientDialog from "../components/patients/EditPatientDialog";
import DeletePatientDialog from "../components/patients/DeletePatientDialog";

import { createPatient, deletePatient, getPatients, updatePatient } from "../services/patientService";
import type { Patient, PatientCreateRequest } from "../types/patient";

export default function Patients() {
  const [patients, setPatients] = useState<Patient[]>([]);
  const [search, setSearch] = useState("");
  const [openAdd, setOpenAdd] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [editingPatient, setEditingPatient] = useState<Patient | null>(null);
  const [deletingPatient, setDeletingPatient] = useState<Patient | null>(null);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    const loadPatients = async () => {
      try {
        setLoading(true);
        setError(null);
        const data = await getPatients();
        setPatients(data);
      } catch (err) {
        console.error(err);
        setError("Unable to load patients from the backend.");
      } finally {
        setLoading(false);
      }
    };

    loadPatients();
  }, []);

  const filteredPatients = useMemo(() => {
    const keyword = search.toLowerCase();

    return patients.filter((patient) =>
      [patient.firstName, patient.lastName, patient.phone, patient.email, patient.diagnosis]
        .join(" ")
        .toLowerCase()
        .includes(keyword)
    );
  }, [patients, search]);

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
    try { setSaving(true); setError(null); const updated = await updatePatient(editingPatient.id, patient); setPatients((current) => current.map((item) => item.id === updated.id ? updated : item)); setEditingPatient(null); }
    catch (err) { console.error(err); setError("Unable to update patient. Please check the form values."); }
    finally { setSaving(false); }
  };

  const handleDeletePatient = async () => {
    if (!deletingPatient) return;
    try { setSaving(true); setError(null); await deletePatient(deletingPatient.id); setPatients((current) => current.filter((item) => item.id !== deletingPatient.id)); setDeletingPatient(null); }
    catch (err) { console.error(err); setError("Unable to delete patient."); }
    finally { setSaving(false); }
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

      <PatientToolbar search={search} setSearch={setSearch} onAdd={() => setOpenAdd(true)} />

      {loading ? (
        <Box sx={{ display: "flex", justifyContent: "center", py: 8 }}>
          <CircularProgress />
        </Box>
      ) : (
        <PatientTable patients={filteredPatients} onEdit={setEditingPatient} onDelete={setDeletingPatient} />
      )}

      <AddPatientDialog open={openAdd} onClose={() => setOpenAdd(false)} onSave={handleAddPatient} />
      <EditPatientDialog open={Boolean(editingPatient)} patient={editingPatient} saving={saving} onClose={() => setEditingPatient(null)} onSave={handleEditPatient} />
      <DeletePatientDialog open={Boolean(deletingPatient)} patient={deletingPatient} deleting={saving} onClose={() => setDeletingPatient(null)} onConfirm={() => void handleDeletePatient()} />
    </Box>
  );
}
