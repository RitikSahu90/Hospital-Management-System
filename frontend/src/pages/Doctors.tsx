import ApiResourcePage from "../components/common/ApiResourcePage";
import { createDoctor, deleteDoctor, getDoctors, updateDoctor } from "../services/doctorService";
import { useAuth } from "../contexts/AuthContext";
import type { Doctor, DoctorRequest } from "../types/clinical";

const fields = [
  { key: "id", label: "Doctor ID", type: "number" as const }, 
  { key: "departmentId", label: "Department ID", type: "number" as const }, 
  { key: "doctorCode", label: "Doctor code" }, 
  { key: "firstName", label: "First name" }, 
  { key: "lastName", label: "Last name" }, 
  { key: "licenseNumber", label: "License number" }, 
  { key: "specialization", label: "Specialization" }, 
  { key: "phone", label: "Phone" }, 
  { key: "yearsExperience", label: "Years experience", type: "number" as const }, 
  { key: "consultationFee", label: "Consultation fee", type: "number" as const }, 
  { key: "status", label: "Status", type: "select" as const, options: [{label: "ACTIVE", value: "ACTIVE"}, {label: "NOT ACTIVE", value: "INACTIVE"}] }
];
export default function Doctors() { const { user } = useAuth(); const canWrite = user?.role === "ADMIN" || user?.role === "RECEPTIONIST"; return <ApiResourcePage<Doctor> title="Doctors" fields={fields} load={getDoctors} canWrite={canWrite} canDelete={user?.role === "ADMIN"} create={(value) => createDoctor(value as DoctorRequest)} update={(id, value) => updateDoctor(id, value as DoctorRequest)} remove={deleteDoctor} />; }