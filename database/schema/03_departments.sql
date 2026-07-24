-- =====================================================================
-- 03_departments.sql
-- Hospital Management System — Hospital: Departments
-- =====================================================================
-- Purpose:
--   Hospital departments (Cardiology, Orthopedics, etc.). Every doctor
--   belongs to exactly one department.
-- =====================================================================

USE hospital_management;

CREATE TABLE departments (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(150) NOT NULL,
    code            VARCHAR(20)  NOT NULL,
    description     VARCHAR(255) NULL,
    status          ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_departments_name UNIQUE (name),
    CONSTRAINT uq_departments_code UNIQUE (code)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_departments_status ON departments(status);
