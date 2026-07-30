# Entity Relationship Diagram

## Core Relationships & ER Flow

The entity relationships within the Hospital Management System map user access control, patient healthcare journeys, clinical documentation, inventory tracking, and system audit trails.

---

## Relationship Pathways

### 1. User & Provider Hierarchy
```
Roles
  │
  ▼ (1 : N)
Users
  │
  ├──────► (1 : 1 / 0..1) Doctors
  │
  └──────► (1 : 1 / 0..1) Patients
```
- **Roles to Users**: One role can be assigned to multiple users (`1 : N`).
- **Users to Doctors**: A user account with the `DOCTOR` role maps to one doctor profile (`1 : 1`).
- **Users to Patients**: A user account with the `PATIENT` role maps to one patient profile (`1 : 0..1`).

---

### 2. Clinical Care Flow (Doctor Centric)
```
Doctors
  │
  ▼ (1 : N)
Appointments
  │
  ▼ (1 : 1)
Medical Records
  │
  ▼ (1 : N)
Prescriptions
  │
  ▼ (1 : N)
Prescription Items
  │
  ▼ (N : 1)
Medicines
```
- **Doctors to Appointments**: A doctor can have multiple scheduled appointments (`1 : N`).
- **Appointments to Medical Records**: Each completed appointment generates a single medical record (`1 : 1`).
- **Medical Records to Prescriptions**: A medical record can contain prescriptions (`1 : N`).
- **Prescriptions to Prescription Items**: A prescription lists individual medicine items (`1 : N`).
- **Prescription Items to Medicines**: Items reference specific catalog medicines (`N : 1`).

---

### 3. Patient Journey & Financial Operations
```
Patients
  │
  ├──────► (1 : N) Appointments
  │
  └──────► (1 : N) Bills
                    │
                    ▼ (1 : N)
                  Payments
```
- **Patients to Appointments**: A patient can book multiple appointments over time (`1 : N`).
- **Patients to Bills**: Charges generated across appointments aggregate into patient bills (`1 : N`).
- **Bills to Payments**: Each bill can have one or more payment transactions (`1 : N`).

---

### 4. Patient Media & Storage
```
Patients
  │
  ▼ (1 : N)
Patient Reports (AWS S3)
```
- **Patients to Reports**: A patient can have multiple diagnostic lab reports or imaging files stored in AWS S3 (`1 : N`).

---

### 5. System Administration & Audit Traces
```
Users
  │
  ├──────► (1 : N) Notifications
  │
  └──────► (1 : N) Audit Logs
```
- **Users to Notifications**: System alerts and messages delivered to specific user accounts (`1 : N`).
- **Users to Audit Logs**: All administrative actions and secure endpoint access events logged against user IDs (`1 : N`).


---

## Visual ER Diagram (Mermaid)

```mermaid
erDiagram
    ROLES ||--o{ USERS : "assigned to"
    USERS ||--o| DOCTORS : "has"
    USERS ||--o| PATIENTS : "has"
    USERS ||--o{ NOTIFICATIONS : "receives"
    USERS ||--o{ AUDIT_LOGS : "generates"

    DEPARTMENTS ||--o{ DOCTORS : "belongs to"
    DOCTORS ||--o{ DOCTOR_AVAILABILITY : "specifies"
    DOCTORS ||--o{ APPOINTMENTS : "conducts"
    
    PATIENTS ||--o{ EMERGENCY_CONTACTS : "has"
    PATIENTS ||--o{ PATIENT_REPORTS : "owns"
    PATIENTS ||--o{ APPOINTMENTS : "books"
    PATIENTS ||--o{ BILLS : "incurred by"

    APPOINTMENTS ||--o| MEDICAL_RECORDS : "generates"
    
    MEDICAL_RECORDS ||--o{ PRESCRIPTIONS : "contains"
    PRESCRIPTIONS ||--o{ PRESCRIPTION_ITEMS : "includes"
    MEDICINES ||--o{ PRESCRIPTION_ITEMS : "prescribed in"
    SUPPLIERS ||--o{ MEDICINES : "supplies"

    BILLS ||--o{ PAYMENTS : "paid via"

    ROLES {
        bigint role_id PK
        string role_name
        string description
    }

    USERS {
        bigint user_id PK
        bigint role_id FK
        string username
        string email
        string password_hash
        string status
    }

    DOCTORS {
        bigint doctor_id PK
        bigint user_id FK
        bigint department_id FK
        string license_number
        string specialization
        decimal consultation_fee
    }

    PATIENTS {
        bigint patient_id PK
        bigint user_id FK
        string patient_number
        string first_name
        string last_name
        date dob
        string gender
    }

    DEPARTMENTS {
        bigint department_id PK
        string department_name
        string location
    }

    DOCTOR_AVAILABILITY {
        bigint availability_id PK
        bigint doctor_id FK
        string day_of_week
        time start_time
        time end_time
    }

    EMERGENCY_CONTACTS {
        bigint contact_id PK
        bigint patient_id FK
        string name
        string relationship
        string phone
    }

    APPOINTMENTS {
        bigint appointment_id PK
        bigint patient_id FK
        bigint doctor_id FK
        date appointment_date
        time appointment_time
        string status
    }

    MEDICAL_RECORDS {
        bigint record_id PK
        bigint appointment_id FK
        bigint patient_id FK
        bigint doctor_id FK
        string diagnosis
        string treatment
    }

    PRESCRIPTIONS {
        bigint prescription_id PK
        bigint record_id FK
        string notes
    }

    PRESCRIPTION_ITEMS {
        bigint item_id PK
        bigint prescription_id FK
        bigint medicine_id FK
        string dosage
        string duration
        int quantity
    }

    MEDICINES {
        bigint medicine_id PK
        bigint supplier_id FK
        string name
        decimal unit_price
        date expiry_date
    }

    SUPPLIERS {
        bigint supplier_id PK
        string supplier_name
        string phone
        string email
    }

    BILLS {
        bigint bill_id PK
        bigint patient_id FK
        bigint appointment_id FK
        decimal consultation_fee
        decimal medicine_fee
        decimal total_amount
        string status
    }

    PAYMENTS {
        bigint payment_id PK
        bigint bill_id FK
        string payment_method
        decimal amount
        datetime payment_date
    }

    PATIENT_REPORTS {
        bigint report_id PK
        bigint patient_id FK
        string report_name
        string report_url
        datetime uploaded_at
    }

    NOTIFICATIONS {
        bigint notification_id PK
        bigint user_id FK
        string title
        string message
        boolean is_read
    }

    AUDIT_LOGS {
        bigint audit_id PK
        bigint user_id FK
        string action
        string module
        datetime timestamp
    }
```