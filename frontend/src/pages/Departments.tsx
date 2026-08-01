import ApiResourcePage from "../components/common/ApiResourcePage";
import { createDepartment, deleteDepartment, getDepartments, updateDepartment } from "../services/departmentService";
import { useAuth } from "../contexts/AuthContext";
import type { Department, DepartmentRequest } from "../services/departmentService";

const fields = [{ key: "name", label: "Name" }, { key: "code", label: "Code" }, { key: "description", label: "Description" }, { key: "status", label: "Status" }];
export default function Departments() {
  const { user } = useAuth();
  const canWrite = user?.role === "ADMIN" || user?.role === "RECEPTIONIST";
  return <ApiResourcePage<Department> title="Departments" fields={fields} load={getDepartments} canWrite={canWrite} canDelete={user?.role === "ADMIN"} create={(value) => createDepartment(value as DepartmentRequest)} update={(id, value) => updateDepartment(id, value as DepartmentRequest)} remove={deleteDepartment} />;
}