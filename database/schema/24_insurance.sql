-- =====================================================================
-- 24_insurance.sql
-- Hospital Management System — Financial: Insurance Claims
-- =====================================================================
-- Purpose:
--   insurance_providers: registered insurers / TPAs.
--   insurance_claims: claim submissions linked to a bill.
--   Tracks the full lifecycle from submission to settlement.
-- =====================================================================

USE hospital_management;

CREATE TABLE insurance_providers (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(200) NOT NULL,
    provider_code   VARCHAR(50)  NOT NULL UNIQUE,
    contact_phone   VARCHAR(20)  NULL,
    contact_email   VARCHAR(150) NULL,
    website         VARCHAR(255) NULL,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                        ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_insurance_providers_name ON insurance_providers(name);

-- -------------------------------------------------------

CREATE TABLE insurance_claims (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    bill_id             BIGINT NOT NULL,
    patient_id          BIGINT NOT NULL,
    provider_id         BIGINT NOT NULL,
    claim_number        VARCHAR(100) NOT NULL UNIQUE,
    policy_number       VARCHAR(100) NULL,
    amount_claimed      DECIMAL(12,2) NOT NULL,
    amount_approved     DECIMAL(12,2) NULL,
    amount_settled      DECIMAL(12,2) NULL,
    status              ENUM('DRAFT','SUBMITTED','UNDER_REVIEW','APPROVED',
                             'REJECTED','SETTLED','CANCELLED')
                            NOT NULL DEFAULT 'DRAFT',
    rejection_reason    VARCHAR(500) NULL,
    submitted_at        TIMESTAMP NULL,
    approved_at         TIMESTAMP NULL,
    settled_at          TIMESTAMP NULL,
    notes               TEXT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_claims_bill
        FOREIGN KEY (bill_id) REFERENCES bills(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT fk_claims_patient
        FOREIGN KEY (patient_id) REFERENCES patients(id)
        ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_claims_provider
        FOREIGN KEY (provider_id) REFERENCES insurance_providers(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT chk_claims_amount CHECK (amount_claimed > 0)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_claims_bill_id       ON insurance_claims(bill_id);
CREATE INDEX idx_claims_patient_id    ON insurance_claims(patient_id);
CREATE INDEX idx_claims_provider_id   ON insurance_claims(provider_id);
CREATE INDEX idx_claims_status        ON insurance_claims(status);
CREATE INDEX idx_claims_submitted_at  ON insurance_claims(submitted_at);
