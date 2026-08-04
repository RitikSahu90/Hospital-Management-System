-- =====================================================================
-- 21_emergency.sql
-- Hospital Management System — Clinical: Emergency Cases
-- =====================================================================
-- Purpose:
--   Records emergency department arrivals. patient_id is nullable
--   for unidentified / walk-in patients. Triage level follows the
--   Standard Emergency Triage Assessment and Treatment (START) scale.
-- =====================================================================

USE hospital_management;

CREATE TABLE emergency_cases (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id          BIGINT NULL,           -- NULL for unidentified walk-ins
    triage_level        ENUM('P1','P2','P3','P4')
                            NOT NULL DEFAULT 'P3',
    -- P1=Immediate, P2=Urgent, P3=Less Urgent, P4=Non-Urgent
    chief_complaint     VARCHAR(500) NOT NULL,
    arrival_time        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    assigned_doctor_id  BIGINT NULL,
    status              ENUM('WAITING','ACTIVE','ADMITTED','DISCHARGED','TRANSFERRED','DECEASED')
                            NOT NULL DEFAULT 'WAITING',
    resolution_notes    TEXT NULL,
    resolved_at         TIMESTAMP NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_emergency_patient
        FOREIGN KEY (patient_id) REFERENCES patients(id)
        ON DELETE SET NULL ON UPDATE CASCADE,

    CONSTRAINT fk_emergency_doctor
        FOREIGN KEY (assigned_doctor_id) REFERENCES doctors(id)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_emergency_patient_id      ON emergency_cases(patient_id);
CREATE INDEX idx_emergency_triage_level    ON emergency_cases(triage_level);
CREATE INDEX idx_emergency_status          ON emergency_cases(status);
CREATE INDEX idx_emergency_arrival_time    ON emergency_cases(arrival_time);
CREATE INDEX idx_emergency_doctor_id       ON emergency_cases(assigned_doctor_id);
