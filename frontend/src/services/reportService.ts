import apiClient from "./apiClient";

export interface PatientReport {
  id: number;
  patientId: number;
  title: string;
  reportUrl: string;
  createdAt: string;
  updatedAt: string;
}

export const getPatientReports = (patientId: number) =>
  apiClient.get<PatientReport[]>(`/api/patients/${patientId}/reports`).then((response) => response.data);

export const uploadPatientReport = (patientId: number, title: string, file: File) => {
  const formData = new FormData();
  formData.append("title", title);
  formData.append("file", file);
  return apiClient.post<PatientReport>(`/api/patients/${patientId}/reports`, formData, {
    headers: { "Content-Type": "multipart/form-data" },
  }).then((response) => response.data);
};