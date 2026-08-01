import ApiResourcePage from "../components/common/ApiResourcePage";
import { createPrescription, getPrescriptions, updatePrescription } from "../services/prescriptionService";
import { useAuth } from "../contexts/AuthContext";
import type { Prescription, PrescriptionRequest } from "../types/clinical";

const fields = [{ key: "patientId", label: "Patient ID", type: "number" as const }, { key: "doctorId", label: "Doctor ID", type: "number" as const }, { key: "medicineName", label: "Medicine" }, { key: "dosage", label: "Dosage" }, { key: "frequency", label: "Frequency" }, { key: "durationDays", label: "Days", type: "number" as const }, { key: "prescribedDate", label: "Prescribed date", type: "date" as const }, { key: "notes", label: "Notes" }];
export default function Prescriptions() { const { user } = useAuth(); const canWrite = Boolean(user && ["ADMIN", "DOCTOR", "PHARMACIST"].includes(user.role)); return <ApiResourcePage<Prescription> title="Prescriptions" fields={fields} load={getPrescriptions} canWrite={canWrite} create={(value) => createPrescription(value as PrescriptionRequest)} update={(id, value) => updatePrescription(id, value as PrescriptionRequest)} />; }
