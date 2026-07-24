-- =====================================================================
-- 14_medicines.sql
-- Hospital Management System — Pharmacy: Medicines
-- =====================================================================
-- Purpose:
--   Medicine catalog. Stock levels live separately in `inventory`
--   (1:1) so pricing/catalog data and fast-changing stock counts
--   don't compete for the same row locks.
-- =====================================================================

USE hospital_management;

CREATE TABLE medicines (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    supplier_id         BIGINT NOT NULL,
    name                VARCHAR(150) NOT NULL,
    manufacturer        VARCHAR(150) NULL,
    unit_price          DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_medicines_supplier
        FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT chk_medicines_price CHECK (unit_price >= 0)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_medicines_name        ON medicines(name);
CREATE INDEX idx_medicines_supplier_id ON medicines(supplier_id);
