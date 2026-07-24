-- =====================================================================
-- 13_suppliers.sql
-- Hospital Management System — Pharmacy: Suppliers
-- =====================================================================
-- Purpose:
--   Pharmaceutical suppliers that provide medicines to the hospital
--   pharmacy.
-- =====================================================================

USE hospital_management;

CREATE TABLE suppliers (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(200) NOT NULL,
    contact_person      VARCHAR(150) NULL,
    phone               VARCHAR(20)  NOT NULL,
    email               VARCHAR(150) NULL,
    address             VARCHAR(255) NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_suppliers_name UNIQUE (name)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_suppliers_phone ON suppliers(phone);
