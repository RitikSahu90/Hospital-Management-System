import { useMemo, useState } from "react";
import { Box, Typography } from "@mui/material";

import PatientToolbar from "../components/patients/PatientToolbar";
import PatientTable from "../components/patients/PatientTable";
import AddPatientDialog from "../components/patients/AddPatientDialog";

import { patients as initialPatients } from "../data/patients";
import type { Patient } from "../types/patient";

export default function Patients() {
  const [patients, setPatients] = useState<Patient[]>(initialPatients);

  const [search, setSearch] = useState("");

  const [openAdd, setOpenAdd] = useState(false);

  const filteredPatients = useMemo(() => {
    const keyword = search.toLowerCase();

    return patients.filter((patient) =>
      [
        patient.patientId,
        patient.firstName,
        patient.lastName,
        patient.phone,
        patient.doctor,
        patient.disease,
        patient.status,
      ]
        .join(" ")
        .toLowerCase()
        .includes(keyword)
    );
  }, [patients, search]);

  const handleAddPatient = (patient: Patient) => {
    setPatients((prev) => [...prev, patient]);
  };

  const handleDeletePatient = (patient: Patient) => {
    if (
      window.confirm(
        `Delete ${patient.firstName} ${patient.lastName}?`
      )
    ) {
      setPatients((prev) =>
        prev.filter((p) => p.id !== patient.id)
      );
    }
  };

  const handleEditPatient = (patient: Patient) => {
    alert(
      `Edit feature coming next.\n\nSelected Patient:\n${patient.firstName} ${patient.lastName}`
    );
  };

  return (
    <Box sx={{ p: 3 }}>
      <Typography
        variant="h4"
        sx={{ fontWeight: "bold", mb: 3 }}
      >
        Patients
      </Typography>

      <PatientToolbar
        search={search}
        setSearch={setSearch}
        onAdd={() => setOpenAdd(true)}
      />

      <PatientTable
        patients={filteredPatients}
        onEdit={handleEditPatient}
        onDelete={handleDeletePatient}
      />

      <AddPatientDialog
        open={openAdd}
        onClose={() => setOpenAdd(false)}
        onSave={handleAddPatient}
      />
    </Box>
  );
}