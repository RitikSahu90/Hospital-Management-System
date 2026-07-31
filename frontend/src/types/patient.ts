export interface Patient {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  diagnosis: string;
}

export interface PatientCreateRequest {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  diagnosis: string;
}