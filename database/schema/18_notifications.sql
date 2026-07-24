-- =====================================================================
-- 18_notifications.sql
-- Hospital Management System — System: Notifications
-- =====================================================================
-- Purpose:
--   In-app / email notifications sent to a user (appointment
--   reminders, bill due, low stock alerts to pharmacists, etc.).
-- =====================================================================

USE hospital_management;

CREATE TABLE notifications (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    type            ENUM('APPOINTMENT_REMINDER','BILL_DUE','LOW_STOCK','GENERAL')
                        NOT NULL DEFAULT 'GENERAL',
    title           VARCHAR(200) NOT NULL,
    message         VARCHAR(500) NOT NULL,
    is_read         BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_notifications_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_is_read ON notifications(is_read);
