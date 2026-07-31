export interface Patient {
  id: number;
  patientId: string;
  firstName: string;
  lastName: string;
  age: number;
  gender: "Male" | "Female" | "Other";
  bloodGroup: string;
  phone: string;
  email: string;
  address: string;
  doctor: string;
  disease: string;
  status: "Admitted" | "Discharged" | "Under Treatment";
}