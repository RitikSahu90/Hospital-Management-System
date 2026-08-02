import ApiResourcePage from "../components/common/ApiResourcePage";
import { createMedicine, deleteMedicine, getMedicines, updateMedicine } from "../services/medicineService";
import { useAuth } from "../contexts/AuthContext";
import type { Medicine, MedicineRequest } from "../types/clinical";

const fields = [{ key: "supplierId", label: "Supplier ID", type: "number" as const }, { key: "name", label: "Medicine" }, { key: "manufacturer", label: "Manufacturer" }, { key: "unitPrice", label: "Unit price", type: "number" as const }, { key: "stockQuantity", label: "Stock quantity", type: "number" as const }, { key: "reorderLevel", label: "Reorder level", type: "number" as const }, { key: "expiryDate", label: "Expiry date", type: "date" as const }];
export default function Pharmacy() { const { user } = useAuth(); const canWrite = user?.role === "ADMIN" || user?.role === "PHARMACIST"; return <ApiResourcePage<Medicine> title="Medicines" fields={fields} load={getMedicines} canWrite={canWrite} canDelete={user?.role === "ADMIN"} create={(value) => createMedicine(value as MedicineRequest)} update={(id, value) => updateMedicine(id, value as MedicineRequest)} remove={deleteMedicine} />; }
