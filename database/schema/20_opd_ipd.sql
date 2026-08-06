-- =====================================================================
-- 20_opd_ipd.sql
-- Hospital Management System — Clinical: OPD / IPD Visits
-- =====================================================================
-- Purpose:
--   Tracks outpatient (OPD) and inpatient (IPD) visits.
--   IPD visits carry ward/bed assignment and admission/discharge dates.
--   OPD visits are lightweight same-day consultations.
-- =====================================================================

USE hospital_management;

CREATE TABLE opd_ipd_visits (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id          BIGINT NOT NULL,
    doctor_id           BIGINT NOT NULL,
    appointment_id      BIGINT NULL,           -- optional link to appointment
    visit_type          ENUM('OPD','IPD') NOT NULL DEFAULT 'OPD',
    chief_complaint     VARCHAR(500) NOT NULL,
    ward                VARCHAR(100) NULL,      -- IPD only
    bed_number          VARCHAR(20)  NULL,      -- IPD only
    admission_date      DATE NULL,              -- IPD only
    discharge_date      DATE NULL,              -- IPD only
    status              ENUM('ACTIVE','DISCHARGED','TRANSFERRED','CLOSED')
                            NOT NULL DEFAULT 'ACTIVE',
    notes               TEXT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_opd_ipd_patient
        FOREIGN KEY (patient_id) REFERENCES patients(id)
        ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_opd_ipd_doctor
        FOREIGN KEY (doctor_id) REFERENCES doctors(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT fk_opd_ipd_appointment
        FOREIGN KEY (appointment_id) REFERENCES appointments(id)
        ON DELETE SET NULL ON UPDATE CASCADE,

    CONSTRAINT chk_opd_ipd_discharge
        CHECK (discharge_date IS NULL OR discharge_date >= admission_date)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_opd_ipd_patient_id    ON opd_ipd_visits(patient_id);
CREATE INDEX idx_opd_ipd_doctor_id     ON opd_ipd_visits(doctor_id);
CREATE INDEX idx_opd_ipd_visit_type    ON opd_ipd_visits(visit_type);
CREATE INDEX idx_opd_ipd_status        ON opd_ipd_visits(status);
CREATE INDEX idx_opd_ipd_admission     ON opd_ipd_visits(admission_date);
