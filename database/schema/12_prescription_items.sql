-- =====================================================================
-- 12_prescription_items.sql
-- Hospital Management System — Medical: Prescription Items
-- =====================================================================
-- Purpose:
--   Line items of a prescription — one row per medicine prescribed,
--   with dosage/duration/quantity. Normalized out of `prescriptions`
--   because a prescription may include multiple medicines (3NF).
-- =====================================================================

USE hospital_management;

CREATE TABLE prescription_items (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    prescription_id     BIGINT NOT NULL,
    medicine_id         BIGINT NOT NULL,
    dosage              VARCHAR(100) NOT NULL,
    duration_days       INT NOT NULL,
    quantity            INT NOT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_prescription_items_prescription
        FOREIGN KEY (prescription_id) REFERENCES prescriptions(id)
        ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_prescription_items_medicine
        FOREIGN KEY (medicine_id) REFERENCES medicines(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT chk_prescription_items_duration CHECK (duration_days > 0),
    CONSTRAINT chk_prescription_items_quantity CHECK (quantity > 0)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_prescription_items_prescription_id ON prescription_items(prescription_id);
CREATE INDEX idx_prescription_items_medicine_id      ON prescription_items(medicine_id);
