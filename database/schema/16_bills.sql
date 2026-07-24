-- =====================================================================
-- 16_bills.sql
-- Hospital Management System — Billing: Bills
-- =====================================================================
-- Purpose:
--   One bill per patient visit. total_amount is a generated column so
--   it can never drift out of sync with its components.
-- =====================================================================

USE hospital_management;

CREATE TABLE bills (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id          BIGINT NOT NULL,
    appointment_id      BIGINT NULL,
    consultation_fee    DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    medicine_charges    DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    other_charges       DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total_amount        DECIMAL(10,2) GENERATED ALWAYS AS
                            (consultation_fee + medicine_charges + other_charges) STORED,
    status              ENUM('PENDING','PAID','PARTIALLY_PAID','CANCELLED')
                            NOT NULL DEFAULT 'PENDING',
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_bills_patient
        FOREIGN KEY (patient_id) REFERENCES patients(id)
        ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_bills_appointment
        FOREIGN KEY (appointment_id) REFERENCES appointments(id)
        ON DELETE SET NULL ON UPDATE CASCADE,

    CONSTRAINT chk_bills_fee       CHECK (consultation_fee >= 0),
    CONSTRAINT chk_bills_medicine  CHECK (medicine_charges >= 0),
    CONSTRAINT chk_bills_other     CHECK (other_charges >= 0)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_bills_patient_id ON bills(patient_id);
CREATE INDEX idx_bills_status     ON bills(status);
