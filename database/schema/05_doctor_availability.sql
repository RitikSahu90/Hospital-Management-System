-- =====================================================================
-- 05_doctor_availability.sql
-- Hospital Management System — Hospital: Doctor Availability
-- =====================================================================
-- Purpose:
--   Recurring weekly time slots during which a doctor is available
--   for appointments.
-- =====================================================================

USE hospital_management;

CREATE TABLE doctor_availability (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id       BIGINT NOT NULL,
    day_of_week     ENUM('MON','TUE','WED','THU','FRI','SAT','SUN') NOT NULL,
    start_time      TIME NOT NULL,
    end_time        TIME NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_availability_doctor
        FOREIGN KEY (doctor_id) REFERENCES doctors(id)
        ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT chk_availability_time_range CHECK (end_time > start_time),
    CONSTRAINT uq_availability_doctor_day_start UNIQUE (doctor_id, day_of_week, start_time)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_availability_doctor_id ON doctor_availability(doctor_id);
CREATE INDEX idx_availability_day       ON doctor_availability(day_of_week);
