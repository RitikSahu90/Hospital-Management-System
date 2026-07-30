-- =====================================================================
-- 11_prescriptions.sql
-- Hospital Management System — Medical: Prescriptions
-- =====================================================================
-- Purpose:
--   A prescription issued as part of a medical record. doctor_id and
--   patient_id are denormalized copies (deliberately, see note below)
--   to make prescription lookups fast without joining through
--   medical_records -> appointments every time.
-- =====================================================================

USE hospital_management;

CREATE TABLE prescriptions (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    medical_record_id   BIGINT NOT NULL,
    doctor_id           BIGINT NOT NULL,
    patient_id          BIGINT NOT NULL,
    notes               TEXT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_prescriptions_medical_record
        FOREIGN KEY (medical_record_id) REFERENCES medical_records(id)
        ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_prescriptions_doctor
        FOREIGN KEY (doctor_id) REFERENCES doctors(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT fk_prescriptions_patient
        FOREIGN KEY (patient_id) REFERENCES patients(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_prescriptions_doctor_id  ON prescriptions(doctor_id);
CREATE INDEX idx_prescriptions_patient_id ON prescriptions(patient_id);
