-- =====================================================================
-- 07_emergency_contacts.sql
-- Hospital Management System — Patient: Emergency Contacts
-- =====================================================================
-- Purpose:
--   A patient may have multiple emergency contacts (spouse, parent,
--   friend, etc.) — normalized out of `patients` to avoid repeating
--   groups and to allow more than one contact per patient (3NF).
-- =====================================================================

USE hospital_management;

CREATE TABLE emergency_contacts (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id          BIGINT NOT NULL,
    full_name           VARCHAR(200) NOT NULL,
    relationship        VARCHAR(50)  NOT NULL,
    phone               VARCHAR(20)  NOT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_emergency_contacts_patient
        FOREIGN KEY (patient_id) REFERENCES patients(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_emergency_contacts_patient_id ON emergency_contacts(patient_id);
CREATE INDEX idx_emergency_contacts_phone      ON emergency_contacts(phone);
