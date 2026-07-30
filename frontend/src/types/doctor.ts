export interface Doctor {
  id: number;
  doctorId: string;
  firstName: string;
  lastName: string;
  specialization: string;
  qualification: string;
  experience: number;
  phone: string;
  email: string;
  department: string;
  status: "Available" | "On Leave" | "Busy";
}