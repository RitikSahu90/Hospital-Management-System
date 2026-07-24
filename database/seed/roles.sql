-- Seed: roles
USE hospital_management;

INSERT INTO roles (name, description) VALUES
    ('ADMIN',        'Full system access and administration'),
    ('DOCTOR',       'Doctor with access to patients, appointments, and prescriptions'),
    ('RECEPTIONIST', 'Front-desk staff managing patients and appointments'),
    ('PHARMACIST',   'Pharmacy staff managing medicines and inventory'),
    ('PATIENT',      'Patient portal access to own records and appointments');
