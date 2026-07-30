-- =====================================================================
-- 08_patient_reports.sql
-- Hospital Management System — Patient: Patient Reports
-- =====================================================================
-- Purpose:
--   Metadata for uploaded patient reports (lab results, scans, etc.).
--   The actual file lives in AWS S3 — only the resulting URL is
--   stored here, never the binary file itself.
-- =====================================================================

USE hospital_management;

CREATE TABLE patient_reports (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id      BIGINT NOT NULL,
    title           VARCHAR(200) NOT NULL,
    report_url      VARCHAR(500) NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_patient_reports_patient
        FOREIGN KEY (patient_id) REFERENCES patients(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_patient_reports_patient_id ON patient_reports(patient_id);
