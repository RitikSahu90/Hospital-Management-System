-- =====================================================================
-- 17_payments.sql
-- Hospital Management System — Billing: Payments
-- =====================================================================
-- Purpose:
--   Individual payment transactions against a bill. Separated from
--   `bills` because a bill may be paid in multiple installments
--   (PARTIALLY_PAID status in bills reflects this).
-- =====================================================================

USE hospital_management;

CREATE TABLE payments (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    bill_id             BIGINT NOT NULL,
    amount              DECIMAL(10,2) NOT NULL,
    payment_method      ENUM('CASH','CARD','UPI','INSURANCE') NOT NULL,
    paid_at             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_payments_bill
        FOREIGN KEY (bill_id) REFERENCES bills(id)
        ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT chk_payments_amount CHECK (amount > 0)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_payments_bill_id ON payments(bill_id);
