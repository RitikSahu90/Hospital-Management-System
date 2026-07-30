-- =====================================================================
-- 19_audit_logs.sql
-- Hospital Management System — System: Audit Logs
-- =====================================================================
-- Purpose:
--   Immutable record of who did what, for compliance and traceability.
--   user_id is nullable to allow system-generated actions (e.g. an
--   automated job) that have no human actor, and is never cascaded on
--   delete — audit history must survive even if the user is removed.
-- =====================================================================

USE hospital_management;

CREATE TABLE audit_logs (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT NULL,
    action          VARCHAR(100) NOT NULL,
    entity_name     VARCHAR(100) NOT NULL,
    entity_id       BIGINT NOT NULL,
    details         TEXT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_audit_logs_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_audit_logs_user_id     ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_entity      ON audit_logs(entity_name, entity_id);
CREATE INDEX idx_audit_logs_created_at  ON audit_logs(created_at);
