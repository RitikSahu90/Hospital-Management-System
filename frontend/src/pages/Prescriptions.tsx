import ApiResourcePage from "../components/common/ApiResourcePage";
import { createPrescription, deletePrescription, getPrescriptions, updatePrescription } from "../services/prescriptionService";
import { useAuth } from "../contexts/AuthContext";
import type { Prescription, PrescriptionRequest } from "../types/clinical";

const fields = [{ key: "patientId", label: "Patient ID", type: "number" as const }, { key: "doctorId", label: "Doctor ID", type: "number" as const }, { key: "medicalRecordId", label: "Medical record ID", type: "number" as const }, { key: "items", label: "Items (JSON)" }, { key: "notes", label: "Notes" }];
const toRequest = (value: Record<string, string | number>): PrescriptionRequest => ({ patientId: Number(value.patientId), doctorId: Number(value.doctorId), medicalRecordId: Number(value.medicalRecordId), items: JSON.parse(String(value.items)) as PrescriptionRequest["items"], notes: String(value.notes ?? "") });
export default function Prescriptions() { const { user } = useAuth(); const canWrite = Boolean(user && ["ADMIN", "DOCTOR", "PHARMACIST"].includes(user.role)); return <ApiResourcePage<Prescription> title="Prescriptions" fields={fields} load={getPrescriptions} canWrite={canWrite} canDelete={Boolean(user && ["ADMIN", "DOCTOR"].includes(user.role))} create={(value) => createPrescription(toRequest(value))} update={(id, value) => updatePrescription(id, toRequest(value))} remove={deletePrescription} />; }
