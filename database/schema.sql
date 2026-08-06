-- Authentication
SOURCE schema/01_roles.sql;
SOURCE schema/02_users.sql;

-- Hospital
SOURCE schema/03_departments.sql;
SOURCE schema/04_doctors.sql;
SOURCE schema/05_doctor_availability.sql;

-- Patient
SOURCE schema/06_patients.sql;
SOURCE schema/07_emergency_contacts.sql;
SOURCE schema/08_patient_reports.sql;

-- Appointment
SOURCE schema/09_appointments.sql;

-- Medical
SOURCE schema/10_medical_records.sql;

-- Pharmacy
SOURCE schema/13_suppliers.sql;
SOURCE schema/14_medicines.sql;
SOURCE schema/15_inventory.sql;

-- Continue Medical
SOURCE schema/11_prescriptions.sql;
SOURCE schema/12_prescription_items.sql;

-- Billing
SOURCE schema/16_bills.sql;
SOURCE schema/17_payments.sql;

-- System
SOURCE schema/18_notifications.sql;
SOURCE schema/19_audit_logs.sql;

-- ── Epic 1: Patient Lifecycle ──────────────────────────────────────
SOURCE schema/20_opd_ipd.sql;
SOURCE schema/21_emergency.sql;

-- ── Epic 2: Clinical Management ───────────────────────────────────
SOURCE schema/22_operation_theatres.sql;
SOURCE schema/23_lab_radiology.sql;

-- ── Epic 3: Financial / Insurance ─────────────────────────────────
SOURCE schema/24_insurance.sql;

-- ── Epic 5: Analytics & AI Risk Alerts ────────────────────────────
SOURCE schema/25_risk_alerts.sql;