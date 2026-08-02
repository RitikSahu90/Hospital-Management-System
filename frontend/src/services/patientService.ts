import axios from "axios";

import type { Patient, PatientCreateRequest } from "../types/patient";
import { getAuthHeader } from "./authService";

const API_BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080";

const patientApi = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

patientApi.interceptors.request.use((config) => {
  const authHeaders = getAuthHeader();

  config.headers = {
    ...(config.headers ?? {}),
    ...authHeaders,
  } as typeof config.headers;

  return config;
});

export const getPatients = async (): Promise<Patient[]> => {
  const response = await patientApi.get<Patient[]>("/api/patients");
  return response.data;
};

export const createPatient = async (
  patient: PatientCreateRequest
): Promise<Patient> => {
  const response = await patientApi.post<Patient>("/api/patients", patient);
  return response.data;
};

export const updatePatient = async (id: number, patient: PatientCreateRequest): Promise<Patient> => {
  const response = await patientApi.put<Patient>(`/api/patients/${id}`, patient);
  return response.data;
};

export const deletePatient = async (id: number): Promise<void> => {
  await patientApi.delete(`/api/patients/${id}`);
};
