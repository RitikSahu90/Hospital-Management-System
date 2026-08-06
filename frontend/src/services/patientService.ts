import type { Patient, PatientCreateRequest } from "../types/patient";
import apiClient from "./apiClient";

export const getPatients = async (): Promise<Patient[]> => {
  const response = await apiClient.get<Patient[]>("/api/patients");
  return response.data;
};

export const createPatient = async (
  patient: PatientCreateRequest
): Promise<Patient> => {
  const response = await apiClient.post<Patient>("/api/patients", patient);
  return response.data;
};

export const updatePatient = async (id: number, patient: PatientCreateRequest): Promise<Patient> => {
  const response = await apiClient.put<Patient>(`/api/patients/${id}`, patient);
  return response.data;
};

export const deletePatient = async (id: number): Promise<void> => {
  await apiClient.delete(`/api/patients/${id}`);
};
