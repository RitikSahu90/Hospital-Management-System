import { useEffect, useMemo, useState } from "react";
import { Alert, Box, CircularProgress, Typography } from "@mui/material";

import PatientToolbar from "../components/patients/PatientToolbar";
import PatientTable from "../components/patients/PatientTable";
import AddPatientDialog from "../components/patients/AddPatientDialog";

import { createPatient, getPatients } from "../services/patientService";
import type { Patient, PatientCreateRequest } from "../types/patient";

export default function Patients() {
  const [patients, setPatients] = useState<Patient[]>([]);
  const [search, setSearch] = useState("");
  const [openAdd, setOpenAdd] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

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
        <PatientTable patients={filteredPatients} />
      )}

      <AddPatientDialog open={openAdd} onClose={() => setOpenAdd(false)} onSave={handleAddPatient} />
    </Box>
  );
}