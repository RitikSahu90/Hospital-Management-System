USE hospital_management;

-- ==========================================================
-- Authentication
-- ==========================================================
SOURCE seed/roles.sql;
SOURCE seed/users.sql;

-- ==========================================================
-- Hospital
-- ==========================================================
SOURCE seed/departments.sql;
SOURCE seed/doctors.sql;
SOURCE seed/doctor_availability.sql;

-- ==========================================================
-- Patient
-- ==========================================================
SOURCE seed/patients.sql;
SOURCE seed/emergency_contacts.sql;
SOURCE seed/patient_reports.sql;

-- ==========================================================
-- Pharmacy (Must come BEFORE prescription_items)
-- ==========================================================
SOURCE seed/suppliers.sql;
SOURCE seed/medicines.sql;
SOURCE seed/inventory.sql;

-- ==========================================================
-- Appointment
-- ==========================================================
SOURCE seed/appointments.sql;

-- ==========================================================
-- Medical
-- ==========================================================
SOURCE seed/medical_records.sql;
SOURCE seed/prescriptions.sql;
SOURCE seed/prescription_items.sql;

-- ==========================================================
-- Billing
-- ==========================================================
SOURCE seed/bills.sql;
SOURCE seed/payments.sql;

-- ==========================================================
-- System
-- ==========================================================
SOURCE seed/notifications.sql;
SOURCE seed/audit_logs.sql;