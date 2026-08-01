import ApiResourcePage from "../components/common/ApiResourcePage";
import { createBilling, getBillings, updateBilling } from "../services/billingService";
import { useAuth } from "../contexts/AuthContext";
import type { Billing, BillingRequest } from "../types/clinical";

const fields = [{ key: "patientId", label: "Patient ID", type: "number" as const }, { key: "prescriptionId", label: "Prescription ID", type: "number" as const }, { key: "totalAmount", label: "Total amount", type: "number" as const }, { key: "paidAmount", label: "Paid amount", type: "number" as const }, { key: "billingDate", label: "Billing date", type: "date" as const }];
export default function Billing() { const { user } = useAuth(); const canWrite = user?.role === "ADMIN" || user?.role === "PHARMACIST"; return <ApiResourcePage<Billing> title="Billings" fields={fields} load={getBillings} canWrite={canWrite} create={(value) => createBilling(value as BillingRequest)} update={(id, value) => updateBilling(id, value as BillingRequest)} />; }
