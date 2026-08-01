export interface Doctor { id: number; firstName: string; lastName: string; licenseNumber: string; specialization: string; phone?: string; consultationFee?: number; }
export type DoctorRequest = Omit<Doctor, "id">;
export interface Availability { id: number; doctorId: number; dayOfWeek: string; startTime: string; endTime: string; }
export type AvailabilityRequest = Omit<Availability, "id" | "doctorId">;
export type AppointmentStatus = "SCHEDULED" | "CANCELLED" | "COMPLETED";
export interface Appointment { id: number; patientId: number; doctorId: number; appointmentDate: string; appointmentTime: string; status: AppointmentStatus; reason?: string; }
export type AppointmentRequest = Omit<Appointment, "id" | "status">;
export interface Prescription { id: number; patientId: number; doctorId: number; medicineName: string; dosage: string; frequency: string; durationDays: number; prescribedDate: string; notes?: string; }
export type PrescriptionRequest = Omit<Prescription, "id">;
export interface Medicine { id: number; name: string; manufacturer: string; unitPrice: number; stockQuantity: number; expiryDate: string; }
export type MedicineRequest = Omit<Medicine, "id">;
export interface Billing { id: number; patientId: number; prescriptionId: number; totalAmount: number; paidAmount: number; dueAmount: number; billingDate: string; paid: boolean; }
export type BillingRequest = Pick<Billing, "patientId" | "prescriptionId" | "totalAmount" | "paidAmount" | "billingDate">;
