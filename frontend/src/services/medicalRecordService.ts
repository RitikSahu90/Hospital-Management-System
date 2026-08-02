import apiClient from "./apiClient";

export interface MedicalRecord { id: number; appointmentId: number; patientId: number; doctorId: number; diagnosis: string; clinicalNotes?: string; createdAt: string; updatedAt: string; }
export type MedicalRecordRequest = Pick<MedicalRecord, "appointmentId" | "diagnosis"> & { clinicalNotes?: string };
export const getMedicalRecords = () => apiClient.get<MedicalRecord[]>("/api/medical-records").then((response) => response.data);
export const createMedicalRecord = (body: MedicalRecordRequest) => apiClient.post<MedicalRecord>("/api/medical-records", body).then((response) => response.data);
export const updateMedicalRecord = (id: number, body: MedicalRecordRequest) => apiClient.put<MedicalRecord>(`/api/medical-records/${id}`, body).then((response) => response.data);
export const deleteMedicalRecord = (id: number) => apiClient.delete(`/api/medical-records/${id}`);