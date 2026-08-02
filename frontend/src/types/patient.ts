export interface Patient {
  id: number;
  patientNumber: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  dateOfBirth: string;
  gender: "MALE" | "FEMALE" | "OTHER";
  address?: string;
  bloodGroup?: string;
  diagnosis?: string;
}

export interface PatientCreateRequest {
  patientNumber: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  dateOfBirth: string;
  gender: "MALE" | "FEMALE" | "OTHER";
  address?: string;
  bloodGroup?: string;
  diagnosis?: string;
}