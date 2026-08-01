import ApiResourcePage from "../components/common/ApiResourcePage";
import { createMedicalRecord, deleteMedicalRecord, getMedicalRecords, updateMedicalRecord } from "../services/medicalRecordService";
import { useAuth } from "../contexts/AuthContext";
import type { MedicalRecord, MedicalRecordRequest } from "../services/medicalRecordService";

const fields = [{ key: "appointmentId", label: "Appointment ID", type: "number" as const }, { key: "diagnosis", label: "Diagnosis" }, { key: "clinicalNotes", label: "Clinical notes" }];
export default function MedicalRecords() {
  const { user } = useAuth();
  const canWrite = user?.role === "ADMIN" || user?.role === "DOCTOR";
  return <ApiResourcePage<MedicalRecord> title="Medical Records" fields={fields} load={getMedicalRecords} canWrite={canWrite} canDelete={user?.role === "ADMIN"} create={(value) => createMedicalRecord(value as MedicalRecordRequest)} update={(id, value) => updateMedicalRecord(id, value as MedicalRecordRequest)} remove={deleteMedicalRecord} />;
}