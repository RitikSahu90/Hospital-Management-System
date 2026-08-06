import apiClient from "./apiClient";
import type { Prescription, PrescriptionRequest } from "../types/clinical";
export const getPrescriptions = () => apiClient.get<Prescription[]>("/api/prescriptions").then((r) => r.data);
export const createPrescription = (body: PrescriptionRequest) => apiClient.post<Prescription>("/api/prescriptions", body).then((r) => r.data);
export const updatePrescription = (id: number, body: PrescriptionRequest) => apiClient.put<Prescription>(`/api/prescriptions/${id}`, body).then((r) => r.data);
export const deletePrescription = (id: number) => apiClient.delete(`/api/prescriptions/${id}`);

export const uploadPrescriptionPdf = (id: number, file: File) => {
  const formData = new FormData();
  formData.append("file", file);
  return apiClient.post<Prescription>(`/api/prescriptions/${id}/upload-pdf`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  }).then((r) => r.data);
};

export const getPrescriptionDownloadUrl = (id: number) =>
  apiClient.get<{ url: string }>(`/api/prescriptions/${id}/download`).then((r) => r.data.url);
