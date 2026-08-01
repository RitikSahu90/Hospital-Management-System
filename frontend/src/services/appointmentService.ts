import apiClient from "./apiClient";
import type { Appointment, AppointmentRequest, AppointmentStatus } from "../types/clinical";
export const getAppointments = () => apiClient.get<Appointment[]>("/api/appointments").then((r) => r.data);
export const createAppointment = (body: AppointmentRequest) => apiClient.post<Appointment>("/api/appointments", body).then((r) => r.data);
export const cancelAppointment = (id: number) => apiClient.put<Appointment>(`/api/appointments/${id}/cancel`).then((r) => r.data);
export const updateAppointmentStatus = (id: number, status: AppointmentStatus) => apiClient.put<Appointment>(`/api/appointments/${id}/status`, { status }).then((r) => r.data);
