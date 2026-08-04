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
    headers: { "Content-Type": undefined },
  }).then((response) => response.data);
};

export const getPatientReportDownloadUrl = (patientId: number, reportId: number) =>
  apiClient.get<{ url: string }>(`/api/patients/${patientId}/reports/report/${reportId}/download`)
    .then((response) => response.data.url);
