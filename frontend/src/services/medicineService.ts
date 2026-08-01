import apiClient from "./apiClient";
import type { Medicine, MedicineRequest } from "../types/clinical";
export const getMedicines = () => apiClient.get<Medicine[]>("/api/medicines").then((r) => r.data);
export const createMedicine = (body: MedicineRequest) => apiClient.post<Medicine>("/api/medicines", body).then((r) => r.data);
export const updateMedicine = (id: number, body: MedicineRequest) => apiClient.put<Medicine>(`/api/medicines/${id}`, body).then((r) => r.data);
export const adjustStock = (id: number, operation: "increase" | "reduce", quantity: number) => apiClient.post<Medicine>(`/api/medicines/${id}/stock/${operation}`, undefined, { params: { quantity } }).then((r) => r.data);
