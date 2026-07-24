-- =====================================================================
-- seed.sql — Master seed data loader
-- =====================================================================
-- Loads sample data in dependency order. Run schema.sql first.
-- Usage:
--   mysql -u <user> -p < database/seed.sql
-- (Run from the database/ directory, or adjust the SOURCE paths below.)
-- =====================================================================
-- ==========================================================
-- Hospital Management System
-- Seed Data
-- ==========================================================

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
-- Pharmacy
-- ==========================================================

SOURCE seed/suppliers.sql;
SOURCE seed/medicines.sql;
SOURCE seed/inventory.sql;

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

-- ==========================================================
-- Seed Data Loaded Successfully
-- ==========================================================
-- SOURCE seed/roles.sql;
-- SOURCE seed/departments.sql;
-- SOURCE seed/users.sql;
-- SOURCE seed/doctors.sql;
-- SOURCE seed/patients.sql;
-- SOURCE seed/medicines.sql;
-- SOURCE seed/appointments.sql;
-- SOURCE seed/bills.sql;
