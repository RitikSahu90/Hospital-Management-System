import apiClient from "./apiClient";
import type { Billing, BillingRequest } from "../types/clinical";

export const getBillings = () => apiClient.get<Billing[]>("/api/billings").then((r) => r.data);
export const createBilling = (body: BillingRequest) => apiClient.post<Billing>("/api/billings", body).then((r) => r.data);
export const updateBilling = (id: number, body: BillingRequest) => apiClient.put<Billing>(`/api/billings/${id}`, body).then((r) => r.data);
export const deleteBilling = (id: number) => apiClient.delete(`/api/billings/${id}`);
