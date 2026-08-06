import apiClient from "./apiClient";

export interface DocumentMetadataResponse {
  id: number;
  patientId: number;
  patientName: string;
  patientPhone: string;
  fileType: "PRESCRIPTION" | "INVOICE" | "REPORT";
  documentName: string;
  s3Key: string;
  downloadUrl: string;
  uploadedBy: string;
  timestamp: string;
}

export const uploadDocument = (
  patientId: number,
  fileType: "PRESCRIPTION" | "INVOICE" | "REPORT",
  documentName: string,
  file: File
) => {
  const formData = new FormData();
  formData.append("patientId", String(patientId));
  formData.append("fileType", fileType);
  formData.append("documentName", documentName);
  formData.append("file", file);

  return apiClient
    .post<DocumentMetadataResponse>("/api/documents/upload", formData, {
      headers: { "Content-Type": "multipart/form-data" },
    })
    .then((r) => r.data);
};

export const getPatientDocuments = (fileType?: string) => {
  const params = fileType ? { fileType } : {};
  return apiClient
    .get<DocumentMetadataResponse[]>("/api/documents/patient", { params })
    .then((r) => r.data);
};
