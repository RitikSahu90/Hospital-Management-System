-- =====================================================================
-- 04_doctors.sql
-- Hospital Management System — Hospital: Doctors
-- =====================================================================
-- Purpose:
--   Extends a `users` row (role = DOCTOR) with doctor-specific data.
--   Each doctor belongs to exactly one department.
-- =====================================================================

USE hospital_management;

CREATE TABLE doctors (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id             BIGINT NOT NULL,
    department_id       BIGINT NOT NULL,
    doctor_code         VARCHAR(20)  NOT NULL,
    license_number      VARCHAR(50)  NOT NULL,
    first_name          VARCHAR(100) NOT NULL,
    last_name           VARCHAR(100) NOT NULL,
    specialization      VARCHAR(150) NOT NULL,
    phone               VARCHAR(20)  NOT NULL,
    years_experience    INT NOT NULL DEFAULT 0,
    consultation_fee    DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    status              ENUM('ACTIVE', 'ON_LEAVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_doctors_user_id        UNIQUE (user_id),
    CONSTRAINT uq_doctors_doctor_code    UNIQUE (doctor_code),
    CONSTRAINT uq_doctors_license_number UNIQUE (license_number),

    CONSTRAINT fk_doctors_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_doctors_department
        FOREIGN KEY (department_id) REFERENCES departments(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT chk_doctors_experience CHECK (years_experience >= 0),
    CONSTRAINT chk_doctors_fee CHECK (consultation_fee >= 0)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_doctors_department_id  ON doctors(department_id);
CREATE INDEX idx_doctors_doctor_code    ON doctors(doctor_code);
CREATE INDEX idx_doctors_license_number ON doctors(license_number);
CREATE INDEX idx_doctors_phone          ON doctors(phone);
CREATE INDEX idx_doctors_status         ON doctors(status);
