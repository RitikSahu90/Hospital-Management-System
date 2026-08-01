import ApiResourcePage from "../components/common/ApiResourcePage";
import { createBilling, deleteBilling, getBillings, updateBilling } from "../services/billingService";
import { useAuth } from "../contexts/AuthContext";
import type { Billing, BillingRequest } from "../types/clinical";

const fields = [{ key: "patientId", label: "Patient ID", type: "number" as const }, { key: "appointmentId", label: "Appointment ID", type: "number" as const }, { key: "consultationFee", label: "Consultation fee", type: "number" as const }, { key: "medicineCharges", label: "Medicine charges", type: "number" as const }, { key: "otherCharges", label: "Other charges", type: "number" as const }];
export default function Billing() { const { user } = useAuth(); const canWrite = user?.role === "ADMIN" || user?.role === "PHARMACIST"; return <ApiResourcePage<Billing> title="Billings" fields={fields} load={getBillings} canWrite={canWrite} canDelete={user?.role === "ADMIN"} create={(value) => createBilling(value as BillingRequest)} update={(id, value) => updateBilling(id, value as BillingRequest)} remove={deleteBilling} />; }
