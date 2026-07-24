-- =====================================================================
-- 01_roles.sql
-- Hospital Management System — Authentication: Roles
-- =====================================================================
-- Purpose:
--   Lookup table defining the fixed set of system roles used for
--   role-based access control (RBAC). Every user account references
--   exactly one row here via a foreign key (see 02_users.sql).
-- =====================================================================

CREATE DATABASE IF NOT EXISTS hospital_management
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE hospital_management;

CREATE TABLE roles (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(50)  NOT NULL,
    description     VARCHAR(255) NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_roles_name UNIQUE (name)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;
