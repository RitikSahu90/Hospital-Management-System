export type DoctorStatus = "ACTIVE" | "ON_LEAVE" | "INACTIVE";
export interface Doctor { id: number; userId: number; departmentId: number; doctorCode: string; firstName: string; lastName: string; licenseNumber: string; specialization: string; phone?: string; yearsExperience: number; consultationFee?: number; status: DoctorStatus; }
export type DoctorRequest = Omit<Doctor, "id">;
export interface Availability { id: number; doctorId: number; dayOfWeek: string; startTime: string; endTime: string; }
export type AvailabilityRequest = Omit<Availability, "id" | "doctorId">;
export type AppointmentStatus = "SCHEDULED" | "CANCELLED" | "COMPLETED";
export interface Appointment { id: number; patientId: number; doctorId: number; appointmentDate: string; appointmentTime: string; status: AppointmentStatus; reason?: string; }
export type AppointmentRequest = Omit<Appointment, "id" | "status">;
export interface PrescriptionItem { id: number; medicineId: number; dosage: string; durationDays: number; quantity: number; }
export interface Prescription { id: number; patientId: number; doctorId: number; medicalRecordId: number; items: PrescriptionItem[]; notes?: string; }
export type PrescriptionRequest = Omit<Prescription, "id" | "items"> & { items: Omit<PrescriptionItem, "id">[] };
export interface Medicine { id: number; supplierId?: number; name: string; manufacturer?: string; unitPrice: number; stockQuantity: number; reorderLevel?: number; expiryDate?: string; }
export type MedicineRequest = Omit<Medicine, "id" | "stockQuantity"> & { supplierId: number; stockQuantity: number; reorderLevel: number; expiryDate: string };
export type BillingStatus = "PENDING" | "PAID" | "PARTIALLY_PAID" | "CANCELLED";
export interface Billing { id: number; patientId: number; appointmentId?: number; consultationFee: number; medicineCharges: number; otherCharges: number; totalAmount: number; paidAmount: number; dueAmount: number; status: BillingStatus; }
export type BillingRequest = Pick<Billing, "patientId" | "appointmentId" | "consultationFee" | "medicineCharges" | "otherCharges">;
