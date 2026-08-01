import ApiResourcePage from "../components/common/ApiResourcePage";
import { createDoctor, getDoctors, updateDoctor } from "../services/doctorService";
import { useAuth } from "../contexts/AuthContext";
import type { Doctor, DoctorRequest } from "../types/clinical";

const fields = [{ key: "firstName", label: "First name" }, { key: "lastName", label: "Last name" }, { key: "licenseNumber", label: "License number" }, { key: "specialization", label: "Specialization" }, { key: "phone", label: "Phone" }, { key: "consultationFee", label: "Consultation fee", type: "number" as const }];
export default function Doctors() { const { user } = useAuth(); const canWrite = user?.role === "ADMIN" || user?.role === "RECEPTIONIST"; return <ApiResourcePage<Doctor> title="Doctors" fields={fields} load={getDoctors} canWrite={canWrite} create={(value) => createDoctor(value as DoctorRequest)} update={(id, value) => updateDoctor(id, value as DoctorRequest)} />; }