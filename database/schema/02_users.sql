-- =====================================================================
-- 02_users.sql
-- Hospital Management System — Authentication: Users
-- =====================================================================
-- Purpose:
--   Central authentication table. Every human actor in the system
--   (admin, doctor, receptionist, pharmacist, patient) has exactly one
--   row here, linked to exactly one role. Doctors and patients extend
--   this table via a 1:1 foreign key in their own tables.
-- =====================================================================

USE hospital_management;

CREATE TABLE users (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_id         BIGINT NOT NULL,
    username        VARCHAR(100)  NOT NULL,
    email           VARCHAR(150)  NOT NULL,
    password_hash   VARCHAR(255)  NOT NULL,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    last_login_at   TIMESTAMP NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_users_username UNIQUE (username),
    CONSTRAINT uq_users_email    UNIQUE (email),

    CONSTRAINT fk_users_role
        FOREIGN KEY (role_id) REFERENCES roles(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_users_role_id ON users(role_id);
