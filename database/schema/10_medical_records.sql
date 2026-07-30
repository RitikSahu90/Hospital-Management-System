-- =====================================================================
-- 10_medical_records.sql
-- Hospital Management System — Medical: Medical Records
-- =====================================================================
-- Purpose:
--   One medical record per appointment (1:1) — the doctor's diagnosis
--   and clinical notes for that visit. Prescriptions hang off this.
-- =====================================================================

USE hospital_management;

CREATE TABLE medical_records (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id      BIGINT NOT NULL,
    diagnosis           VARCHAR(500) NOT NULL,
    clinical_notes      TEXT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_medical_records_appointment_id UNIQUE (appointment_id),

    CONSTRAINT fk_medical_records_appointment
        FOREIGN KEY (appointment_id) REFERENCES appointments(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;
