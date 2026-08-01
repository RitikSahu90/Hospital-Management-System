import apiClient from "./apiClient";

export interface Supplier { id: number; name: string; contactPerson?: string; phone: string; email?: string; address?: string; }
export type SupplierRequest = Omit<Supplier, "id">;
export const getSuppliers = () => apiClient.get<Supplier[]>("/api/suppliers").then((response) => response.data);
export const createSupplier = (body: SupplierRequest) => apiClient.post<Supplier>("/api/suppliers", body).then((response) => response.data);
export const updateSupplier = (id: number, body: SupplierRequest) => apiClient.put<Supplier>(`/api/suppliers/${id}`, body).then((response) => response.data);
export const deleteSupplier = (id: number) => apiClient.delete(`/api/suppliers/${id}`);