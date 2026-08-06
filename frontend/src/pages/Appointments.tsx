import ApiResourcePage from "../components/common/ApiResourcePage";
import { createAppointment, getAppointments } from "../services/appointmentService";
import { useAuth } from "../contexts/AuthContext";
import type { Appointment, AppointmentRequest } from "../types/clinical";

const fields = [{ key: "patientId", label: "Patient ID", type: "number" as const }, { key: "doctorId", label: "Doctor ID", type: "number" as const }, { key: "appointmentDate", label: "Date", type: "date" as const }, { key: "appointmentTime", label: "Time", type: "time" as const }, { key: "reason", label: "Reason" }];
export default function Appointments() { const { user } = useAuth(); const canWrite = Boolean(user && ["ADMIN", "DOCTOR", "RECEPTIONIST"].includes(user.role)); return <ApiResourcePage<Appointment> title="Appointments" fields={fields} load={getAppointments} canWrite={canWrite} create={(value) => createAppointment(value as AppointmentRequest)} />; }
