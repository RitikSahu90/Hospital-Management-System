import apiClient from "./apiClient";

export interface DashboardSummary { patientCount: number; doctorCount: number; appointmentCount: number; revenue: number; appointmentsByStatus: Record<string, number>; }
export const getDashboardSummary = () => apiClient.get<DashboardSummary>("/api/dashboard/summary").then((response) => response.data);