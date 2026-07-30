-- =====================================================================
-- 15_inventory.sql
-- Hospital Management System — Pharmacy: Inventory
-- =====================================================================
-- Purpose:
--   Current stock level for each medicine (1:1 with medicines).
-- =====================================================================

USE hospital_management;

CREATE TABLE inventory (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    medicine_id         BIGINT NOT NULL,
    stock_quantity      INT NOT NULL DEFAULT 0,
    reorder_level       INT NOT NULL DEFAULT 10,
    expiry_date         DATE NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_inventory_medicine_id UNIQUE (medicine_id),

    CONSTRAINT fk_inventory_medicine
        FOREIGN KEY (medicine_id) REFERENCES medicines(id)
        ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT chk_inventory_stock CHECK (stock_quantity >= 0),
    CONSTRAINT chk_inventory_reorder CHECK (reorder_level >= 0)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_inventory_expiry_date ON inventory(expiry_date);
