-- =====================================================================
-- 23_lab_radiology.sql
-- Hospital Management System — Clinical: Lab & Radiology
-- =====================================================================
-- Purpose:
--   lab_tests: catalog of available lab and radiology tests.
--   lab_orders: per-patient test orders placed by a doctor.
--   Results can be numeric values OR a URL to a file in S3 (for
--   imaging results like X-Ray / MRI scans).
-- =====================================================================

USE hospital_management;

CREATE TABLE lab_tests (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(200) NOT NULL,
    category        ENUM('HAEMATOLOGY','BIOCHEMISTRY','MICROBIOLOGY',
                         'PATHOLOGY','RADIOLOGY','CARDIOLOGY','OTHER')
                        NOT NULL DEFAULT 'OTHER',
    description     VARCHAR(500) NULL,
    normal_range    VARCHAR(100) NULL,   -- e.g. "70–100 mg/dL"
    unit            VARCHAR(50)  NULL,   -- e.g. "mg/dL", "mmol/L"
    price           DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT chk_lab_tests_price CHECK (price >= 0)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_lab_tests_category ON lab_tests(category);
CREATE INDEX idx_lab_tests_name     ON lab_tests(name);

-- -------------------------------------------------------

CREATE TABLE lab_orders (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id          BIGINT NOT NULL,
    test_id             BIGINT NOT NULL,
    ordered_by          BIGINT NOT NULL,            -- doctor_id
    appointment_id      BIGINT NULL,
    order_number        VARCHAR(30) NOT NULL UNIQUE,
    status              ENUM('PENDING','SAMPLE_COLLECTED','IN_PROGRESS',
                             'COMPLETED','CANCELLED')
                            NOT NULL DEFAULT 'PENDING',
    result_value        VARCHAR(200) NULL,           -- numeric/text result
    result_url          VARCHAR(500) NULL,           -- S3 URL for image results
    remarks             TEXT NULL,
    ordered_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at        TIMESTAMP NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_lab_orders_patient
        FOREIGN KEY (patient_id) REFERENCES patients(id)
        ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_lab_orders_test
        FOREIGN KEY (test_id) REFERENCES lab_tests(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT fk_lab_orders_doctor
        FOREIGN KEY (ordered_by) REFERENCES doctors(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT fk_lab_orders_appointment
        FOREIGN KEY (appointment_id) REFERENCES appointments(id)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_lab_orders_patient_id     ON lab_orders(patient_id);
CREATE INDEX idx_lab_orders_test_id        ON lab_orders(test_id);
CREATE INDEX idx_lab_orders_ordered_by     ON lab_orders(ordered_by);
CREATE INDEX idx_lab_orders_status         ON lab_orders(status);
CREATE INDEX idx_lab_orders_ordered_at     ON lab_orders(ordered_at);
