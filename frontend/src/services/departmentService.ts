import apiClient from "./apiClient";

export interface Department { id: number; name: string; code: string; description?: string; status: "ACTIVE" | "INACTIVE"; }
export type DepartmentRequest = Omit<Department, "id">;
export const getDepartments = () => apiClient.get<Department[]>("/api/departments").then((response) => response.data);
export const createDepartment = (body: DepartmentRequest) => apiClient.post<Department>("/api/departments", body).then((response) => response.data);
export const updateDepartment = (id: number, body: DepartmentRequest) => apiClient.put<Department>(`/api/departments/${id}`, body).then((response) => response.data);
export const deleteDepartment = (id: number) => apiClient.delete(`/api/departments/${id}`);