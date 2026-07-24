-- =====================================================================
-- 06_patients.sql
-- Hospital Management System — Patient: Patients
-- =====================================================================
-- Purpose:
--   Core patient demographic record. May optionally link to a `users`
--   row (role = PATIENT) for patients who have portal login access;
--   walk-in patients registered by reception may have no login at all.
-- =====================================================================

USE hospital_management;

CREATE TABLE patients (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id             BIGINT NULL,
    patient_number      VARCHAR(20) NOT NULL,
    first_name          VARCHAR(100) NOT NULL,
    last_name           VARCHAR(100) NOT NULL,
    date_of_birth       DATE NOT NULL,
    gender              ENUM('MALE','FEMALE','OTHER') NOT NULL,
    phone               VARCHAR(20)  NOT NULL,
    email               VARCHAR(150) NULL,
    address             VARCHAR(255) NULL,
    blood_group         VARCHAR(5)   NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_patients_user_id        UNIQUE (user_id),
    CONSTRAINT uq_patients_patient_number UNIQUE (patient_number),

    CONSTRAINT fk_patients_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_patients_patient_number ON patients(patient_number);
CREATE INDEX idx_patients_phone          ON patients(phone);
CREATE INDEX idx_patients_name           ON patients(last_name, first_name);
