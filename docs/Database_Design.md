# Database Design

## Overview

The Database Design for the Hospital Management System ensures data integrity, high query performance, and strict transactional safety. It models core entities across clinical, administrative, inventory, and audit sub-domains.

---

## Database Management System

- **Database Engine**: MySQL 8.0+
- **Storage Engine**: InnoDB (for ACID compliance and foreign key enforcement)
- **Character Set**: `utf8mb4`
- **Collation**: `utf8mb4_unicode_ci`

---

## Database Concepts & Best Practices

- **Relational Database Model**: Strict relational schema structuring entities with explicit primary-foreign key relationships.
- **Normalization (3NF)**: Structured up to Third Normal Form (3NF) to eliminate data redundancy, update anomalies, and structural duplication.
- **Primary Keys (PK)**: Auto-incremented surrogate keys (`BIGINT AUTO_INCREMENT` or `UUID`) assigned to every table for unique record identification.
- **Foreign Keys (FK)**: Enforced referential integrity constraints across domain boundaries using `ON DELETE CASCADE` or `ON DELETE RESTRICT`.
- **Indexes**: Strategic B-Tree indexes on frequently queried foreign keys, search fields (e.g., `email`, `phone`), and composite indexes for range queries (`appointment_date`, `created_at`).
- **Constraints**: Constraints including `NOT NULL`, `UNIQUE`, `CHECK` (for valid ranges or statuses), and default timestamps.
- **Generated Columns**: Computed virtual/stored columns for dynamic price calculations, full-name concatenations, or status derived logic.
- **AWS S3 Object URL Storage**: Offloads large binary blobs (e.g., PDF lab reports, medical scans) to AWS S3, storing only secure URL references in the database.

---

## Database Schema Structure

### 1. Authentication & Identity
- **`roles`**: Stores defined user authority levels (e.g., `ROLE_ADMIN`, `ROLE_DOCTOR`, `ROLE_PATIENT`, `ROLE_PHARMACIST`).
- **`users`**: Central account table handling system credentials, profile metadata, status, and authentication logs.

### 2. Hospital Core
- **`departments`**: Medical departments and operational divisions (e.g., Cardiology, Neurology).
- **`doctors`**: Detailed clinical credentials, qualifications, consultation fees, and department links.
- **`doctor_availability`**: Weekly dynamic scheduling matrix mapping working days and time slots per doctor.
- **`patients`**: Comprehensive patient demographic details, medical identifiers, and contact information.
- **`emergency_contacts`**: Primary and secondary emergency contact details associated with patient profiles.
- **`patient_reports`**: Metadata for uploaded diagnostic files storing S3 URL references.

### 3. Appointments
- **`appointments`**: Central scheduling system tracking patient-doctor visit statuses, slots, and clinical reasons.

### 4. Medical Records
- **`medical_records`**: Diagnostic histories, treatment notes, and follow-up directives tied directly to visits.
- **`prescriptions`**: Master prescription headers generated during patient consultations.
- **`prescription_items`**: Line-item details specifying prescribed medicine IDs, dosages, frequencies, and durations.

### 5. Pharmacy & Inventory
- **`suppliers`**: Pharmaceutical vendor profiles, contact information, and supply contracts.
- **`medicines`**: Master inventory list including brand names, chemical compositions, unit prices, and expiry dates.
- **`inventory`**: Real-time stock levels, warehouse batch locations, reorder thresholds, and unit quantities.

### 6. Billing & Financials
- **`bills`**: Itemized financial statements compiling consultation fees, pharmacy charges, and service costs.
- **`payments`**: Transaction records capturing payment methods, gateway references, amounts, and dates.

### 7. System & Governance
- **`notifications`**: System-generated alerts and push notifications delivered to user accounts.
- **`audit_logs`**: Immutable security audit trail tracking user actions, API modules, IP addresses, and timestamps.
