-- =====================================================================
-- 22_operation_theatres.sql
-- Hospital Management System — Clinical: Operation Theatres
-- =====================================================================
-- Purpose:
--   operation_theatres: physical OT rooms.
--   ot_schedules: surgical booking records linked to patient,
--   surgeon (doctor), and a specific theatre room.
-- =====================================================================

USE hospital_management;

CREATE TABLE operation_theatres (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,   -- e.g. "OT-1", "Cardiac OT"
    floor       VARCHAR(50)  NULL,
    status      ENUM('AVAILABLE','OCCUPIED','MAINTENANCE')
                    NOT NULL DEFAULT 'AVAILABLE',
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                    ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE TABLE ot_schedules (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id          BIGINT NOT NULL,
    surgeon_id          BIGINT NOT NULL,        -- the primary surgeon (doctor)
    theatre_id          BIGINT NOT NULL,
    surgery_type        VARCHAR(200) NOT NULL,
    scheduled_at        DATETIME NOT NULL,
    estimated_duration  SMALLINT NULL,          -- minutes
    status              ENUM('SCHEDULED','IN_PROGRESS','COMPLETED','CANCELLED','POSTPONED')
                            NOT NULL DEFAULT 'SCHEDULED',
    pre_op_notes        TEXT NULL,
    surgery_notes       TEXT NULL,              -- filled in after completion
    post_op_notes       TEXT NULL,
    anaesthesiologist   VARCHAR(150) NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_ot_schedules_patient
        FOREIGN KEY (patient_id) REFERENCES patients(id)
        ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_ot_schedules_surgeon
        FOREIGN KEY (surgeon_id) REFERENCES doctors(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT fk_ot_schedules_theatre
        FOREIGN KEY (theatre_id) REFERENCES operation_theatres(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_ot_schedules_patient_id   ON ot_schedules(patient_id);
CREATE INDEX idx_ot_schedules_surgeon_id   ON ot_schedules(surgeon_id);
CREATE INDEX idx_ot_schedules_theatre_id   ON ot_schedules(theatre_id);
CREATE INDEX idx_ot_schedules_scheduled_at ON ot_schedules(scheduled_at);
CREATE INDEX idx_ot_schedules_status       ON ot_schedules(status);
