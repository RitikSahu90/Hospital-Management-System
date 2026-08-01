import ApiResourcePage from "../components/common/ApiResourcePage";
import { createSupplier, deleteSupplier, getSuppliers, updateSupplier } from "../services/supplierService";
import { useAuth } from "../contexts/AuthContext";
import type { Supplier, SupplierRequest } from "../services/supplierService";

const fields = [{ key: "name", label: "Name" }, { key: "contactPerson", label: "Contact person" }, { key: "phone", label: "Phone" }, { key: "email", label: "Email" }, { key: "address", label: "Address" }];
export default function Suppliers() {
  const { user } = useAuth();
  const canWrite = user?.role === "ADMIN" || user?.role === "PHARMACIST";
  return <ApiResourcePage<Supplier> title="Suppliers" fields={fields} load={getSuppliers} canWrite={canWrite} canDelete={user?.role === "ADMIN"} create={(value) => createSupplier(value as SupplierRequest)} update={(id, value) => updateSupplier(id, value as SupplierRequest)} remove={deleteSupplier} />;
}